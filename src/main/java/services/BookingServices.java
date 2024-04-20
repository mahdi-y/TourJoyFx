package services;

import entities.Booking;
import entities.Guide;
import utils.MyDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import services.GuideServices;


public class BookingServices {
    private static Connection connection;

    public BookingServices() {
        // Initialize the connection when the BookingServices instance is created
        connection = MyDB.getInstance().getConnection();
    }

    public boolean addBooking(String selectedGuideName, Map<Integer, String> guideMap, LocalDate selectedDate) {
        try {
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

            // Prepare SQL statement to insert booking
            String query = "INSERT INTO Booking (guide_id, user_id, date) VALUES (?, NULL, ?)";

            // Create prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set parameters
            preparedStatement.setInt(1, selectedGuideId);
            preparedStatement.setDate(2, Date.valueOf(selectedDate));

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
        String query = "SELECT COUNT(*) AS count FROM Booking WHERE guide_id = ? AND date = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, guideId);
        preparedStatement.setDate(2, Date.valueOf(selectedDate));
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt("count");
        preparedStatement.close();
        return count > 0;
    }

    // Ensure the connection is closed when necessary
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any errors here
        }
    }

    public static List<Booking> Read() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT id, guide_id, date FROM Booking";
        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int guideId = resultSet.getInt("guide_id");
                LocalDate date = resultSet.getDate("date").toLocalDate(); // Assuming date is stored as a SQL DATE type

                // Create a Booking object with the retrieved information
                Booking booking = new Booking(id, guideId, date);
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
                LocalDate date = rs.getDate("date").toLocalDate();
                lastBooking = new Booking(id, guideId, date);
            }
        }
        return lastBooking;
    }
    public List<Booking> getBookingsByGuide(int guideId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM Booking WHERE guide_id = ?";

        try (Connection conn = MyDB.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, guideId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Assuming you have a constructor in your Booking entity that matches the fetched data
                int id = rs.getInt("id");
                LocalDate date = rs.getDate("date").toLocalDate(); // Ensure correct conversion from SQL Date to LocalDate
                int userId = rs.getInt("user_id");  // Assuming there's a user_id field if needed

                Booking booking = new Booking(id, guideId, date);
                bookings.add(booking);
            }
        }
        return bookings;
    }

}






