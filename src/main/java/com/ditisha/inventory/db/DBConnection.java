package com.ditisha.inventory.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Creates JDBC connections. Settings come from environment variables so no password is stored in code. */
public final class DBConnection {

    private static final String URL = env("DB_URL",
            "jdbc:mysql://localhost:3306/inventory_db?createDatabaseIfNotExist=true");
    private static final String USER = env("DB_USERNAME", "root");
    private static final String PASSWORD = env("DB_PASSWORD", "change_me");

    private DBConnection() {}

    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /** Creates the products table if it does not exist yet. */
    public static void initSchema() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS products ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(100) NOT NULL, "
                + "category VARCHAR(50), "
                + "price DECIMAL(10,2) NOT NULL, "
                + "quantity INT NOT NULL DEFAULT 0, "
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try (Connection con = getConnection(); Statement st = con.createStatement()) {
            st.execute(sql);
        }
    }
}
