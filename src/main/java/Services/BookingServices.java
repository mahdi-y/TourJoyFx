package Services;

import Entities.Booking;
import Entities.Guide;
import utils.DBConnection;
import utils.MyDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import Services.GuideServices;
import utils.UserSession;


public class BookingServices {
    private static Connection connection;

    public BookingServices() {
        // Ensure connection is initialized here
        initializeConnection();
    }

    private void initializeConnection() {
        if (connection == null) {
            // Initialize the connection when the BookingServices instance is created
            connection = MyDB.getInstance().getConnection();
        }
    }

    public boolean addBooking(String selectedGuideName, Map<Integer, String> guideMap, LocalDate selectedDate) {
        try {
            initializeConnection(); // Ensure connection is active

            // Retrieve the logged-in user's ID
            int userId = UserSession.getInstance().getId();  // Assuming UserSession holds the current user
            // Get selected guide ID from the map
            int selectedGuideId = guideMap.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(selectedGuideName))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(-1); // Default value if not found

            // Check if the guide is already booked on the selected date
            if (isGuideBooked(selectedGuideId, selectedDate)) {
                // Guide is already booked on the selected date
                return false;
            }

            // Prepare SQL statement to insert booking with user_id
            String query = "INSERT INTO Booking (guide_id, user_id, date) VALUES (?, ?, ?)";

            // Create prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set parameters
            preparedStatement.setInt(1, selectedGuideId);
            preparedStatement.setInt(2, userId);  // Set user_id
            preparedStatement.setDate(3, Date.valueOf(selectedDate));

            // Execute the insert statement
            int rowsAffected = preparedStatement.executeUpdate();

            // Close the prepared statement (not the connection)
            preparedStatement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any errors here
            return false;
        }
    }




    // Check if the guide is already booked on the selected date
    private boolean isGuideBooked(int guideId, LocalDate selectedDate) throws SQLException {
        if (connection == null) {
            // Initialize the connection when the BookingServices instance is created, if it's not already initialized
            connection = MyDB.getInstance().getConnection();
        }
        String query = "SELECT COUNT(*) AS count FROM Booking WHERE guide_id = ? AND date = ?";
        connection = MyDB.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, guideId);
        preparedStatement.setDate(2, Date.valueOf(selectedDate));
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt("count");
        preparedStatement.close();
        return count > 0;
    }


    public static List<Booking> Read() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT id, guide_id, user_id, date, status FROM Booking";
        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int guideId = resultSet.getInt("guide_id");
                int userId = resultSet.getInt("user_id");

                LocalDate date = resultSet.getDate("date").toLocalDate(); // Assuming date is stored as a SQL DATE type
                String status = resultSet.getString("status");
                // Create a Booking object with the retrieved information
                Booking booking = new Booking(id, guideId,userId, date, status);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        // Print retrieved bookings to console
        System.out.println("Retrieved Bookings:");
        for (Booking booking : bookings) {
            System.out.println(booking);
        }

        return bookings;
    }


    public Booking getBookingById(int bookingId) throws SQLException {
        String query = "SELECT id, user_id, guide_id, date, status FROM Booking WHERE id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookingId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("user_id");
                int guideId = resultSet.getInt("guide_id");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                String status = resultSet.getString("status");

                return new Booking(id, guideId, userId, date, status);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            throw e;
        }
        return null;
    }


    public void delete(Booking booking) throws SQLException {
        String query = "DELETE FROM Booking WHERE id = ?";
        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, booking.getId());
            preparedStatement.executeUpdate();
        }
    }

    public Booking getLastBooking() throws SQLException {
        Booking lastBooking = null;
        String query = "SELECT * FROM Booking ORDER BY id DESC LIMIT 1"; // Retrieves the latest booking
        try (Connection conn = MyDB.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                int id = rs.getInt("id");
                int guideId = rs.getInt("guide_id");
                int userId = rs.getInt("user_id");

                LocalDate date = rs.getDate("date").toLocalDate();
                String status = rs.getString("status");

                lastBooking = new Booking(id, guideId, userId, date, status);
            }
        }
        return lastBooking;
    }

    public List<Booking> getBookingsByGuide(int guideId) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT id, guide_id, user_id, date, status FROM Booking WHERE guide_id = ?"; // Corrected to 'Booking' if that's your table name

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = MyDB.getInstance().getConnection();
            if (conn == null) {
                System.out.println("Failed to establish a database connection.");
                return bookings; // Early return with empty list
            }

            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, guideId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int guide = rs.getInt("guide_id");
                int userId = rs.getInt("user_id");

                LocalDate date = rs.getDate("date").toLocalDate(); // Ensure 'date' is of type DATE in the database
                String status = rs.getString("status");

                Booking booking = new Booking(id, guideId, userId, date, status);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        return bookings;
    }
    public void updateBooking(Booking booking) throws SQLException {
        String updateBookingStatusQuery = "UPDATE booking SET status = ? WHERE id = ?";

        Connection connection = null;
        PreparedStatement pst = null;
        try {
            connection = MyDB.getInstance().getConnection();
            connection.setAutoCommit(false); // Start transaction

            pst = connection.prepareStatement(updateBookingStatusQuery);
            pst.setString(1, booking.getStatus());
            pst.setInt(2, booking.getId()); // Assuming you have an ID field in your Booking class
            int rowsUpdated = pst.executeUpdate();

            // Check if any rows were affected
            if (rowsUpdated > 0) {
                System.out.println("Booking status updated successfully!");
                connection.commit(); // Commit transaction if update is successful
            } else {
                System.out.println("Error updating booking status: No rows affected.");
                connection.rollback(); // Rollback transaction if no rows are updated
            }
        } catch (SQLException e) {
            System.out.println("Error updating booking status: " + e.getMessage());
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
    public Guide fetchGuideById(int guideId) throws SQLException {
        String query = "SELECT * FROM Guide WHERE CIN = ?";
        try (Connection conn = MyDB.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, guideId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Guide(
                        rs.getInt("CIN"),
                        rs.getString("firstname_g"),
                        rs.getString("lastname_g"),
                        rs.getString("emailaddress_g"),
                        rs.getString("phonenumber_g"),
                        rs.getString("gender_g"),
                        rs.getString("language"),
                        rs.getString("dob"),
                        rs.getDouble("price"),
                        rs.getString("image_name"),
                        rs.getInt("country_id")
                );
            }
        }
        return null; // Return null if no guide is found
    }


}