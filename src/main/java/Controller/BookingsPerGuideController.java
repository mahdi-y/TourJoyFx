package Controller;

import Entities.Guide;
import com.example.javafx.HelloApplication;
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
import Entities.Guide;

import Services.BookingServices;
import Services.GuideServices;
import javafx.stage.Stage;
import utils.GMailer;
import Services.userService;
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
            userService userService = new userService();
            GuideServices guideService = new GuideServices();
            Guide guide = GuideServices.fetchGuideById(booking.getGuide());
            // Assuming booking.getUser_id() correctly retrieves the user ID from the booking object
            String userEmail = Services.userService.fetchUserEmailById(booking.getUser_id());
            if (userEmail == null) {
                showAlert(Alert.AlertType.ERROR, "User Not Found", "No user found with ID: " + booking.getUser_id());
                return;
            }

            if (guide == null) {
                showAlert(Alert.AlertType.ERROR, "Guide Not Found", "The guide associated with this booking could not be found.");
                return; // Exit if no guide found
            }

            // If both guide and user are found, proceed to send cancellation email and delete the booking
            String guideName = guide.getFirstname_g() + " " + guide.getLastname_g(); // Formatting guide's name
            String bookingDate = booking.getDate().toString(); // Formatting booking date
            String emailContent = String.format("Your booking with guide %s on %s has been cancelled.", guideName, bookingDate);

            // Send the cancellation email
            new GMailer().sendMail(userEmail, "Booking Cancelled", emailContent);

            // Delete the booking from the database
            bookingServices.delete(booking);

            // Refresh the bookings data displayed
            setBookingsData(bookingServices.getBookingsByGuide(booking.getGuide()));

            // Show confirmation alert
            showAlert(Alert.AlertType.INFORMATION, "Booking Deleted", "The booking has been successfully deleted and cancellation email sent.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not complete the operation: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "General Error", "An unexpected error occurred: " + e.getMessage());
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Stage getPrimaryStage() {
        return HelloApplication.getPrimaryStage();
    }

    public void minimizeWindow(javafx.event.ActionEvent actionEvent) {
        getPrimaryStage().setIconified(true);
    }

    public void expandWindow(javafx.event.ActionEvent actionEvent) {
        Stage stage = getPrimaryStage();
        stage.setMaximized(!stage.isMaximized());
    }

    public void closeWindow(javafx.event.ActionEvent actionEvent) {
        getPrimaryStage().close();
    }

}