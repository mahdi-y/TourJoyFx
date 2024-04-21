package services;

import entities.feedback;
import utils.MyDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedbackServices {

    // Add Feedback
    public boolean add(feedback feedback) {
        // Implementation that adds feedback to the database
        try {
            String query = "INSERT INTO feedback (fk_guide_id, comment) VALUES (?, ?)";
            Connection conn = MyDB.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, feedback.getGuide_id());
            pstmt.setString(2, feedback.getComment());
            int result = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Update Feedback
    public void update(int id, String newComment) {
        String query = "UPDATE Feedback SET comment = ? WHERE id = ?";
        try (Connection conn = MyDB.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, newComment);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            // Log error or handle exception
        }
    }

    // Read All Feedback
    public List<feedback> Read() {
        List<feedback> feedbacks = new ArrayList<>();
        String query = "SELECT id, guide_id, comment FROM Feedback";
        try (Connection conn = MyDB.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                feedbacks.add(new feedback(rs.getInt("id"), rs.getInt("guide_id"), rs.getString("comment")));
            }
        } catch (SQLException e) {
            // Log error or handle exception
        }
        return feedbacks;
    }

    // Delete Feedback
    public void delete(int id) {
        String query = "DELETE FROM Feedback WHERE id = ?";
        try (Connection conn = MyDB.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            // Log error or handle exception
        }
    }
}
