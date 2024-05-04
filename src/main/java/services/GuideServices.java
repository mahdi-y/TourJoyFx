package Services;

import Entities.Booking;
import Entities.Guide;
import Entities.feedback;
import utils.MyDB;

import java.sql.*;
import java.time.LocalDate;
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
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = MyDB.getInstance().getConnection();
            if (guide.getCIN() <= 0) {
                System.out.println("Error adding guide: Invalid CIN value.");
                return;
            }
            if (entityExists(guide)) {
                System.out.println("The guide already exists in the database.");
                return;
            }

            conn.setAutoCommit(false); // Start transaction

            String query = "INSERT INTO Guide (CIN, firstname_g, lastname_g, emailaddress_g, phonenumber_g, gender_g, language, dob, price, image_name, country_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = conn.prepareStatement(query);
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
            pst.setInt(11, guide.getCountry());

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                conn.commit(); // Commit the transaction
                System.out.println("Guide added successfully!");
            } else {
                System.out.println("Error adding guide: No rows affected.");
                conn.rollback(); // Rollback the transaction in case of no rows affected
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback on error
                }
            } catch (SQLException ex) {
                System.out.println("Error rolling back transaction: " + ex.getMessage());
            }
            System.out.println("Error adding guide: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for more detailed error information
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset auto-commit to true
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error closing resources: " + ex.getMessage());
            }
        }
    }

    @Override
    public void update(Guide guide) throws SQLException {

    }


    public List<Guide> Read() throws SQLException {
        List<Guide> guides = new ArrayList<>();
        String query = "SELECT CIN, firstname_g, lastname_g, emailaddress_g, phonenumber_g, gender_g, language, dob, price, image_name, country_id FROM Guide " ;

        try (PreparedStatement pst = MyDB.getInstance().getConnection().prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                int cin = rs.getInt("CIN");
                String firstName = rs.getString("firstname_g");
                String lastName = rs.getString("lastname_g");
                String email = rs.getString("emailaddress_g");
                String phoneNumber = rs.getString("phonenumber_g");
                String gender = rs.getString("gender_g");
                String language = rs.getString("language");
                String dob = rs.getString("dob");
                double price = rs.getDouble("price");
                String imagePath = rs.getString("image_name");
                int country_id = rs.getInt("country_id");


                Guide guide = new Guide(cin, firstName, lastName, email, phoneNumber, gender, language, dob, price, imagePath, country_id);
                guides.add(guide);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        String updateGuideQuery = "UPDATE Guide SET CIN=?, firstname_g=?, lastname_g=?, emailaddress_g=?, phonenumber_g=?, gender_g=?, language=?, dob=?, price=?, image_name=?, country_id=? WHERE CIN=?";
        Connection connection = null;
        PreparedStatement pst = null;
        try {
            connection = MyDB.getInstance().getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Assume these methods close the pst internally
            updateRelatedTable(connection, "UPDATE Booking SET guide_id=? WHERE guide_id=?", guide.getCIN(), oldCIN);
            updateRelatedTable(connection, "UPDATE Feedback SET fk_guide_id=? WHERE fk_guide_id=?", guide.getCIN(), oldCIN);

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
            pst.setInt(11, guide.getCountry());
            pst.setInt(12, oldCIN);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                connection.commit(); // Commit transaction if all updates are successful
                System.out.println("Guide updated successfully! Rows affected: " + rowsUpdated);
            } else {
                System.out.println("Error updating guide: No rows affected.");
                connection.rollback(); // Rollback transaction if no rows are updated
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            if (connection != null) {
                connection.rollback(); // Ensure rollback on error
            }
        } finally {
            if (pst != null) pst.close(); // Ensure PreparedStatement is closed
            if (connection != null) {
                connection.setAutoCommit(true); // Reset auto-commit to true
                connection.close();
            }
        }
    }

    private void updateRelatedTable(Connection connection, String query, int newId, int oldId) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, newId);
            pst.setInt(2, oldId);
            pst.executeUpdate();
        }
    }




    public void delete(Guide guide) throws SQLException {
        // Queries to delete bookings, feedback, and the guide itself
        String deleteBookingsQuery = "DELETE FROM Booking WHERE guide_id=?";
        String deleteFeedbackQuery = "DELETE FROM Feedback WHERE fk_guide_id=?";
        String deleteGuideQuery = "DELETE FROM Guide WHERE CIN=?";

        Connection connection = null;
        try {
            connection = MyDB.getInstance().getConnection(); // Open connection
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
            System.out.println("Guide and associated data deleted successfully!");
        } catch (SQLException e) {
            // Roll back transaction if exception occurs
            if (connection != null) {
                connection.rollback();
            }
            // Handle and log exceptions
            System.out.println("Error deleting guide and associated data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Reset default auto-commit behavior
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close(); // Close the connection in the finally block
            }
        }
    }
    public boolean isCINUnique(int cin) throws SQLException {
        // Query the database to check if the CIN already exists
        String query = "SELECT COUNT(*) FROM Guide WHERE CIN = ?";
        try (PreparedStatement pst = MyDB.getInstance().getConnection().prepareStatement(query)) {
            pst.setInt(1, cin);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0; // If count is 0, CIN is unique; otherwise, it's not unique
                }
            }
        }
        return false; // Return false by default in case of any exception or unexpected behavior
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

    public static Guide fetchGuideById(int guideId) {
        String query = "SELECT * FROM Guide WHERE CIN = ?";
        try (Connection conn = MyDB.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, guideId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int CIN = rs.getInt("CIN");
                String firstname_g = rs.getString("firstname_g");
                String lastname_g = rs.getString("lastname_g");
                String emailaddress_g = rs.getString("emailaddress_g");
                String phonenumber_g = rs.getString("phonenumber_g");
                String gender_g = rs.getString("gender_g");
                String language = rs.getString("language");
                String dob = rs.getString("dob");
                double price = rs.getDouble("price");
                String image = rs.getString("image_name");
                int country_id = rs.getInt("country_id");


                return new Guide(CIN, firstname_g, lastname_g, emailaddress_g, phonenumber_g, gender_g, language, dob, price, image, country_id);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Return null if no guide is found or an error occurs
    }
// In your GuideServices class

    public double calculateAverageRate(int guideId) throws SQLException {
        // Retrieve all feedback for the given guideId
        List<feedback> feedbackList = Services.FeedbackServices.getFeedbackByGuideId(guideId); // Assuming you have a FeedbackService class

        // Calculate the total sum of rates
        double totalRate = 0.0;
        for (feedback feedback : feedbackList) {
            totalRate += feedback.getRating();
        }

        // Calculate the average rate
        double averageRate = 0.0;
        if (!feedbackList.isEmpty()) {
            averageRate = totalRate / feedbackList.size();
        }

        return averageRate;
    }

}
