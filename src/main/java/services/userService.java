package services;

import utils.DBConnection;
import models.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;

import utils.DBConnection;

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
        String query = "INSERT INTO user (email, roles, password, first_name, last_name, phone_number, created_at) VALUES (?,?,?,?,?,?,?)";
        pre = con.prepareStatement(query);
        pre.setString(1, user.getEmail());
        String[] roles = {"ROLE_USER"};
        pre.setString(2, Arrays.toString(user.getRoles()));
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        pre.setString(3, hashedPassword);
        pre.setString(4, user.getFirstName());
        pre.setString(5, user.getLastName());
        pre.setInt(6, user.getPhoneNumber());
        pre.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        pre.executeUpdate();
        pre.close();
    }


    @Override
    public boolean updateUser(User user) throws SQLException {
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
}
