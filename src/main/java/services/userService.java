package services;

import com.google.gson.Gson;
import utils.DBConnection;
import models.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import utils.userUtils;


import org.mindrot.jbcrypt.BCrypt;

public class userService implements IServices<User> {

    private final Connection con;
    private PreparedStatement pre;
    private ResultSet res;

    public userService(){
        con = DBConnection.getInstance().getConnection();
    }

    @Override
    public void registerUser(User user) {
        if (con == null) {
            System.err.println("Connection is null.");
            return;
        }
        String query = "INSERT INTO user (email, roles, password, phone_number, created_at) VALUES (?,?,?,?,?)";
        try (PreparedStatement pre = con.prepareStatement(query)) {
            pre.setString(1, user.getEmail());
            Gson gson = new Gson();
            String rolesJson = gson.toJson(user.getRoles());
            pre.setString(2, rolesJson);
            String encryptedPassword = encrypt(user.getPassword());
            pre.setString(3, encryptedPassword);

            if (user.getPhoneNumber() != null) {
                pre.setInt(4, user.getPhoneNumber());
            } else {
                pre.setNull(4, Types.INTEGER);
            }

            pre.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            int result = pre.executeUpdate();
            if (result > 0) {
                System.out.println("User registered successfully!");
            } else {
                System.err.println("User was not registered. No rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQL Error: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteUser(User user) throws SQLException {
        if (con == null) {
            throw new SQLException("Connection is null.");
        }
        String query = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement pre = con.prepareStatement(query)) {
            pre.setInt(1, user.getId());
            int result = pre.executeUpdate();
            return result > 0;
        }
    }

    @Override
    public void updateProfile(User user, String currentEmail) throws SQLException {
        if (con == null) {
            throw new SQLException("Connection is null.");
        }
        String query = "UPDATE user SET first_name = ?, last_name = ?, country = ?, profile_picture = ?, modified_at = ? WHERE email = ?";
        PreparedStatement pre = con.prepareStatement(query);
        pre.setString(1, user.getFirstName());
        pre.setString(2, user.getLastName());
        pre.setString(3, user.getCountry());
        pre.setString(4, user.getProfilePicture());
        pre.setTimestamp(5, Timestamp.valueOf(user.getModifiedAt()));
        pre.setString(6, currentEmail);

        int result = pre.executeUpdate();
        if (result == 0) {
            throw new SQLException("Update failed, no rows affected.");
        }
    }
    @Override
    public void updateProfileAfetrCompletion(User user, String currentEmail) throws SQLException {
        if (con == null) {
            throw new SQLException("Connection is null.");
        }
        String query = "UPDATE user SET email = ?, first_name = ?, last_name = ?, country = ?, profile_picture = ?, modified_at = ? WHERE email = ?";
        try (PreparedStatement pre = con.prepareStatement(query)) {
            pre.setString(1, user.getEmail());
            pre.setString(2, user.getFirstName());
            pre.setString(3, user.getLastName());
            pre.setString(4, user.getCountry());
            pre.setString(5, user.getProfilePicture());
            pre.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            pre.setString(7, currentEmail);

            int result = pre.executeUpdate();
            if (result == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
        }
    }
    @Override
    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT count(*) FROM user WHERE email = ?";
        try (PreparedStatement pre = con.prepareStatement(query)) {
            pre.setString(1, email);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // returns true if count is greater than 0
                }
            }
        }
        return false;
    }





    @Override
    public List<User> ReadUser() throws SQLException {
        return null;
    }


    public static User loginUser(String email, String password){
        // Query to fetch the user by email
        String query = "SELECT * FROM user WHERE email = ?";

        try (PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");

                    // Check if the password matches the hashed password in the database
                    if (BCrypt.checkpw(password, storedPassword)) {
                        // If passwords match, extract user information
                        int id = resultSet.getInt("id");
                        String email1 = resultSet.getString("email");
                        String[] roles = userUtils.extractRoles(resultSet);
                        String first_name = resultSet.getString("first_name");
                        String last_name = resultSet.getString("last_name");
                        Integer phone_number = resultSet.getInt("phone_number");
                        if (resultSet.wasNull()) {
                            phone_number = null; // Handle nullability of phone_number
                        }
                        String country = resultSet.getString("country");
                        String profile_picture = resultSet.getString("profile_picture");
                        String google_authenticator_secret = resultSet.getString("google_authenticator_secret");
                        boolean is_verified = resultSet.getBoolean("is_verified");
                        boolean is_banned = resultSet.getBoolean("is_banned");
                        String google_id = resultSet.getString("google_id");

                        return new User(id, email1, roles, storedPassword, first_name, last_name, phone_number, country, profile_picture, google_authenticator_secret, is_verified, is_banned, google_id);
                    }
                }
                return null; // Return null if no user found or passwords do not match
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Consider handling this more gracefully
        }
    }



    public static String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public List<User> fetchAllUsers() throws SQLException {
        List<User> usersList = new ArrayList<>();
        String query = "SELECT id, email, roles, first_name, last_name, phone_number, country, created_at, is_verified, is_banned FROM user";
        Gson gson = new Gson();

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String email = rs.getString("email");
                String rolesJson = rs.getString("roles");
                String[] roles = gson.fromJson(rolesJson, String[].class); // Convert JSON to String[]
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                Integer phoneNumber = rs.getObject("phone_number", Integer.class); // Handle nullability better
                String country = rs.getString("country");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                boolean isVerified = rs.getBoolean("is_verified");
                boolean isBanned = rs.getBoolean("is_banned");

                usersList.add(new User(id, email, roles, firstName, lastName, phoneNumber, country, createdAt, isVerified, isBanned));
            }
        }
        return usersList;
    }



}
