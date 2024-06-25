import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderBookTest {
    private OrderBook orderBook;

    @BeforeEach
    public void setUp() {
        orderBook = new OrderBook();
    }

    @Test
    public void testAddOrder() {
        Order buyOrder = new Order(100.0, 10, true);
        Order sellOrder = new Order(105.0, 5, false);

        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);

        List<Order> buyOrders = orderBook.getOrdersAtPriceLevel(100.0, true);
        List<Order> sellOrders = orderBook.getOrdersAtPriceLevel(105.0, false);

        assertEquals(1, buyOrders.size());
        assertEquals(1, sellOrders.size());
        assertEquals(buyOrder, buyOrders.get(0));
        assertEquals(sellOrder, sellOrders.get(0));
    }

    @Test
    public void testDeleteOrder() {
        Order buyOrder = new Order(100.0, 10, true);
        orderBook.addOrder(buyOrder);

        orderBook.deleteOrder(buyOrder.getId());
        List<Order> buyOrders = orderBook.getOrdersAtPriceLevel(100.0, true);

        assertTrue(buyOrders.isEmpty());
    }

    @Test
    public void testModifyOrder() {
        Order buyOrder = new Order(100.0, 10, true);
        orderBook.addOrder(buyOrder);

        orderBook.modifyOrder(buyOrder.getId(), 20);
        List<Order> buyOrders = orderBook.getOrdersAtPriceLevel(100.0, true);

        assertEquals(1, buyOrders.size());
        assertEquals(20, buyOrders.get(0).getQuantity());
    }

    @Test
    public void testMatchOrders() {
        Order buyOrder = new Order(100.0, 10, true);
        Order sellOrder = new Order(95.0, 5, false);
        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);

        List<Order> buyOrders = orderBook.getOrdersAtPriceLevel(100.0, true);
        List<Order> sellOrders = orderBook.getOrdersAtPriceLevel(95.0, false);

        assertEquals(1, buyOrders.size());
        assertEquals(0, sellOrders.size());
        assertEquals(5, buyOrders.getFirst().getQuantity());
    }

    @Test
    public void testMultipleMatches() {
        Order buyOrder1 = new Order(100.0, 10, true);
        Order buyOrder2 = new Order(101.0, 15, true);
        Order sellOrder1 = new Order(95.0, 5, false);
        Order sellOrder2 = new Order(96.0, 25, false);

        orderBook.addOrder(buyOrder1);
        orderBook.addOrder(buyOrder2);
        orderBook.addOrder(sellOrder1);
        orderBook.addOrder(sellOrder2);

        // Assertions after all matches are processed
        List<Order> remainingBuyOrdersAt100 = orderBook.getOrdersAtPriceLevel(100.0, true);
        List<Order> remainingBuyOrdersAt101 = orderBook.getOrdersAtPriceLevel(101.0, true);

        assertTrue(remainingBuyOrdersAt100.isEmpty());
        assertTrue(remainingBuyOrdersAt101.isEmpty());

        List<Order> remainingSellOrdersAt95 = orderBook.getOrdersAtPriceLevel(95.0, false);
        List<Order> remainingSellOrdersAt96 = orderBook.getOrdersAtPriceLevel(96.0, false);

        assertTrue(remainingSellOrdersAt95.isEmpty());
        assertEquals(1, remainingSellOrdersAt96.size());
        assertEquals(5, remainingSellOrdersAt96.getFirst().getQuantity());
    }




    @Test
    public void testNoMatchOrders() {
        Order buyOrder = new Order(90.0, 10, true);
        Order sellOrder = new Order(95.0, 5, false);
        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);

        List<Order> buyOrders = orderBook.getOrdersAtPriceLevel(90.0, true);
        List<Order> sellOrders = orderBook.getOrdersAtPriceLevel(95.0, false);

        assertEquals(1, buyOrders.size());
        assertEquals(1, sellOrders.size());
        assertEquals(buyOrder, buyOrders.get(0));
        assertEquals(sellOrder, sellOrders.get(0));
    }
}
