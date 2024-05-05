package Services;

import models.Message;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageService {
    private Connection conn; // Maintain connection as a member variable

    public MessageService() throws SQLException {
        // Initialize connection once
        conn = DBConnection.getInstance().getConnection();
    }

    public void saveMessage(Message message, int fkUser) throws SQLException {
       /* if (!checkUserExists(fkUser)) {
            throw new SQLException("No such user exists with ID: " + message.getFkUser());
        }*/
        String sql = "INSERT INTO Message (text, timestamp, sender, role, fkUser) VALUES (?, ?, ?,?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, message.getText());
        pstmt.setTimestamp(2, Timestamp.valueOf(message.getTimestamp()));
        pstmt.setString(3, message.getSender());
        pstmt.setString(4, message.getRole());
        pstmt.setInt(5, fkUser);
        pstmt.executeUpdate();

        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                message.setId(generatedKeys.getInt(1));

            } else {
                throw new SQLException("Creating message failed, no ID obtained.");
            }
        }
        pstmt.close();  // Ensure the statement is closed after use
    }


    public List<Message> getAllMessages() throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message ORDER BY timestamp ASC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                messages.add(new Message(
                        rs.getString("text"),
                        rs.getTimestamp("timestamp").toLocalDateTime(),
                        rs.getString("sender"),
                        rs.getString("role")
                ));
            }
        }
        return messages;
    }


    public void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close(); // Close connection when it's no longer needed
        }
    }
    public boolean checkUserExists(int userId) throws SQLException {
        String checkUserSql = "SELECT COUNT(*) FROM user WHERE id = ?";
        try (PreparedStatement checkUserStmt = conn.prepareStatement(checkUserSql)) {
            checkUserStmt.setInt(1, userId);
            ResultSet rs = checkUserStmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }
}
