package com.ditisha.inventory.model;

/** A product stored in the inventory. */
public class Product {

    private int id;
    private String name;
    private String category;
    private double price;
    private int quantity;

    // Used when registering a new product (the database assigns the id)
    public Product(String name, String category, double price, int quantity) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    // Used when reading an existing product from the database
    public Product(int id, String name, String category, double price, int quantity) {
        this(name, category, price, quantity);
        this.id = id;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // Total price of the stock held for this product
    public double getTotalValue() {
        return price * quantity;
    }
}
