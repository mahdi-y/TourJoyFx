package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Message;
import models.categories;
import models.claims;
import services.ServiceClaims;

import utils.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RecController {

    @FXML
    private ListView<Message> messageListView;
    @FXML
    private TextField messageInputField;
    @FXML
    private Button sendButton;
    @FXML
    private Button chat;
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
    private TableColumn<claims, String> catView;

    @FXML
    private Label description;
    @FXML
    private TextField descriptionT;
    @FXML
    ComboBox<categories> catBox;
    @FXML
    private Label title;
    @FXML
    private TextField titleT;
    private ServiceClaims ServiceClaims;
    private int selectedClaimId;
    @FXML
    void initialize() {
        ServiceClaims = new ServiceClaims();
        button.setOnAction(event -> addClaims());
        try {
            List<categories> categories = loadCategories(); // This should return a list of Category objects
            catBox.setItems(FXCollections.observableArrayList(categories));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load categories.");
        }
    }

    private List<categories> loadCategories() throws SQLException {
        List<categories> categories = new ArrayList<>();
        Connection con = DBConnection.getInstance().getCnx();
        String query = "SELECT id, name FROM categories"; // Adjusted for a hypothetical table structure
        try (PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                categories.add(new categories(rs.getInt("id"), rs.getString("name")));
            }
        }
        return categories;
    }

    @FXML
    void addClaims() {
        try {
            String title = titleT.getText();
            String description = descriptionT.getText();
            LocalDateTime createDate = LocalDateTime.now();
            String state = "Not treated";  // Default state
            Integer fkC = (catBox.getSelectionModel().getSelectedItem() != null) ? catBox.getSelectionModel().getSelectedItem().getId() : null;
            String reply = "-";  // Default reply
// Validate inputs
            if (title.isEmpty() || description.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Title and description cannot be empty.");
                return;
            }

            if (title.length() < 3 || title.length() > 50) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "the title must be between 3 and 50 characters.");
                return;
            }

            if (description.length() < 5 || description.length() > 300) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Description must be between 5 and 300 characters.");
                return;
            }

            if (fkC == null) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please select a category.");
                return;
            }

            // Create claim object
            claims claims = new claims(title, description, createDate, state, fkC, reply);

            // Add claim to database
            ServiceClaims.add(claims);
            ServiceClaims.addNotification("A new claim has been submitted"); // Add notification

            clearForm();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Claim added successfully!");
            //processNewClaim();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid ID.");
        } catch (SQLException e) {
            e.printStackTrace();  // Print stack trace for detailed error information
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding claim to the database.");
        }

    }

    /*private void processNewClaim() {
        try {
            MailUtil.sendEmail("Tourjoy-help@outlook.com");
            System.out.println("Notification email sent.");
        } catch (MessagingException e) {
            System.out.println("Could not send email.");
            e.printStackTrace();
        }
    }*/

    private void clearForm() {
        titleT.setText("");
        descriptionT.setText("");

        catBox.setValue(null);

    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleChat() {
        try {
            // Load the second FXML file
            // Ensure that the FXMLLoader uses the correct class to find the resource relative to its location in the classpath.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tourjoy/chatClient.fxml"));
            // Change 'loader' to 'fxmlLoader' to match the initialized FXMLLoader object.
            Parent root = fxmlLoader.load();

            // Create a new stage or use an existing one
            Stage stage = new Stage();
            stage.setTitle("client chat");  // Updated title to reflect the function
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
