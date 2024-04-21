package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import entities.Booking;
import services.BookingServices;

import java.sql.SQLException;
import java.util.List;

public class BookingsPerGuideController {

    private BookingServices bookingServices = new BookingServices();

    @FXML
    private ListView<Booking> bookingsListView;

    @FXML
    private Button deleteBookingButton; // Assuming you have a button in your FXML for deleting

    public void setBookingsData(List<Booking> bookings) {
        bookingsListView.getItems().setAll(bookings);
    }

    @FXML
    private void handleDeleteBooking() {
        Booking selectedBooking = bookingsListView.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a booking to delete.");
            return;
        }

        try {
            bookingServices.delete(selectedBooking);
            bookingsListView.getItems().remove(selectedBooking);
            showAlert(Alert.AlertType.INFORMATION, "Booking Deleted", "The booking has been successfully deleted.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error while deleting the booking: " + e.getMessage());
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
