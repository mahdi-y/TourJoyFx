package services;

import entities.Booking;
import entities.Guide;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class GuideServices implements IServices<Guide> {

    public boolean entityExists(Guide guide) {
        try {
            String requete = "SELECT * FROM guide WHERE CIN = ?";
            PreparedStatement pst = MyDB.getInstance().getConnection().prepareStatement(requete);
            pst.setInt(1, guide.getCIN());

            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking guide existence: " + e.getMessage());
            return false;
        }
    }

    public void add(Guide guide) {
        try {
            if (guide.getCIN() <= 0) {
                System.out.println("Error adding guide: Invalid CIN value.");
                return;
            }
            if (entityExists(guide)) {
                System.out.println("The guide already exists in the database.");
                return;
            }

            String query = "INSERT INTO Guide (CIN, firstname_g, lastname_g, emailaddress_g, phonenumber_g, gender_g, language, dob, price, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";            PreparedStatement pst = MyDB.getInstance().getConnection().prepareStatement(query);
            pst.setInt(1, guide.getCIN());
            pst.setString(2, guide.getFirstname_g());
            pst.setString(3, guide.getLastname_g());
            pst.setString(4, guide.getEmailaddress_g());
            pst.setString(5, guide.getPhonenumber_g());
            pst.setString(6, guide.getGender_g());
            pst.setString(7, guide.getLanguage());
            pst.setString(8, guide.getDob());
            pst.setDouble(9, guide.getPrice());
            pst.setString(10, guide.getImage());

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Guide added successfully!");
            } else {
                System.out.println("Error adding guide: No rows affected.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding guide: " + e.getMessage());
        }
    }

    public List<Guide> Read() throws SQLException {
        List<Guide> guides = new ArrayList<>();
        String query = "SELECT CIN, firstname_g, lastname_g, emailaddress_g, phonenumber_g, gender_g, language, dob, price, image FROM Guide";
        PreparedStatement pst = MyDB.getInstance().getConnection().prepareStatement(query);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            int cin = rs.getInt("CIN");
            String firstName = rs.getString("firstname_g");
            String lastName = rs.getString("lastname_g");
            String email = rs.getString("emailaddress_g");
            String phoneNumber = rs.getString("phonenumber_g");
            String gender = rs.getString("gender_g");
            String language = rs.getString("language");
            String dob = rs.getString("dob");
            double price = rs.getDouble("price"); // Retrieve price from ResultSet
            String imagePath = rs.getString("image");

            Guide guide = new Guide(cin, firstName, lastName, email, phoneNumber, gender, language, dob, price, imagePath);
            guides.add(guide);
        }
        // Print retrieved bookings to console
        System.out.println("Retrieved Bookings:");
        for (Guide guide : guides) {
            System.out.println(guide);
        }
        return guides;
    }

    @Override
    public void update(Guide guide, int oldCIN) throws SQLException {
        // Queries to update related tables and the guide itself
        String updateGuideQuery = "UPDATE Guide SET CIN=?, firstname_g=?, lastname_g=?, emailaddress_g=?, phonenumber_g=?, gender_g=?, language=?, dob=?, price=?, image=? WHERE CIN=?";
        String updateBookingQuery = "UPDATE Booking SET guide_id=? WHERE guide_id=?";
        String updateFeedbackQuery = "UPDATE Feedback SET fk_guide_id=? WHERE fk_guide_id=?";

        Connection connection = null;
        PreparedStatement pst = null;
        try {
            connection = MyDB.getInstance().getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Update related bookings
            pst = connection.prepareStatement(updateBookingQuery);
            pst.setInt(1, guide.getCIN());
            pst.setInt(2, oldCIN);
            pst.executeUpdate();
            pst.close(); // Close the previous statement

            // Update related feedbacks
            pst = connection.prepareStatement(updateFeedbackQuery);
            pst.setInt(1, guide.getCIN());
            pst.setInt(2, oldCIN);
            pst.executeUpdate();
            pst.close(); // Close the previous statement

            // Update the guide
            pst = connection.prepareStatement(updateGuideQuery);
            pst.setInt(1, guide.getCIN());
            pst.setString(2, guide.getFirstname_g());
            pst.setString(3, guide.getLastname_g());
            pst.setString(4, guide.getEmailaddress_g());
            pst.setString(5, guide.getPhonenumber_g());
            pst.setString(6, guide.getGender_g());
            pst.setString(7, guide.getLanguage());
            pst.setString(8, guide.getDob());
            pst.setDouble(9, guide.getPrice());
            pst.setString(10, guide.getImage());
            pst.setInt(11, oldCIN);
            int rowsUpdated = pst.executeUpdate();

            // Check if any rows were affected
            if (rowsUpdated > 0) {
                System.out.println("Guide updated successfully!");
                connection.commit(); // Commit transaction if all updates are successful
            } else {
                System.out.println("Error updating guide: No rows affected.");
                connection.rollback(); // Rollback transaction if no rows are updated
            }
        } catch (SQLException e) {
            System.out.println("Error updating guide: " + e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback(); // Ensure rollback on error
                } catch (SQLException ex) {
                    System.out.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
        } finally {
            if (pst != null) {
                pst.close(); // Ensure PreparedStatement is closed
            }
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Reset auto-commit to true
                } catch (SQLException e) {
                    System.out.println("Error setting auto-commit to true: " + e.getMessage());
                }
            }
        }
    }



    public void delete(Guide guide) throws SQLException {
        // Queries to delete bookings, feedback, and the guide itself
        String deleteBookingsQuery = "DELETE FROM Booking WHERE guide_id=?";
        String deleteFeedbackQuery = "DELETE FROM Feedback WHERE fk_guide_id=?";
        String deleteGuideQuery = "DELETE FROM Guide WHERE CIN=?";

        Connection connection = MyDB.getInstance().getConnection(); // Open connection
        try {
            // Start a transaction
            connection.setAutoCommit(false);

            // Delete bookings associated with the guide
            try (PreparedStatement deleteBookingsPst = connection.prepareStatement(deleteBookingsQuery)) {
                deleteBookingsPst.setInt(1, guide.getCIN());
                deleteBookingsPst.executeUpdate();
            }

            // Delete feedback associated with the guide
            try (PreparedStatement deleteFeedbackPst = connection.prepareStatement(deleteFeedbackQuery)) {
                deleteFeedbackPst.setInt(1, guide.getCIN());
                deleteFeedbackPst.executeUpdate();
            }

            // Delete the guide
            try (PreparedStatement deleteGuidePst = connection.prepareStatement(deleteGuideQuery)) {
                deleteGuidePst.setInt(1, guide.getCIN());
                deleteGuidePst.executeUpdate();
            }

            // Commit transaction
            connection.commit();
        } catch (SQLException e) {
            // Roll back transaction if exception occurs
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error rolling back transaction");
                    ex.printStackTrace();
                }
            }
            // Handle and rethrow or log exceptions
            System.out.println("Error deleting guide and associated data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Reset default auto-commit behavior
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("Error resetting auto-commit: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }


    public double getGuidePrice(int guideId) throws SQLException {
        double price = 0;
        String query = "SELECT price FROM Guide WHERE CIN = ?"; // Assuming the price field exists
        try (Connection conn = MyDB.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, guideId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    price = rs.getDouble("price");
                }
            }
        }
        return price;
    }




}
