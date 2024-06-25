import java.util.*;

/**
 * OrderQueue class to manage a queue of orders.
 */
public class OrderQueue {
    private final Deque<Order> queue;

    /**
     * Constructs an empty OrderQueue.
     */
    public OrderQueue() {
        this.queue = new ArrayDeque<>();
    }

    /**
     * Adds an order to the queue.
     *
     * @param order the order to add
     */
    public void add(Order order) {
        queue.offer(order);
    }

    /**
     * Removes an order from the queue.
     *
     * @param order the order to remove
     */
    public void remove(Order order) {
        queue.remove(order);
    }

    /**
     * Modifies an order in the queue.
     *
     * @param order       the order to modify
     * @param newQuantity the new quantity of the order
     */
    public void modify(Order order, int newQuantity) {
        queue.remove(order);
        order.setQuantity(newQuantity);
        queue.offer(order);
    }

    /**
     * Retrieves and removes the head of the queue.
     *
     * @return the head of the queue
     */
    public Order poll() {
        return queue.poll();
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Returns a collection of orders in the queue.
     *
     * @return a collection of orders
     */
    public Collection<Order> getOrders() {
        return new ArrayList<>(queue);
    }

    @Override
    public String toString() {
        return "OrderQueue{" + "queue=" + queue + '}';
    }
}
