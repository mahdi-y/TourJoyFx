package Controller;

import Entities.Guide;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import Entities.Booking;
import Services.BookingServices;
import Services.GuideServices;
import utils.GMailer;

import java.sql.SQLException;
import java.util.List;

public class BookingsPerGuideController {

    private BookingServices bookingServices = new BookingServices();

    @FXML
    private VBox bookingsVBox;

    public void setBookingsData(List<Booking> bookings) {
        bookingsVBox.getChildren().clear();
        for (Booking booking : bookings) {
            HBox bookingBox = new HBox(10);
            bookingBox.setPadding(new Insets(5, 10, 5, 10));
            bookingBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
            bookingBox.setStyle("-fx-background-color: lightgray; -fx-background-radius: 5;");

            Label bookingInfo = new Label("Date: " + booking.getDate() + " - Status: " + booking.getStatus());
            bookingInfo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            deleteButton.setOnAction(event -> handleDeleteBooking(booking));

            bookingBox.getChildren().addAll(bookingInfo, new Region(), deleteButton); // Region pushes the button to the right
            bookingsVBox.getChildren().add(bookingBox);
        }
    }

    private void handleDeleteBooking(Booking booking) {
        try {
            // Fetch the guide details using the guide ID from the booking
            Guide guide = GuideServices.fetchGuideById(booking.getGuide());

            if (guide == null) {
                showAlert(Alert.AlertType.ERROR, "Guide Not Found", "The guide associated with this booking could not be found.");
                return;
            }

            // Create a formatted name string using the first and last name of the guide
            String guideName = guide.getFirstname_g() + " " + guide.getLastname_g();  // Assuming you have getter methods or direct field access

            String bookingDate = booking.getDate().toString(); // Assuming getDate() returns a LocalDate or similar

            // Compose the email message to include guide details
            String emailContent = String.format("Your booking with guide %s on %s has been cancelled.", guideName, bookingDate);

            // Send the cancellation email
            new GMailer().sendMail("eya.benouhiba@esprit.tn", "Booking Cancelled", emailContent);

            // Proceed with deleting the booking from the database
            bookingServices.delete(booking);

            // Update the UI by reloading the booking data for the guide
            setBookingsData(bookingServices.getBookingsByGuide(booking.getGuide()));

            // Show confirmation alert
            showAlert(Alert.AlertType.INFORMATION, "Booking Deleted", "The booking has been successfully deleted and cancellation email sent.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not delete booking: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "General Error", "An unexpected error occurred while deleting the booking: " + e.getMessage());
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}