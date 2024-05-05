package controllers;

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
import models.Message;
import models.categories;
import models.claims;
import services.ServiceClaims;
import utils.DBConnection;
import utils.UserSession;


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

    private ObservableList<claims> observableClaims = FXCollections.observableArrayList();

    @FXML
    private TableColumn<claims, Integer> idR1;
    @FXML
    private TableColumn<claims, String> titleR1;
    @FXML
    private TableColumn<claims, String> descriptionR1;
    @FXML
    private TableColumn<claims, LocalDateTime> createDateR1;
    @FXML
    private TableColumn<claims, String> catView1;
    @FXML
    private TableColumn<claims, String> stateR1;
    @FXML
    private TableColumn<claims, String> replyR1;
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
    @FXML
    private TableView<claims> claimsTableView1;
    private ServiceClaims ServiceClaims;
    private int selectedClaimId;
    UserSession session = UserSession.getInstance();
    @FXML
    void initialize() {
        ServiceClaims = new ServiceClaims();
        button.setOnAction(event -> addClaims());
        claimsTableView1.setItems(observableClaims);  // Bind the TableView to the observable list

        System.out.println(session.getFirstname() + session.getId());
        loadClaimsData();
        setupTableColumns();
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
        Connection con = DBConnection.getInstance().getConnection();
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
            Integer fkUser = session.getId();
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
            claims claims = new claims(title, description, createDate, state, fkC, reply, fkUser);

            // Add claim to database
            ServiceClaims.add(claims);
            observableClaims.add(claims);
            ServiceClaims.addNotification("A new claim has been submitted", fkUser); // Add notification

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
    private String getCategoryNameById(Integer id) {
        // Logic to fetch the category name from the database or a cache
        // For demonstration purposes, let's assume we have a map with category names
        String categoryName = ""; // Default to an empty string if category is not found
        try {
            // Here you would have the actual code to query your database
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT name FROM categories WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        categoryName = resultSet.getString("name");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryName;
    }
    private void setupTableColumns() {
        titleR1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        descriptionR1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        createDateR1.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCreateDate()));
        stateR1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState()));
        // Assuming you have a method getCategoryNameById that returns the category name
        catView1.setCellValueFactory(cellData -> {
            Integer categoryId = cellData.getValue().getFkC(); // This is the foreign key ID
            String categoryName = getCategoryNameById(categoryId); // You need to implement this method
            return new SimpleStringProperty(categoryName);
        });

        replyR1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReply()));
    }
    private void loadClaimsData() {

        try {
            List<claims> claimsList = ServiceClaims.Read(session.getId());
            observableClaims.setAll(claimsList);  // Refresh the observable list
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
