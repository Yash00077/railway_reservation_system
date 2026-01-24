package com.railway.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    public static Connection connect(String url, String user, String pass) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {
        }
        return DriverManager.getConnection(url, user, pass);
    }

    public static void initSchema(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS trains (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "source VARCHAR(100) NOT NULL," +
                    "destination VARCHAR(100) NOT NULL," +
                    "departure_time DATETIME NOT NULL," +
                    "arrival_time DATETIME NOT NULL," +
                    "total_seats INT NOT NULL" +
                    ")");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS bookings (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "train_id INT NOT NULL," +
                    "passenger_name VARCHAR(100) NOT NULL," +
                    "seats INT NOT NULL," +
                    "booking_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (train_id) REFERENCES trains(id) ON DELETE CASCADE" +
                    ")");
        }
    }
}
