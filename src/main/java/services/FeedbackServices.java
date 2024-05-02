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
        String query = "INSERT INTO feedback (fk_guide_id, comment, rating) VALUES (?, ?, ?)";
        try (Connection conn = MyDB.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, feedback.getGuide_id());
            pstmt.setString(2, feedback.getComment());
            pstmt.setDouble(3, feedback.getRating());  // This will handle the partial ratings
            int result = pstmt.executeUpdate();
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
        // Include the rating field in the SELECT query
        String query = "SELECT id, guide_id, comment, rating FROM Feedback";
        try (Connection conn = MyDB.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                // Create new feedback objects including the rating
                feedbacks.add(new feedback(
                        rs.getInt("id"),
                        rs.getInt("guide_id"),
                        rs.getString("comment"),
                        rs.getDouble("rating")  // Make sure to fetch the rating from the ResultSet
                ));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
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

    public static List<feedback> getFeedbackByGuideId(int guideId) throws SQLException {
        List<feedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedback WHERE fk_guide_id = ?";
        Connection conn = MyDB.getInstance().getConnection();
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, guideId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    feedback feedback = new feedback();
                    feedback.setId(resultSet.getInt("id"));
                    feedback.setGuide_id(resultSet.getInt("fk_guide_id"));
                    feedback.setRating(resultSet.getInt("rating"));
                    feedback.setComment(resultSet.getString("comment"));
                    // Assuming other fields exist in your Feedback entity
                    feedbackList.add(feedback);
                }
            }
        }
        return feedbackList;
    }
}
