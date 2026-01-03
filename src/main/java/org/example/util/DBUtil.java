package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/uniride_db?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "root"; // your MySQL username
    private static final String PASSWORD = "R@adhika110905"; // your MySQL password

    static {
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Initialize tables on startup
            initializeTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get a DB connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Initialize tables if they don't exist
    private static void initializeTables() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            // Users Table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    password VARCHAR(100) NOT NULL,
                    role VARCHAR(50) NOT NULL,
                    vehicle_info VARCHAR(255)
                )
            """);

            // Rides Table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS rides (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    driver_id INT NOT NULL,
                    source VARCHAR(100) NOT NULL,
                    destination VARCHAR(100) NOT NULL,
                    ride_date DATETIME NOT NULL,
                    available_seats INT NOT NULL,
                    fare DOUBLE NOT NULL,
                    FOREIGN KEY (driver_id) REFERENCES users(id) ON DELETE CASCADE
                )
            """);

            // Bookings Table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS bookings (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    ride_id INT NOT NULL,
                    passenger_id INT NOT NULL,
                    seats_booked INT NOT NULL,
                    status VARCHAR(50) NOT NULL,
                    FOREIGN KEY (ride_id) REFERENCES rides(id) ON DELETE CASCADE,
                    FOREIGN KEY (passenger_id) REFERENCES users(id) ON DELETE CASCADE
                )
            """);

            System.out.println("✅ Tables initialized successfully (MySQL DB).");

        } catch (Exception e) {
            System.out.println("❌ Error initializing tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
