package controller;
import Entities.Accomodation;
import Entities.Reservation;
import Services.ServiceAccomodation;
import Services.ServiceResBack;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class AccomodationController {

    @FXML
    private TableView<Accomodation> DisplayAccommodations;
    @FXML
    private TableColumn<Accomodation, Integer> refAColumn;
    @FXML
    private TableColumn<Accomodation, String> nameColumn;
    @FXML
    private TableColumn<Accomodation, String> typeColumn;
    @FXML
    private TableColumn<Accomodation, String> locationColumn;
    @FXML
    private TableColumn<Accomodation, Float> priceColumn;
    @FXML
    private TableColumn<Accomodation, Integer> roomColumn;

    @FXML
    private TextField refAField;
    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private TextField locationField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField roomField;

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
   // @FXML
   // private Button clear1;


    private ServiceAccomodation serviceAccomodation;


    @FXML
    void initialize() {
        serviceAccomodation = new ServiceAccomodation();
        refAColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRefA()).asObject());
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        typeComboBox.getItems().addAll(
                "Villa", "Guesthouse", "Private house", "Apartment", "Farm house"
        );
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        roomColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNb_rooms()).asObject());
        priceColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getPrice()).asObject());
        addButton.setOnAction(event -> addAccomodation());
        deleteButton.setOnAction(event -> deleteAccomodation());
        updateButton.setOnAction(event -> updateAccomodation());
      //  clear1.setOnAction(event -> ClearAcc());

        // Load accommodations into the table
        try {
            List<Accomodation> listAccomodations = serviceAccomodation.Read();
            DisplayAccommodations.getItems().addAll(listAccomodations);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch accommodations from the database.");
        }

        // Add listener to handle row selection
        DisplayAccommodations.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Populate text fields with selected accommodation's data
                refAField.setText(String.valueOf(newSelection.getRefA()));
                nameField.setText(newSelection.getName());
                typeComboBox.setValue(newSelection.getType());
                locationField.setText(newSelection.getLocation());
                priceField.setText(String.valueOf(newSelection.getPrice()));
                roomField.setText(String.valueOf(newSelection.getNb_rooms()));


            }
        });
    }

    @FXML
    void addAccomodation() {
        try {
            int refA = Integer.parseInt(refAField.getText());
            String name = nameField.getText();
            String type = typeComboBox.getValue();
            String location = locationField.getText();
            int nb_rooms= Integer.parseInt(roomField.getText());
            float price= Float.parseFloat(priceField.getText());




            // Validate fields

            if (name.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Name is obligatory.");
                return;
            }
            if(name.length()<6){
                showAlert(Alert.AlertType.ERROR, "Input Error", "Name is too short.");
                return;
            }
            if (type == null || type.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Type is obligatory.");
                return;
            }
            if (location.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Location is obligatory.");
                return;
            }
            if (price <= 0) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Price must be positive.");
                return;
            }
            if (nb_rooms < 0 || nb_rooms > 6) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Number of rooms must be between 0 and 6.");
                return;
            }

            // Create accommodation object
            Accomodation accomodation = new Accomodation(refA, name,  type, location, price, nb_rooms);

            // Add accommodation to database
            serviceAccomodation.add(accomodation);

            // Update TableView
            DisplayAccommodations.getItems().add(accomodation);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Accommodation added successfully!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid ID.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding Accommodation to the database.");
        }
    }

    @FXML
    void deleteAccomodation() {
        Accomodation selectedAccomodation = DisplayAccommodations.getSelectionModel().getSelectedItem();
        if (selectedAccomodation != null) {
            try {
                serviceAccomodation.delete(selectedAccomodation);
                DisplayAccommodations.getItems().remove(selectedAccomodation);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Accommodation deleted successfully!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error deleting accommodation from the database.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select an accommodation to delete.");
        }
    }

    @FXML
    void updateAccomodation() {
        Accomodation selectedAccomodation = DisplayAccommodations.getSelectionModel().getSelectedItem();
        if (selectedAccomodation != null) {
            try {
                int refA = Integer.parseInt(refAField.getText());
                String name = nameField.getText();
                String type = typeComboBox.getValue();
                String location = locationField.getText();
                int nb_rooms= Integer.parseInt(roomField.getText());
                float price= Float.parseFloat(priceField.getText());

                // Validate fields

                if (name.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Name is obligatory.");
                    return;
                }
                if(name.length()<6){
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Name is too short.");
                    return;
                }
                if (type == null || type.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Type is obligatory.");
                    return;
                }
                if (location.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Location is obligatory.");
                    return;
                }
                if (price <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Price must be positive.");
                    return;
                }
                if (nb_rooms < 0 || nb_rooms > 6) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Number of rooms must be between 0 and 6.");
                    return;
                }

                // Update selected accommodation object
                selectedAccomodation.setRefA(refA);
                selectedAccomodation.setName(name);
                selectedAccomodation.setType(type);
                selectedAccomodation.setLocation(location);
                selectedAccomodation.setPrice(price);
                selectedAccomodation.setNb_rooms(nb_rooms);



                // Update accommodation in the database
                serviceAccomodation.update(selectedAccomodation);

                // Refresh TableView
                DisplayAccommodations.refresh();

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Accommodation updated successfully!");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid ID.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating accommodation in the database.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a accommodation to update.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


}








