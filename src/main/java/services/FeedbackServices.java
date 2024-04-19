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
    public void add(feedback feedback) {
        String query = "INSERT INTO Feedback (fk_guide_id, comment) VALUES (?, ?)";
        try (Connection conn = MyDB.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, feedback.getGuide_id());
            pst.setString(2, feedback.getComment());
            int result = pst.executeUpdate();
            if (result > 0) {
                System.out.println("Feedback added successfully");
            } else {
                System.out.println("No rows affected");
            }
        } catch (SQLException e) {
            System.out.println("Error adding feedback: " + e.getMessage());
            e.printStackTrace(); // This will provide a stack trace which can be very helpful
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
