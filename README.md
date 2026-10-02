# Inventory Management System

A console-based inventory management application built with **Core Java, JDBC and MySQL**.
It manages product details and stock information, tracks stock quantities, and calculates total prices.

## Features
- Register new products (name, category, price, opening stock)
- View all products with the **total value** of each product's stock (price x quantity)
- Search products by name
- Update product details
- Add stock and remove stock, with a check that stock never goes below zero
- Delete products (with confirmation)
- Low stock report for products at or below a chosen quantity
- Total inventory value across all products
- Input validation and clear error messages

## Concepts used
- **Object-oriented programming:** a `Product` model class with encapsulation (private fields, getters and setters)
- **JDBC:** connections, `PreparedStatement`, `ResultSet`, try-with-resources
- **Layered design:** model, DAO (database access), service (business rules) and console UI kept separate
- **Core Java:** collections (`List`, `Optional`), exception handling, `Scanner` input, `switch` expressions

## Tech stack
| Layer | Technology |
|-------|------------|
| Language | Java 17 |
| Database | MySQL |
| Database access | JDBC (MySQL Connector/J) |
| Build tool | Maven |

## Project structure
```
src/main/java/com/ditisha/inventory
├── InventoryApp.java            Console menu (entry point)
├── model/Product.java           Product entity
├── db/DBConnection.java         JDBC connection and table setup
├── dao/ProductDAO.java          All SQL queries
└── service/InventoryService.java  Validation and business rules
schema.sql                       Table definition and sample data
```

## Database
Table `products`:

| Column | Type |
|--------|------|
| id | INT, primary key, auto increment |
| name | VARCHAR(100), not null |
| category | VARCHAR(50) |
| price | DECIMAL(10,2), not null |
| quantity | INT, not null, default 0 |
| created_at | TIMESTAMP |

The app creates the database `inventory_db` and this table automatically on first run. `schema.sql` is included if you prefer to create them yourself or load sample data.

## How to run
**Prerequisites:** JDK 17+, MySQL, Maven (or an IDE such as IntelliJ IDEA)

1. Make sure MySQL is running.
2. Set your MySQL login as environment variables (no password is stored in the code):
   ```
   DB_USERNAME=root
   DB_PASSWORD=your_password
   ```
   In IntelliJ: Run > Edit Configurations > Environment variables, then enter `DB_USERNAME=root;DB_PASSWORD=your_password`.
3. Run `InventoryApp.java` (in IntelliJ, click the green Run arrow next to `main`).
4. Use the menu in the Run window by typing a number and pressing Enter.

Optional settings: `DB_URL` can override the default `jdbc:mysql://localhost:3306/inventory_db?createDatabaseIfNotExist=true`.

## Sample output
```
=== Inventory Management System ===

1. Register new product
2. View all products
...
Enter your choice: 2
------------------------------------------------------------------------------
ID   Name                     Category            Price    Qty    Total Value
------------------------------------------------------------------------------
1    Notebook A4              Stationery          60.00    120        7200.00
2    Ball Pen (Blue)          Stationery          10.00    300        3000.00
3    Wireless Mouse           Electronics        350.00      4        1400.00
------------------------------------------------------------------------------
Total inventory value: Rs. 11600.00
```

## Screenshots
_![Console output](screenshots/console window Inventory.png)
![MySQL table](screenshots/Mysql Inventory.png)

## Future improvements
- Supplier and purchase order tracking
- Stock movement history (who added or removed stock, and when)
- Unit tests with JUnit
- Export reports to CSV

## Author
Ditisha Mohite, BE Information Technology
