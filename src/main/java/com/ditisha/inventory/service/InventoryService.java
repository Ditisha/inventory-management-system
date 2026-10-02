package com.ditisha.inventory.service;

import com.ditisha.inventory.dao.ProductDAO;
import com.ditisha.inventory.model.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** Business rules and validation. The console menu talks to this class, and this class talks to the DAO. */
public class InventoryService {

    private final ProductDAO dao = new ProductDAO();

    public int registerProduct(String name, String category, double price, int quantity) throws SQLException {
        validate(name, price);
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        return dao.add(new Product(name.trim(), category.trim(), price, quantity));
    }

    public List<Product> getAllProducts() throws SQLException {
        return dao.findAll();
    }

    public Optional<Product> getProduct(int id) throws SQLException {
        return dao.findById(id);
    }

    public List<Product> search(String keyword) throws SQLException {
        return dao.searchByName(keyword.trim());
    }

    public List<Product> lowStock(int threshold) throws SQLException {
        return dao.findLowStock(threshold);
    }

    public void updateProduct(int id, String name, String category, double price) throws SQLException {
        validate(name, price);
        Product p = require(id);
        p.setName(name.trim());
        p.setCategory(category.trim());
        p.setPrice(price);
        dao.update(p);
    }

    public int addStock(int id, int amount) throws SQLException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        int newQuantity = require(id).getQuantity() + amount;
        dao.updateQuantity(id, newQuantity);
        return newQuantity;
    }

    public int removeStock(int id, int amount) throws SQLException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        Product p = require(id);
        if (amount > p.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + p.getQuantity());
        }
        int newQuantity = p.getQuantity() - amount;
        dao.updateQuantity(id, newQuantity);
        return newQuantity;
    }

    public void deleteProduct(int id) throws SQLException {
        if (!dao.delete(id)) {
            throw new IllegalArgumentException("Product not found with ID " + id);
        }
    }

    public double totalInventoryValue() throws SQLException {
        return dao.totalInventoryValue();
    }

    private Product require(int id) throws SQLException {
        return dao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID " + id));
    }

    private void validate(String name, double price) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
    }
}
