-- The app creates this database and table automatically on first run.
-- Use this file if you want to create them yourself or load sample data.
CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

CREATE TABLE IF NOT EXISTS products (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100)  NOT NULL,
    category   VARCHAR(50),
    price      DECIMAL(10,2) NOT NULL,
    quantity   INT           NOT NULL DEFAULT 0,
    created_at TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);

-- Optional sample data
INSERT INTO products (name, category, price, quantity) VALUES
    ('Notebook A4',      'Stationery',  60.00, 120),
    ('Ball Pen (Blue)',  'Stationery',  10.00, 300),
    ('USB Keyboard',     'Electronics', 450.00, 25),
    ('Wireless Mouse',   'Electronics', 350.00, 4),
    ('Desk Lamp',        'Furniture',   799.00, 8);
