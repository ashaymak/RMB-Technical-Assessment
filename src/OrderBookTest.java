import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderBookTest {
    private OrderBook orderBook;

    @BeforeEach
    public void setUp() {
        // Initialize a OrderBook before any test
        orderBook = new OrderBook();
    }

    @Test
    public void testAddOrder() {
        // Create buy and sell order that won't match
        Order buyOrder = new Order(100.0, 10, true);
        Order sellOrder = new Order(105.0, 5, false);

        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);

        // Get the Orders at the price level to confirm that they were added
        List<Order> buyOrders = orderBook.getOrdersAtPriceLevel(100.0, true);
        List<Order> sellOrders = orderBook.getOrdersAtPriceLevel(105.0, false);

        // Test that the orders are in the order book
        assertEquals(1, buyOrders.size());
        assertEquals(1, sellOrders.size());
        assertEquals(buyOrder, buyOrders.getFirst());
        assertEquals(sellOrder, sellOrders.getFirst());
    }

    @Test
    public void testDeleteOrder() {
        // Create a sell order
        Order buyOrder = new Order(100.0, 10, true);
        orderBook.addOrder(buyOrder);

        // Test delete by orderID
        orderBook.deleteOrder(buyOrder.getId());
        List<Order> buyOrders = orderBook.getOrdersAtPriceLevel(100.0, true);

        assertTrue(buyOrders.isEmpty());
    }

    @Test
    public void testModifyOrder() {
        // Create an arbitrary order
        Order buyOrder = new Order(100.0, 10, true);
        orderBook.addOrder(buyOrder);

        // Modify by orderID with new quantity
        orderBook.modifyOrder(buyOrder.getId(), 20);
        List<Order> buyOrders = orderBook.getOrdersAtPriceLevel(100.0, true);

        // Check that order has been modified
        assertEquals(1, buyOrders.size());
        assertEquals(20, buyOrders.getFirst().getQuantity());
    }

    @Test
    public void testMatchOrders() {
        // Create 2 orders that will match
        Order buyOrder = new Order(100.0, 10, true);
        Order sellOrder = new Order(95.0, 5, false);

        // Adding to the order book will invoke matching
        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);

        List<Order> buyOrders = orderBook.getOrdersAtPriceLevel(100.0, true);
        List<Order> sellOrders = orderBook.getOrdersAtPriceLevel(95.0, false);

        // Check that match occurred and quantities are correct
        assertEquals(1, buyOrders.size());
        assertEquals(0, sellOrders.size());
        assertEquals(5, buyOrders.getFirst().getQuantity());
    }

    @Test
    public void testMultipleMatches() {
        // Create multiple orders that will match
        Order buyOrder1 = new Order(100.0, 10, true);
        Order buyOrder2 = new Order(101.0, 15, true);
        Order sellOrder1 = new Order(95.0, 5, false);
        Order sellOrder2 = new Order(96.0, 25, false);

        // Add orders to order book and invoke matching
        orderBook.addOrder(buyOrder1);
        orderBook.addOrder(buyOrder2);
        orderBook.addOrder(sellOrder1);
        orderBook.addOrder(sellOrder2);

        List<Order> remainingBuyOrdersAt100 = orderBook.getOrdersAtPriceLevel(100.0, true);
        List<Order> remainingBuyOrdersAt101 = orderBook.getOrdersAtPriceLevel(101.0, true);

        // Both buy orders should be filled
        assertTrue(remainingBuyOrdersAt100.isEmpty());
        assertTrue(remainingBuyOrdersAt101.isEmpty());

        List<Order> remainingSellOrdersAt95 = orderBook.getOrdersAtPriceLevel(95.0, false);
        List<Order> remainingSellOrdersAt96 = orderBook.getOrdersAtPriceLevel(96.0, false);

        // The lowest sell order was filled first, leaving quantity in the higher sell order
        assertTrue(remainingSellOrdersAt95.isEmpty());
        assertEquals(1, remainingSellOrdersAt96.size());
        assertEquals(5, remainingSellOrdersAt96.getFirst().getQuantity());
    }
}
