package Services;

import Entities.Guide;
import models.User;
import models.claims;
import models.notification;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceClaims  {

    private Connection con;
    private PreparedStatement pre;
    private ResultSet res;



    public ServiceClaims() {
        con = DBConnection.getInstance().getConnection();
    }


    public void registerUser(claims claims) throws SQLException {

    }

    public void updateProfile(claims claims, String currentEmail) throws SQLException {

    }

    public void updateProfileAfetrCompletion(User user, String currentEmail) throws SQLException {

    }

    public void add(claims claims) throws SQLException {
        String query = "INSERT INTO claims (fk_c_id,title, description, create_date, state,  reply, fk_u_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pre = con.prepareStatement(query)) {
            pre.setString(2, claims.getTitle());
            pre.setString(3, claims.getDescription());
            pre.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            pre.setString(5, claims.getState());
            pre.setInt(1, claims.getFkC());
            pre.setString(6, claims.getReply());
            pre.setInt(7, claims.getFkUser());
            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean emailExists(String email) throws SQLException {
        return false;
    }

    public boolean deleteUser(claims claims) throws SQLException {
        return false;
    }

    public void update(claims claims) throws SQLException {

    }

    public List<claims> ReadBackList() throws  SQLException{
        List<claims> claimsList = new ArrayList<>();
        String query = "SELECT * FROM claims";
        try (PreparedStatement pre = con.prepareStatement(query);
             ResultSet res = pre.executeQuery()) {
            while (res.next()) {
                Integer id = res.getObject("id", Integer.class);  // Using getObject to safely handle nulls
                String title = res.getString("title");
                String description = res.getString("description");
                LocalDateTime createDate = res.getTimestamp("create_date") != null ? res.getTimestamp("create_date").toLocalDateTime() : null;
                String state = res.getString("state");
                Integer fkC = res.getObject("fk_c_id", Integer.class); // Safe null handling
                String reply = res.getString("reply");
                claims claim = new claims(id, title, description, createDate, state, fkC, reply);
                claimsList.add(claim);
            }
        } catch (SQLException e) {
            throw new SQLException("Error reading claims from database", e);
        }
        return claimsList;
    }

    public List<claims> Read(int fkUser) throws SQLException {
        List<claims> claimsList = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();
        String query = "SELECT * FROM claims WHERE fk_u_id = ?";
        try (PreparedStatement pre = con.prepareStatement(query)) {
            pre.setInt(1, fkUser);  // Set the fkUser parameter in the query
            try (ResultSet res = pre.executeQuery()) {
                while (res.next()) {
                    Integer id = res.getObject("id", Integer.class);  // Using getObject to safely handle nulls
                    String title = res.getString("title");
                    String description = res.getString("description");
                    LocalDateTime createDate = res.getTimestamp("create_date") != null ? res.getTimestamp("create_date").toLocalDateTime() : null;
                    String state = res.getString("state");
                    Integer fkC = res.getObject("fk_c_id", Integer.class); // Safe null handling
                    String reply = res.getString("reply");
                    claims claim = new claims(id, title, description, createDate, state, fkC, reply, fkUser);
                    claimsList.add(claim);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error reading claims from database", e);
        }
        return claimsList;
    }


    public boolean phoneNumberExists(int phone) throws SQLException {
        return false;
    }

    public List<claims> ReadUser() throws SQLException {
        return List.of();
    }

    public List<User> fetchAllUsers() throws SQLException {
        return List.of();
    }

    public void update(claims claims, Integer fkUser) throws SQLException {
        String query = "UPDATE claims SET fk_c_id=?,title=?, description=?, state=?, reply=?, fk_u_id=? WHERE id=?";
        try (PreparedStatement pre = con.prepareStatement(query)) {
            pre.setString(2, claims.getTitle());
            pre.setString(3, claims.getDescription());
            pre.setString(4, claims.getState());
            pre.setInt(1, claims.getFkC());
            pre.setString(5, claims.getReply());
            pre.setInt(6, claims.getId());
            pre.setInt(7, fkUser);
            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void update(Guide guide, int oldCIN) throws SQLException {

    }
 public void delete(claims claims) throws SQLException {
        String deleteClaimsQuery = "DELETE FROM claims WHERE id=?";
        PreparedStatement deleteClaimsPst = null;
        try {
            deleteClaimsPst = con.prepareStatement(deleteClaimsQuery);
            deleteClaimsPst.setInt(1, claims.getId());
            int affectedRows = deleteClaimsPst.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting claim failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting claim from the database", e);
        } finally {
            if (deleteClaimsPst != null) {
                deleteClaimsPst.close(); // Close only the PreparedStatement
            }
            // Do not close the connection here if it's being reused
        }
    }

    public Map<String, Integer> getCategoryStatistics() throws SQLException {
        Map<String, Integer> stats = new HashMap<>();
        String query = "SELECT c.name AS category, COUNT(*) AS count FROM claims cl JOIN categories c ON cl.fk = c.id GROUP BY c.name";
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getInstance().getConnection(); // Get the connection without auto-closing it
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String category = rs.getString("category");
                int count = rs.getInt("count");
                stats.put(category, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Handle exception appropriately
            throw e;  // Rethrow or handle as needed
        } finally {
            // Close result set and statement here, but not the connection
            if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
            if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
        }
        return stats;
    }




        public void addNotification(String message, Integer fkUser) throws SQLException {
            String sql = "INSERT INTO notification (message, is_read, created_at, user) VALUES (?, ?, ?, ?)";
            try (Connection conn = DBConnection.getInstance().getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, message);
                pstmt.setInt(2, 0);
                pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.setInt(4, fkUser);
                pstmt.executeUpdate();
            }
        }

    public List<notification> getAllNotifications() throws SQLException {
        List<notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notification ORDER BY created_at DESC";
        PreparedStatement pstmt = con.prepareStatement(sql);  // Use the existing connection
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            notifications.add(new notification(
                    rs.getInt("id"),
                    rs.getString("message"),
                    rs.getBoolean("is_read"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getInt("user")
            ));
        }
        rs.close();  // Close ResultSet manually
        pstmt.close();  // Close PreparedStatement manually
        return notifications;
    }

    public void closeConnection() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();  // Close connection when sure it's no longer needed
        }
    }


    }





