import java.util.*;

public class PerformanceTest {

    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();
        Random random = new Random();
        // Number of orders to test with
        int numOrders = 100000;

        // Data structures to store time measurements and order IDs
        List<Long> addTimes = new ArrayList<>();
        List<Long> deleteTimes = new ArrayList<>();
        List<Long> modifyTimes = new ArrayList<>();
        List<Integer> orderIds = new ArrayList<>();

        // Generate initial buy and sell orders and measure add time
        for (int i = 0; i < numOrders; i++) {
            double price = 100.0 + random.nextInt(50) - 25;
            int quantity = 10 + random.nextInt(10);
            boolean isBuy = random.nextBoolean();

            Order order = new Order(price, quantity, isBuy);
            orderIds.add(order.getId()); // Store the generated order ID
            long startTime = System.nanoTime();
            orderBook.addOrder(order);
            long endTime = System.nanoTime();
            addTimes.add(endTime - startTime);
        }

        // Measure modify time using valid order IDs
        // Shuffle to randomize modify order
        Collections.shuffle(orderIds);
        for (int i = 0; i < numOrders; i++) {
            int orderId = orderIds.get(i);
            int newQuantity = 10 + random.nextInt(10);
            long startTime = System.nanoTime();
            orderBook.modifyOrder(orderId, newQuantity);
            long endTime = System.nanoTime();
            modifyTimes.add(endTime - startTime);
        }

        // Measure delete time using valid order IDs
        Collections.shuffle(orderIds);
        for (int i = 0; i < numOrders; i++) {
            int orderId = orderIds.get(i);
            long startTime = System.nanoTime();
            orderBook.deleteOrder(orderId);
            long endTime = System.nanoTime();
            deleteTimes.add(endTime - startTime);
        }

        // Print summary statistics
        printStatistics("Add Operation", addTimes);
        printStatistics("Delete Operation", deleteTimes);
        printStatistics("Modify Operation", modifyTimes);
    }

    private static void printStatistics(String operation, List<Long> times) {
        double average = times.stream().mapToLong(Long::longValue).average().orElse(0);
        long min = times.stream().mapToLong(Long::longValue).min().orElse(0);
        long max = times.stream().mapToLong(Long::longValue).max().orElse(0);

        System.out.println(operation + " - Average: " + average + " ns, Min: " + min + " ns, Max: " + max + " ns");
    }
}
