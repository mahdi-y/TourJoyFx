package Controller;

import Entities.Booking;
import com.example.javafx.HelloApplication;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Services.BookingServices;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import Entities.Guide; // Import statement for the Guide class
import Services.GuideServices;
import Test.GMailer;


public class BookingsBack {

    @FXML
    private TableView<Booking> ListBookings;
    List<Guide> listBookings = new ArrayList<>();
    @FXML
    private Button seebookingsButton;

    @FXML
    private TableColumn<Booking, Integer> guideColumn;

    @FXML
    private TableColumn<Booking, String> dateColumn;

    @FXML
    private Button deletebookingButton;

    private BookingServices bookingServices;

    private TableView<Guide> ListGuides;


    public void initialize() {
        bookingServices = new BookingServices();

        guideColumn.setCellValueFactory(new PropertyValueFactory<>("guide"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));



        try {
            List<Booking> listBookings = BookingServices.Read();
            ListBookings.getItems().addAll(listBookings);
        }  catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch guides from the database.");
        }
    }




    public void deleteBooking() {
        Booking selectedBooking = ListBookings.getSelectionModel().getSelectedItem();
        if (selectedBooking != null) {
            try {
                // Delete the booking from the database
                bookingServices.delete(selectedBooking);

                // Remove the booking from the TableView
                ListBookings.getItems().remove(selectedBooking);

                // Send the cancellation email
                new GMailer().sendMail("eya.benouhiba@esprit.tn", "Booking Cancelled", "Your booking has been cancelled.");

                // Show a success alert
                showAlert(Alert.AlertType.INFORMATION, "Success", "Booking deleted successfully!");
            } catch (SQLException e) {
                // If there's an SQL error, show an error alert
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete the booking.");
                e.printStackTrace();
            } catch (Exception e) {
                // Handle exceptions from the email sending
                showAlert(Alert.AlertType.ERROR, "Email Sending Error", "Failed to send cancellation email.");
                e.printStackTrace();
            }
        } else {
            // If no booking is selected, show a warning
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a booking to delete.");
        }
    }










    @FXML
    void refreshTable() {
        // Clear the items from the TableView
        ListBookings.getItems().clear();

        // Re-populate the TableView with updated data
        try {
            List<Booking> bookings = BookingServices.Read(); // Assuming getAllBookings() returns all bookings from the database
            ListBookings.getItems().addAll(bookings);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch bookings from the database.");
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
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
