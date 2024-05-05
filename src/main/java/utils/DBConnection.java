/*
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final String URL = "jdbc:mysql://localhost:3306/db";
    private final String USER = "root";
    private final String PWD = "";

    private Connection cnx;
    private static DBConnection instance;

    private DBConnection() {
        try {

            cnx = DriverManager.getConnection(URL, USER, PWD);
            System.out.println("Connection established !");
        } catch (SQLException e) {
            System.out.println("Error while trying to establish connection : " + e.getMessage());
        }
    }

    public Connection getCnx() {
        return cnx;
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
}*/


package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final String URL = "jdbc:mysql://localhost:3306/db?autoReconnect=true&useSSL=false&serverTimezone=UTC";
    private final String USER = "root";
    private final String PWD = "";

    private Connection cnx;
    private static DBConnection instance;

    private DBConnection() {
        connect(); // Moved connection logic to a separate method for clarity and reuse
        //addShutdownHook(); // Add shutdown hook to close the connection when the JVM shuts down
    }

    // Method to establish or re-establish a connection
    private void connect() {
        try {
            if (cnx != null && !cnx.isClosed()) {
                cnx.close(); // Close the existing connection if open
            }
            cnx = DriverManager.getConnection(URL, USER, PWD);
            System.out.println("Connection successfully established!");
        } catch (SQLException e) {
            System.err.println("Error while trying to establish connection: " + e.getMessage());
        }
    }

    // Method to add a shutdown hook to the Runtime
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (cnx != null) {
                try {
                    cnx.close();
                    System.out.println("Database connection closed successfully.");
                } catch (SQLException e) {
                    System.err.println("Failed to close the database connection: " + e.getMessage());
                }
            }
        }));
    }

    // Public method to get the connection instance
    public Connection getCnx() {
        try {
            // Check if connection is valid or not closed before returning it
            if (cnx == null || cnx.isClosed() || !cnx.isValid(5)) {
                System.out.println("Reconnecting to the database...");
                connect();
            }
        } catch (SQLException e) {
            System.err.println("Failed to validate the connection, attempting to reconnect...");
            connect();
        }
        return cnx;
    }

    // Singleton access method
    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
}
