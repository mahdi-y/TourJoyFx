package services;

import models.Message;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageService {
    private Connection conn; // Maintain connection as a member variable

    public MessageService() throws SQLException {
        // Initialize connection once
        conn = DBConnection.getInstance().getCnx();
    }

    public void saveMessage(Message message) throws SQLException {
        String sql = "INSERT INTO Message (text, timestamp, sender, role) VALUES (?, ?, ?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, message.getText());
        pstmt.setTimestamp(2, Timestamp.valueOf(message.getTimestamp()));
        pstmt.setString(3, message.getSender());
        pstmt.setString(4, message.getRole());
        int affectedRows = pstmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating message failed, no rows affected.");
        }

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
}
