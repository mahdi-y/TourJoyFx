package Services;

import Entities.Guide;
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
            String rolesString = String.join(",", user.getRoles());
            pre.setString(2, rolesString);
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
    public void add(User user) throws SQLException {

    }

    @Override
    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT count(*) FROM user WHERE email = ?";
        try (PreparedStatement pre = con.prepareStatement(query)) {
            pre.setString(1, email);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    @Override
    public boolean phoneNumberExists(int phone) throws SQLException{
        String query = "SELECT count(*) FROM user WHERE phone_number = ?";
        try(PreparedStatement pre = con.prepareStatement(query)){
            pre.setInt(1,phone);
            try(ResultSet rs = pre.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1) > 0;
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
        String query = "SELECT * FROM user WHERE email = ?";

        try (PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    if (BCrypt.checkpw(password, storedPassword)) {
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
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
//                String[] roles = gson.fromJson(rolesJson, String[].class); // Convert JSON to String[]
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                Integer phoneNumber = rs.getObject("phone_number", Integer.class); // Handle nullability better
                String country = rs.getString("country");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                boolean isVerified = rs.getBoolean("is_verified");
                boolean isBanned = rs.getBoolean("is_banned");

                usersList.add(new User(id, email, firstName, lastName, phoneNumber, country, createdAt, isVerified, isBanned));
            }
        }
        return usersList;
    }

    public boolean isEmailTaken(String email) throws SQLException {
        String query = "SELECT * FROM user WHERE email = ?";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(query);
        preparedStatement.setString(1, email);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            return resultSet.next();
        }
    }



    public User selectByPhoneNumber(String phoneNumber) {
        User utilisateur = null;
        String req = "SELECT * FROM user WHERE phone_number = ?";
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ps.setString(1, phoneNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                utilisateur = new User();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setLastName(rs.getString("last_name"));
                utilisateur.setFirstName(rs.getString("first_name"));
//                utilisateur.setSurnom(rs.getString("surnom"));
//                utilisateur.setSexe(rs.getString("sexe"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setPhoneNumber(rs.getInt("phone_number"));
                /*String roleS = rs.getString("role");
                Role role = Role.valueOf(roleS);*/
//                utilisateur.setRole(role);
                utilisateur.setPassword(rs.getString("password"));
                utilisateur.setProfilePicture(rs.getString("profile_picture"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'utilisateur par numéro de téléphone : " + e.getMessage());
        }
        return utilisateur;
    }

    public User selectByEmail(String email) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = DBConnection.getInstance().getConnection();
            statement = connection.prepareStatement("SELECT * FROM user WHERE email = ?");
            statement.setString(1, email);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("last_name");
                String lastName = resultSet.getString("first_name");
//                String username = resultSet.getString("surnom");
//                String sexe = resultSet.getString("sexe");
                String userEmail = resultSet.getString("email") ;
//                int tel = resultSet.getInt("phone_number");
//                String role = resultSet.getString("role");
                String password = resultSet.getString("password");
                String image_user = resultSet.getString("profile_picture");



                user = new User(id, firstName, lastName ,userEmail, password,image_user);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }

        return user;
    }

    public  User selectById(int idUtilisateur) throws SQLException {
        String req = "SELECT * FROM `user` WHERE id=?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, idUtilisateur);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            User u = new User();
            u.setId(rs.getInt("idUtilisateur"));
            u.setLastName(rs.getString("last_name"));
            u.setFirstName(rs.getString("first_name"));
//            u.setSurnom(rs.getString("surnom"));
//            u.setSexe(rs.getString("sexe"));
            u.setEmail(rs.getString("email"));
            u.setPhoneNumber(rs.getInt("phone_number"));
//            String roleString = rs.getString("role");
//            Role role = Role.valueOf(roleString); // Convertir la chaîne en valeur d'énumération
//            u.setRole(role);
            u.setPassword(rs.getString("password"));
            u.setProfilePicture(rs.getString("profile_picture"));

            return u;
        } else {
            return null; // Retourner null si aucun utilisateur n'est trouvé avec cet ID
        }
    }

    public void update(User utilisateur) throws SQLException {
        String req = "UPDATE user SET last_name=?, first_name=?, email=?, phone_number=?,  password=?, profile_picture=? WHERE id=?";
        PreparedStatement ps = con.prepareStatement(req);

        // Attribution des valeurs aux paramètres de la requête préparée.

        ps.setString(1, utilisateur.getLastName());
        ps.setString(2, utilisateur.getFirstName());
//        ps.setString(3, utilisateur.getSurnom());
//        ps.setString(4, utilisateur.getSexe());
        ps.setString(3, utilisateur.getEmail());
        ps.setInt(4, utilisateur.getPhoneNumber());
        //ps.setString(7, utilisateur.getRole().toString());

        String hashedPassword = null;
        hashedPassword = encrypt(utilisateur.getPassword());
        ps.setString(5,hashedPassword );
        ps.setString(6, utilisateur.getProfilePicture());
        ps.setInt(7, utilisateur.getId());

        // Exécution de la requête de mise à jour.
        int rowsAffected = ps.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("L'utilisateur " + utilisateur.getFirstName() + " a été mis à jour avec succès.");
        } else {
            System.out.println("La mise à jour de  " +  utilisateur.getFirstName() + " a échoué.");
        }
    }

    @Override
    public void update(Guide guide, int oldCIN) throws SQLException {

    }

    @Override
    public void delete(User user) throws SQLException {

    }

    @Override
    public List<User> Read(int fkUser) throws SQLException {
        return List.of();
    }

//    @Override
    public List<User> Read() throws SQLException {
        return List.of();
    }

    public static void updateforgottenpassword(String email, String password) {
        String passwordencrypted = encrypt(password);

        String query = "UPDATE user " +
                "SET password = ? WHERE email = ?";
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(query);
            preparedStatement.setString(1, passwordencrypted);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
            System.out.println("Password updated!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void banUser(User user) throws SQLException {
        String query = "UPDATE user SET is_banned = TRUE WHERE id = ?";
        try (PreparedStatement pre = con.prepareStatement(query)) {
            pre.setInt(1, user.getId());
            pre.executeUpdate();
        }
    }

    public void verifyUser(String email) throws SQLException {
        String query = "SELECT id FROM user WHERE email = ?";
        try (PreparedStatement pre = con.prepareStatement(query)) {
            pre.setString(1, email);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");
                String updateQuery = "UPDATE user SET is_verified = TRUE WHERE id = ?";
                try (PreparedStatement updatePre = con.prepareStatement(updateQuery)) {
                    updatePre.setInt(1, userId);
                    updatePre.executeUpdate();
                }
            }
        }
    }



    public void unBanUser(User user) throws SQLException{
        String query = "UPDATE user SET is_banned = FALSE WHERE id = ?";
        try (PreparedStatement pre = con.prepareStatement(query)) {
            pre.setInt(1, user.getId());
            pre.executeUpdate();
        }
    }

    public void updateUserRoles(int userId, String newRoles) throws SQLException {
        String query = "UPDATE user SET roles = CONCAT(roles, ',ROLE_ADMIN') WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User roles updated successfully.");
            } else {
                System.out.println("No user was updated. Check if the user ID is correct.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            throw e;
        }
    }

}
