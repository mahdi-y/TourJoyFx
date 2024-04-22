package controller;

import Entities.Accomodation;
import Entities.Reservation;
import Services.ServiceAccomodation;
import Services.ServiceResBack;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ResBackController {


    @FXML
    private TableView<Reservation> DisplayReservations;
    @FXML
    private TableColumn<Reservation, Integer> idRColumn;
    @FXML
    private TableColumn<Reservation, String> nameRColumn;
    @FXML
    private TableColumn<Reservation, Date> startColumn;
    @FXML
    private TableColumn<Reservation, Date> endColumn;
    @FXML
    private Button deleteRButton;

    private ServiceResBack serviceResBack;



    @FXML
    void initialize() {
        serviceResBack = new ServiceResBack();
        idRColumn.setCellValueFactory(new PropertyValueFactory<>("idR"));
        nameRColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end_date"));


        deleteRButton.setOnAction(event -> deleteRes());

        // Load reservations into the table
        try {
            List<Reservation> listReservations = serviceResBack.Read();
            DisplayReservations.getItems().addAll(listReservations);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch reservations from the database.");
        }

    }



    @FXML
    void deleteRes() {
        Reservation selectedReservation = DisplayReservations.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            try {
                serviceResBack.delete(selectedReservation);
                DisplayReservations.getItems().remove(selectedReservation);
                showAlert(Alert.AlertType.INFORMATION, "Success", "The booking is deleted successfully!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error deleting reservation from the database.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a reservation to delete.");
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
