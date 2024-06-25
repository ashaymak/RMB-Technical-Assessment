import java.util.*;

/**
 * OrderBook class to manage buy and sell orders.
 */
public class OrderBook {
    // Create separate TreeMaps for buy and sell order for matching efficiency
    private final NavigableMap<Double, OrderQueue> buyOrders;
    private final NavigableMap<Double, OrderQueue> sellOrders;

    /**
     * Constructs an empty OrderBook.
     */
    public OrderBook() {
        // Highest price first
        buyOrders = new TreeMap<>(Collections.reverseOrder());

        // Lowest price first
        sellOrders = new TreeMap<>();
    }

    /**
     * Adds an order to the order book and attempts to match orders.
     *
     * @param order the order to add
     */
    public void addOrder(Order order) {

        // Get the correct half of the order book
        NavigableMap<Double, OrderQueue> orders = order.isBuy() ? buyOrders : sellOrders;

        // Create a new OrderQueue if one doesn't exist at that price yet
        orders.putIfAbsent(order.getPrice(), new OrderQueue());

        // Add the order at the right price
        orders.get(order.getPrice()).add(order);

        // Attempt to match orders whenever a new order is added
        matchOrders();
    }

    /**
     * Deletes an order from the order book.
     *
     * @param orderId the ID of the order to delete
     */
    public void deleteOrder(int orderId) {
        // Help delete efficiency by extracted buy or sell side from orderID
        boolean isBuy = (orderId & 1) == 1;
        if (!removeOrderFromMap(orderId, isBuy ? buyOrders : sellOrders)) {
            System.out.println("Order not found: " + orderId);
        }
    }

    private boolean removeOrderFromMap(int orderId, NavigableMap<Double, OrderQueue> orders) {
        // Loop through orders in buy or sell side
        for (Map.Entry<Double, OrderQueue> entry : orders.entrySet()) {
            OrderQueue queue = entry.getValue();
            // Loop through the OrderQueue
            for (Order order : queue.getOrders()) {
                if (order.getId() == orderId) {
                    // Remove the order
                    queue.remove(order);
                    if (queue.isEmpty()) {
                        // Remove the price level if empty
                        orders.remove(entry.getKey());
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Modifies an order in the order book.
     *
     * @param orderId     the ID of the order to modify
     * @param newQuantity the new quantity of the order
     */
    public void modifyOrder(int orderId, int newQuantity) {
        boolean isBuy = (orderId & 1) == 1;
        if (!modifyOrderInMap(orderId, newQuantity, isBuy ? buyOrders : sellOrders)) {
            System.out.println("Order not found: " + orderId);
        }
    }

    private boolean modifyOrderInMap(int orderId, int newQuantity, NavigableMap<Double, OrderQueue> orders) {
        // Loop through prices in order book
        for (Map.Entry<Double, OrderQueue> entry : orders.entrySet()) {
            OrderQueue queue = entry.getValue();
            // Loop through orders in queue
            for (Order order : queue.getOrders()) {
                if (order.getId() == orderId) {
                    // Modify the order, and deprioritize
                    queue.modify(order, newQuantity);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the orders at a specific price level and side (buy/sell).
     *
     * @param price the price level
     * @param isBuy true if looking for buy orders, false if looking for sell orders
     * @return a list of orders at the specified price level and side
     */
    public List<Order> getOrdersAtPriceLevel(double price, boolean isBuy) {
        NavigableMap<Double, OrderQueue> orders = isBuy ? buyOrders : sellOrders;
        return new ArrayList<>(orders.getOrDefault(price, new OrderQueue()).getOrders());
    }

    /**
     * Matches buy and sell orders and updates the order book.
     */
    public void matchOrders() {

        // Check that there are buy and sell orders left
        while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {

            double highestBuyPrice = buyOrders.firstKey();
            double lowestSellPrice = sellOrders.firstKey();

            // Check if an order can be sold for asking price or higher
            if (highestBuyPrice >= lowestSellPrice) {
                // Get the orders according their priority
                OrderQueue buyQueue = buyOrders.get(highestBuyPrice);
                OrderQueue sellQueue = sellOrders.get(lowestSellPrice);

                // Get the first orders that were placed at the right price
                Order buyOrder = buyQueue.poll();
                Order sellOrder = sellQueue.poll();

                // Find the new quantity after the trade
                final int matchQuantity = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
                buyOrder.setQuantity(buyOrder.getQuantity() - matchQuantity);
                sellOrder.setQuantity(sellOrder.getQuantity() - matchQuantity);

                // Add orders back to queue if they still have a quantity
                if (buyOrder.getQuantity() > 0) {
                    buyQueue.add(buyOrder);
                }
                if (sellOrder.getQuantity() > 0) {
                    sellQueue.add(sellOrder);
                }

                // Remove the queues from the order book if they are empty
                if (buyQueue.isEmpty()) {
                    buyOrders.remove(highestBuyPrice);
                }
                if (sellQueue.isEmpty()) {
                    sellOrders.remove(lowestSellPrice);
                }
            } else {
                // No more matches possible
                break;
            }
        }
    }
}
