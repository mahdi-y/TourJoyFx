package controller;

import Entities.Accomodation;
import Entities.Reservation;
import Services.ServiceReservation;
import Services.ServiceAccomodation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.xml.namespace.QName;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Date;
import java.util.Map;
import java.sql.Date;
import java.sql.SQLException;

public class ResController {

    @FXML
    private TextField idRField;
    @FXML
    private DatePicker startPicker;
    @FXML
    private DatePicker endPicker;
    @FXML
    private ComboBox<String> NameComboBox;
    @FXML
    private Button bookButton;

    private ServiceReservation serviceReservation;
    private ServiceAccomodation serviceAccomodation;
    private Map<String, Integer> accommodationNameIdMap;

    @FXML
    public void initialize() throws SQLException {
        serviceReservation = new ServiceReservation();
        serviceAccomodation = new ServiceAccomodation();
        loadAccommodations();
        bookButton.setOnAction(event -> BookAccommodation());
    }

    private void loadAccommodations() throws SQLException {
        accommodationNameIdMap = serviceAccomodation.getAccommodationNameIdMap();
        NameComboBox.getItems().addAll(accommodationNameIdMap.keySet());
    }



    @FXML
    void BookAccommodation() {
        try {
            int idR = Integer.parseInt(idRField.getText());

            // Get start and end dates from DatePickers
            Date start_date = Date.valueOf(startPicker.getValue());
            Date end_date = Date.valueOf(endPicker.getValue());

            // Get the name of the selected accommodation
            String selectedAccommodationName = NameComboBox.getValue();
            Integer name = accommodationNameIdMap.get(selectedAccommodationName);
            //Validate Fields
            if (name == null) {
                showAlert(Alert.AlertType.ERROR, "Invalid Selection", "Please select a valid accommodation.");
                return;
            }
            if (start_date == null || end_date == null) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please select both start and end dates.");
                return;
            }
            Date today = new Date(System.currentTimeMillis());
            if (start_date.before(today)) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Check-in must be after today.");
                return;
            }
            if (end_date.before(start_date)) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Check-out must be after Check-in.");
                return;
            }
            // Create reservation object with only the name
            Reservation reservation = new Reservation(idR, start_date, end_date, name);

            // Add reservation to database
            serviceReservation.add(reservation);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Reservation added successfully!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid ID.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding reservation to the database.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
