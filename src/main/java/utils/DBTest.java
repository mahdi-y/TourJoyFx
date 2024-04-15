package utils;

import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;

public class DBTest {

    public static void main(String[] args) {
        // Get the DBConnection instance
        DBConnection dbConnection = DBConnection.getInstance();

        // Get the connection
        Connection connection = dbConnection.getConnection();

        // Check if the connection is not null
        if (connection != null) {
            System.out.println("Connection to database successful!");
            try {
                // Create a test user with null columns
                User testUser = new User();
                testUser.setEmail("test@example.com");
                testUser.setPassword("password");

                // Register the test user
                registerUser(connection, testUser);

                // Close the connection
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.err.println("Error executing query: " + e.getMessage());
            }
        } else {
            System.err.println("Failed to connect to database.");
        }
    }

    public static void registerUser(Connection connection, User user) throws SQLException {
        String query = "INSERT INTO user (email, roles, password, first_Name, last_Name, phone_number, created_at) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement pre = connection.prepareStatement(query);

        pre.setString(1, user.getEmail());
        pre.setString(2, user.getRoles() != null ? Arrays.toString(user.getRoles()) : "ROLE_USER");
        pre.setString(3, user.getPassword());
        pre.setString(4, user.getFirstName());
        pre.setString(5, user.getLastName());
        int phoneNumber = user.getPhoneNumber() != null ? user.getPhoneNumber() : 0;
        pre.setInt(6, phoneNumber);

        pre.setTimestamp(7, Timestamp.valueOf(user.getCreatedAt() != null ? user.getCreatedAt() : LocalDateTime.now()));

        pre.executeUpdate();
        pre.close();
    }

}
