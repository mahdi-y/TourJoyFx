package services;

import utils.DBConnection;
import models.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
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
    public void registerUser(User user) throws SQLException {
        if (con == null) {
            throw new SQLException("Connection is null.");
        }
        String query = "INSERT INTO user (email, roles, password, phone_number, created_at) VALUES (?,?,?,?,?)";
        pre = con.prepareStatement(query);
        pre.setString(1, user.getEmail());
        String[] roles = {"ROLE_USER"}; // Normally, this should be taken from `user.getRoles()`
        pre.setString(2, Arrays.toString(roles));
        String encryptedPassword = encrypt(user.getPassword());
        pre.setString(3, encryptedPassword);

        // Handle potentially null phoneNumber
        if (user.getPhoneNumber() != null) {
            pre.setInt(4, user.getPhoneNumber());
        } else {
            pre.setNull(4, java.sql.Types.INTEGER); // Set null if phoneNumber is null
        }

        pre.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        pre.executeUpdate();
        pre.close();
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
    public boolean deleteUser(User user) throws SQLException {
        return false;
    }

    @Override
    public List<User> ReadUser() throws SQLException {
        return null;
    }

    @Override
    public List<User> selectAll() throws SQLException {
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


}
