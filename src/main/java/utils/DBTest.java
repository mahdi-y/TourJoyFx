package utils;

import java.sql.Connection;
import java.sql.SQLException;

public class DBTest {

    public static void main(String[] args) {
        // Get the DBConnection instance
        DBConnection dbConnection = DBConnection.getInstance();

        // Get the connection
        Connection connection = dbConnection.getCnx();

        // Check if the connection is not null
        if (connection != null) {
            System.out.println("Connection to database successful!");
            try {
                // Close the connection
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        } else {
            System.err.println("Failed to connect to database.");
        }
    }
}
