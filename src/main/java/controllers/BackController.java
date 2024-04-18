package controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.claims;
import services.ServiceClaims;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class BackController {

    @FXML
    private Button catB;
    @FXML
    private TableColumn<claims, Integer> idR;
    @FXML
    private TableColumn<claims, String> titleR;
    @FXML
    private TableColumn<claims, String> descriptionR;
    @FXML
    private TableColumn<claims, LocalDateTime> createDateR;
    @FXML
    private TableColumn<claims, String> stateR;
    @FXML
    private TableColumn<claims, String> replyR;

    @FXML
    private Label descriptionMod;

    @FXML
    private TextField descriptionM;

    @FXML
    private Label replyMod;

    @FXML
    private TextField replyM;

    @FXML
    private Label stateMod;

    @FXML
    private TextField stateM;
    @FXML
    private Button deleteButton;


    @FXML
    private Label titleMod;

    @FXML
    private Button updateButton;
    @FXML
    private TableView<claims> claimsTableView;


    @FXML
    private TextField titleM;
    private ServiceClaims ServiceClaims;



    @FXML
    void initialize() {

        ServiceClaims=new ServiceClaims();



        updateButton.setOnAction(event -> updateClaims());
        deleteButton.setOnAction(event -> deleteClaims());
        loadClaimsData();
        setupTableColumns();
        // Add listener to handle row selection
        claimsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Populate fields with selected country's data

                titleM.setText(newSelection.getTitle());
                descriptionM.setText(newSelection.getDescription());
                stateM.setText(newSelection.getState());
                replyM.setText(newSelection.getReply());
            }
        });
    }

    private void setupTableColumns() {
        idR.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        titleR.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        descriptionR.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        createDateR.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCreateDate()));
        stateR.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState()));
        replyR.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReply()));
    }
    private void loadClaimsData() {
        try {
            List<claims> claimsList = ServiceClaims.Read();
            ObservableList<claims> observableClaims = FXCollections.observableArrayList(claimsList);
            claimsTableView.setItems(observableClaims);
        } catch (SQLException e) {
            e.printStackTrace();  // Handle exceptions, log them, and maybe show an error message to the user
        }
    }
    @FXML
    void deleteClaims() {
        claims selectedClaim = claimsTableView.getSelectionModel().getSelectedItem();
        if (selectedClaim != null) {
            try {
                ServiceClaims.delete(selectedClaim);
                claimsTableView.getItems().remove(selectedClaim);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Claim deleted successfully!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error deleting claim from the database.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a claim to delete.");
        }
    }

    @FXML
    void updateClaims() {
        claims selectedClaims = claimsTableView.getSelectionModel().getSelectedItem();
        if (selectedClaims != null) {
            try {
                String title = titleM.getText();
                String description = descriptionM.getText();
                LocalDateTime createDate =LocalDateTime.now();
                String state = stateM.getText();
                String reply = replyM.getText();




                // Update selected country object
                selectedClaims.setTitle(title);
                selectedClaims.setDescription(description);
                selectedClaims.setState(state);
                selectedClaims.setReply(reply);

                // Update country in the database
                ServiceClaims.update(selectedClaims);

                // Refresh TableView
                claimsTableView.refresh();

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Claim updated successfully!");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid ID.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating claim in the database.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a claim to update.");
        }
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleCatV() {
        try {
            // Load the second FXML file
            // Ensure that the FXMLLoader uses the correct class to find the resource relative to its location in the classpath.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tourjoy/add-cat.fxml"));
            // Change 'loader' to 'fxmlLoader' to match the initialized FXMLLoader object.
            Parent root = fxmlLoader.load();

            // Create a new stage or use an existing one
            Stage stage = new Stage();
            stage.setTitle("Add Category");  // Updated title to reflect the function
            stage.setScene(new Scene(root));
            stage.show();

            // Optionally hide the current stage if it's a full screen change
            // ((Stage) openSecondaryViewButton.getScene().getWindow()).hide();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
            // Consider showing an error alert to the user here as well
        }
    }


}
