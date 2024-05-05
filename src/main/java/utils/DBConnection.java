package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final String URL = "jdbc:mysql://localhost:3306/tourjoydb";
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

    public Connection getConnection() {
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = DriverManager.getConnection(URL, USER, PWD);
                System.out.println("Reconnecting: Connection established !");
            }
        } catch (SQLException e) {
            System.out.println("Failed to re-establish connection: " + e.getMessage());
        }
        return cnx;
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
}
