# RMB-Technical-Assessment

This project implements an order book matching engine for a simple trading system. The engine handles buy and sell orders, matching them based on price-time priority. The system supports adding, deleting, and modifying orders, as well as displaying the current state of the order book.

## Features

- **Add Orders**: Add buy or sell orders to the order book.
- **Delete Orders**: Delete orders from the order book by order ID.
- **Modify Orders**: Modify the quantity of existing orders.
- **Match Orders**: Automatically match buy and sell orders based on price-time priority.
- **Display Order Book**: Display the current state of the order book, showing all buy and sell orders.

## Classes and Methods

### Order Class

Represents an individual order with a unique ID, price, quantity, and order type (BUY or SELL determined by a boolean).

- **Fields**:
  - `id`: Unique identifier for the order.
  - `price`: Price of the order.
  - `quantity`: Quantity of the order.
  - `isBuy`: Boolean indicating if the order is a buy order (`true`) or a sell order (`false`).

- **Methods**:
  - `getId()`: Returns the order ID.
  - `getPrice()`: Returns the order price.
  - `getQuantity()`: Returns the order quantity.
  - `setQuantity(int quantity)`: Sets the order quantity.
  - `isBuy()`: Returns `true` if the order is a buy order, `false` if it is a sell order.

### OrderQueue Class

Manages a queue of orders at a specific price level.

- **Methods**:
  - `add(Order order)`: Adds an order to the queue.
  - `remove(Order order)`: Removes an order from the queue.
  - `modify(Order order, int newQuantity)`: Modifies the quantity of an existing order.
  - `poll()`: Retrieves and removes the head of the queue.
  - `isEmpty()`: Checks if the queue is empty.
  - `getOrders()`: Returns the list of orders in the queue.

### OrderBook Class

Manages the order book, handling the addition, deletion, modification, and matching of orders.

- **Fields**:
  - `buyOrders`: A `NavigableMap` of buy orders, sorted by price in descending order.
  - `sellOrders`: A `NavigableMap` of sell orders, sorted by price in ascending order.

- **Methods**:
  - `addOrder(Order order)`: Adds an order to the order book and attempts to match orders.
  - `deleteOrder(int orderId)`: Deletes an order from the order book by order ID.
  - `modifyOrder(int orderId, int newQuantity)`: Modifies the quantity of an existing order by order ID.
  - `getOrdersAtPriceLevel(double price, boolean isBuy)`: Returns a list of orders at a specific price level and side (buy/sell).
  - `matchOrders()`: Matches buy and sell orders and updates the order book.
  - `displayOrderBook()`: Displays the current state of the order book.

## Unit Tests

### OrderBookTest Class

Contains unit tests for the `OrderBook` class, verifying the correct functionality of adding, deleting, modifying, and matching orders.

- **Methods**:
  - `testAddOrder()`: Tests adding buy and sell orders to the order book.
  - `testDeleteOrder()`: Tests deleting an order from the order book.
  - `testModifyOrder()`: Tests modifying an existing order in the order book.
  - `testMatchOrders()`: Tests matching a buy order with a sell order.
  - `testMultipleMatches()`: Tests matching multiple buy and sell orders.
  - `testNoMatchOrders()`: Tests that no match occurs when buy and sell prices do not overlap.

## How to Run the Project

1. **Clone the Repository**: 
    ```sh
    git clone <repository-url>
    cd order-book-matching-engine
    ```

2. **Open the Project in IntelliJ IDEA**:
    - Open IntelliJ IDEA.
    - Click on `File > Open` and select the project directory.

3. **Add JUnit 5 Dependency**:
    - Go to `File > Project Structure > Modules`.
    - Select your module and then the `Dependencies` tab.
    - Click the `+` button and add `JUnit5` as a dependency.

4. **Run the Tests**:
    - Right-click on the `OrderBookTest` class in the Project Explorer.
    - Select `Run 'OrderBookTest'`.


