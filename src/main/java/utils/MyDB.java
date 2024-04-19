package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDB {
    private final String URL = "jdbc:mysql://localhost:3306/tourjoydb";
    private final String USER = "root";
    private final String PWD = "";
    private final int TIMEOUT = 5; // Timeout in seconds

    private Connection cnx;
    private static MyDB instance;

    private MyDB() {
        connect(); // Attempt to establish a connection when instance is created
    }

    // Method to establish a database connection
    private void connect() {
        try {
            cnx = DriverManager.getConnection(URL, USER, PWD);
            System.out.println("Connexion établie !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'établissement de la connexion : " + e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            // Check if connection is closed or invalid, then reconnect
            if (cnx == null || cnx.isClosed() || !cnx.isValid(TIMEOUT)) {
                System.out.println("Reconnecting to database...");
                connect();
            }
        } catch (SQLException e) {
            System.out.println("Error checking connection status: " + e.getMessage());
        }
        return cnx;
    }

    public static synchronized MyDB getInstance() {
        if (instance == null) {
            instance = new MyDB();
        }
        return instance;
    }
}
