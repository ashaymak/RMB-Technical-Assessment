/**
 * Represents an order in the order book.
 */
public class Order {
    public static int idCounter = 0;
    private final int id;
    private final double price;
    private int quantity;
    private final boolean isBuy; // true for buy, false for sell

    /**
     * Constructs an Order with the specified price, quantity, and side.
     *
     * @param price    the price of the order
     * @param quantity the quantity of the order
     * @param isBuy    true if the order is a buy order, false if it is a sell order
     */
    public Order(double price, int quantity, boolean isBuy) {
        this.id = generateId(isBuy);
        this.price = price;
        this.quantity = quantity;
        this.isBuy = isBuy;
    }

    /**
     * Generates a unique ID for the order, encoding the buy/sell side.
     *
     * @param isBuy true if the order is a buy order, false if it is a sell order
     * @return a unique ID with the last bit representing the buy/sell side
     */
    private int generateId(boolean isBuy) {
        int id = (idCounter << 1) | (isBuy ? 1 : 0);
        idCounter++;
        return id;
    }

    /**
     * Gets the ID of the order.
     *
     * @return the ID of the order
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the price of the order.
     *
     * @return the price of the order
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the quantity of the order.
     *
     * @return the quantity of the order
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the order.
     *
     * @param quantity the new quantity of the order
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the side of the order.
     *
     * @return true if the order is a buy order, false if it is a sell order
     */
    public boolean isBuy() {
        return (id & 1) == 1;
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", price=" + price + ", quantity=" + quantity + ", isBuy=" + isBuy + '}';
    }
}
