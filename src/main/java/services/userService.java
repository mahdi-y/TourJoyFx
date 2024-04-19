package services;

import utils.DBConnection;
import models.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class userService implements IServices<User> {

    private Connection con;
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
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        pre.setString(3, hashedPassword);

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
}
