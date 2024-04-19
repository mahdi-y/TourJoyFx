package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.claims;
import services.ServiceClaims;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class RecController {

    @FXML
    private Button button;
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
    private Label description;

    @FXML
    private TextField descriptionT;

    @FXML
    private Label reply;

    @FXML
    private TextField replyT;

    @FXML
    private Label state;

    @FXML
    private TextField stateT;
    @FXML
    private Button deleteButton;


    @FXML
    private Label title;

    @FXML
    private Button updateButton;
    @FXML
    private TableView<claims> claimsTableView;


    @FXML
    private TextField titleT;
    private ServiceClaims ServiceClaims;



    @FXML
    void initialize() {

        ServiceClaims=new ServiceClaims();
        button.setOnAction(event -> {
            addClaims();

        });


    }
    @FXML
    void addClaims() {
        try {

            String title = titleT.getText();
            String description = descriptionT.getText();
            LocalDateTime createDate =LocalDateTime.now();
            String state = stateT.getText();
            String reply = replyT.getText();



            // Create claim object
            claims claims = new claims(title, description, createDate,state,reply);



            // Add claim to database
            ServiceClaims.add(claims);



            showAlert(Alert.AlertType.INFORMATION, "Success", "claim added successfully!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid ID.");
        } catch (SQLException e) {
            e.printStackTrace();  // Print stack trace for detailed error information
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding claim to the database.");
        }
    }

        private void showAlert(Alert.AlertType type, String title, String message) {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.showAndWait();
        }


}