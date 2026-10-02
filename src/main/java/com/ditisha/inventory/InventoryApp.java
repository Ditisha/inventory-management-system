package com.ditisha.inventory;

import com.ditisha.inventory.db.DBConnection;
import com.ditisha.inventory.model.Product;
import com.ditisha.inventory.service.InventoryService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/** Console menu for the Inventory Management System. */
public class InventoryApp {

    private static final Scanner in = new Scanner(System.in);
    private static final InventoryService service = new InventoryService();

    public static void main(String[] args) {
        try {
            DBConnection.initSchema();
        } catch (SQLException e) {
            System.out.println("Could not connect to MySQL: " + e.getMessage());
            System.out.println("Check that MySQL is running and that DB_USERNAME and DB_PASSWORD are set correctly.");
            return;
        }

        System.out.println("=== Inventory Management System ===");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");
            try {
                switch (choice) {
                    case 1 -> addProduct();
                    case 2 -> viewAll();
                    case 3 -> searchProducts();
                    case 4 -> updateDetails();
                    case 5 -> addStock();
                    case 6 -> removeStock();
                    case 7 -> deleteProduct();
                    case 8 -> lowStockReport();
                    case 9 -> System.out.printf("Total inventory value: Rs. %.2f%n", service.totalInventoryValue());
                    case 0 -> {
                        System.out.println("Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please enter a number from 0 to 9.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1. Register new product");
        System.out.println("2. View all products");
        System.out.println("3. Search product by name");
        System.out.println("4. Update product details");
        System.out.println("5. Add stock");
        System.out.println("6. Remove stock");
        System.out.println("7. Delete product");
        System.out.println("8. Low stock report");
        System.out.println("9. Total inventory value");
        System.out.println("0. Exit");
    }

    private static void addProduct() throws SQLException {
        String name = readRequired("Product name: ");
        String category = readOptional("Category (press Enter for General): ");
        if (category.isEmpty()) {
            category = "General";
        }
        double price = readDouble("Price (Rs.): ");
        int quantity = readInt("Opening stock quantity: ");
        int id = service.registerProduct(name, category, price, quantity);
        System.out.println("Product registered with ID " + id);
    }

    private static void viewAll() throws SQLException {
        List<Product> products = service.getAllProducts();
        printTable(products);
        if (!products.isEmpty()) {
            System.out.printf("Total inventory value: Rs. %.2f%n", service.totalInventoryValue());
        }
    }

    private static void searchProducts() throws SQLException {
        printTable(service.search(readRequired("Enter name or part of name: ")));
    }

    private static void updateDetails() throws SQLException {
        int id = readInt("Product ID: ");
        Optional<Product> found = service.getProduct(id);
        if (found.isEmpty()) {
            System.out.println("Product not found with ID " + id);
            return;
        }
        Product p = found.get();
        System.out.println("Press Enter to keep the current value.");
        String name = readOptional("Name [" + p.getName() + "]: ");
        String category = readOptional("Category [" + p.getCategory() + "]: ");
        String priceText = readOptional("Price [" + p.getPrice() + "]: ");

        double price = p.getPrice();
        if (!priceText.isEmpty()) {
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Price must be a number");
            }
        }
        service.updateProduct(id, name.isEmpty() ? p.getName() : name,
                category.isEmpty() ? p.getCategory() : category, price);
        System.out.println("Product updated.");
    }

    private static void addStock() throws SQLException {
        int id = readInt("Product ID: ");
        int amount = readInt("Quantity to add: ");
        System.out.println("Stock updated. New quantity: " + service.addStock(id, amount));
    }

    private static void removeStock() throws SQLException {
        int id = readInt("Product ID: ");
        int amount = readInt("Quantity to remove: ");
        System.out.println("Stock updated. New quantity: " + service.removeStock(id, amount));
    }

    private static void deleteProduct() throws SQLException {
        int id = readInt("Product ID to delete: ");
        String answer = readOptional("Are you sure? (y/n): ");
        if (answer.equalsIgnoreCase("y")) {
            service.deleteProduct(id);
            System.out.println("Product deleted.");
        } else {
            System.out.println("Cancelled.");
        }
    }

    private static void lowStockReport() throws SQLException {
        int threshold = readInt("Show products with stock at or below: ");
        printTable(service.lowStock(threshold));
    }

    private static void printTable(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        String line = "-".repeat(78);
        System.out.println(line);
        System.out.printf("%-4s %-24s %-14s %10s %6s %14s%n", "ID", "Name", "Category", "Price", "Qty", "Total Value");
        System.out.println(line);
        for (Product p : products) {
            System.out.printf("%-4d %-24s %-14s %10.2f %6d %14.2f%n", p.getId(), cut(p.getName(), 24),
                    cut(p.getCategory(), 14), p.getPrice(), p.getQuantity(), p.getTotalValue());
        }
        System.out.println(line);
    }

    private static String cut(String text, int max) {
        if (text == null) {
            return "";
        }
        return text.length() <= max ? text : text.substring(0, max - 1) + ".";
    }

    // ---- input helpers ----

    private static String readOptional(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    private static String readRequired(String prompt) {
        while (true) {
            String value = readOptional(prompt);
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This field cannot be empty.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readOptional(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readOptional(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
