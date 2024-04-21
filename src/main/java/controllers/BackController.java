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

public class BackController {

    public Button ClaimB;
    @FXML
    private Button catB;
    @FXML
    private TableColumn<claims, Integer> idR;
    @FXML
    private TableColumn<claims, String> catView;
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
    private Label catLabel;

    @FXML
    private TextField descriptionM;

    @FXML
    private Label replyMod;

    @FXML
    private TextField replyM;
    @FXML
    private Button btn_workbench;

    @FXML
    private Button btn_workbench1;

    @FXML
    private Button btn_workbench11;

    @FXML
    private Button btn_workbench122;

    @FXML
    private Button btn_workbench2;

    @FXML
    private Button btn_workbench21;

    @FXML
    private Label stateMod;

    @FXML
    ComboBox<String> stateBox;
    @FXML
    private Button deleteButton;
    @FXML
    ComboBox<categories> catBo;

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

        stateBox.getItems().addAll("Not treated", "Treated"); // Optionally, populate ComboBox items if not set in FXML

        updateButton.setOnAction(event -> updateClaims());
        deleteButton.setOnAction(event -> deleteClaims());
        try {
            List<categories> categories = loadCategories(); // This should return a list of Category objects
            catBo.setItems(FXCollections.observableArrayList(categories));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load categories.");
        }
        loadClaimsData();
        setupTableColumns();
        // Add listener to handle row selection
        claimsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Populate fields with selected country's data

                titleM.setText(newSelection.getTitle());
                descriptionM.setText(newSelection.getDescription());
                stateBox.setValue(newSelection.getState());
                // Now set the selected category in the catBo ComboBox
                catBo.setValue(findCategoryById(newSelection.getFkC())); // Assuming your categories constructor can handle this

                replyM.setText(newSelection.getReply());
                titleM.setEditable(false);
                descriptionM.setEditable(false);
                catBo.setDisable(true);
            }
        });
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
    private categories findCategoryById(int categoryId) {
        for (categories cat : catBo.getItems()) {
            if (cat.getId() == categoryId) {
                return cat;
            }
        }
        return null;
    }
    private String getCategoryNameById(Integer id) {
        // Logic to fetch the category name from the database or a cache
        // For demonstration purposes, let's assume we have a map with category names
        String categoryName = ""; // Default to an empty string if category is not found
        try {
            // Here you would have the actual code to query your database
            Connection connection = DBConnection.getInstance().getCnx();
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
        idR.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        titleR.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        descriptionR.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        createDateR.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCreateDate()));
        stateR.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState()));
        // Assuming you have a method getCategoryNameById that returns the category name
        catView.setCellValueFactory(cellData -> {
            Integer categoryId = cellData.getValue().getFkC(); // This is the foreign key ID
            String categoryName = getCategoryNameById(categoryId); // You need to implement this method
            return new SimpleStringProperty(categoryName);
        });

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
    private void clearForm() {
        titleM.setText("");
        descriptionM.setText("");
        stateBox.setValue(null);  // or set to default value if preferred
        catBo.setValue(null);
        replyM.setText("");
    }

    // Use it in the deleteClaims method
    @FXML
    void deleteClaims() {
        claims selectedClaim = claimsTableView.getSelectionModel().getSelectedItem();
        if (selectedClaim != null) {
            try {
                ServiceClaims.delete(selectedClaim);
                claimsTableView.getItems().remove(selectedClaim);
                claimsTableView.getSelectionModel().clearSelection();
                clearForm();  // Clear the form fields after deletion
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

                String state = stateBox.getValue();
                categories selectedCategory = catBo.getValue();  // Get the selected category from ComboBox

                String reply = replyM.getText();

// Validate state
                if (state == null) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please select a state.");
                    return;
                }

                if (reply.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a reply.");
                    return;
                }
                // Validate reply, for example, check if it's not too long
                if (reply.length() < 5 || reply.length() > 200) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "reply must be between 5 and 200 characters.");
                    return;
                }


                // Update selected country object

                selectedClaims.setState(state);
                if (selectedCategory != null) {
                    selectedClaims.setFkC(selectedCategory.getId());  // Assuming there is a setter for fkC in your claims model
                }
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

    @FXML
    private void handleAddV() {
        try {
            // Load the second FXML file
            // Ensure that the FXMLLoader uses the correct class to find the resource relative to its location in the classpath.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tourjoy/add-rec.fxml"));
            // Change 'loader' to 'fxmlLoader' to match the initialized FXMLLoader object.
            Parent root = fxmlLoader.load();

            // Create a new stage or use an existing one
            Stage stage = new Stage();
            stage.setTitle("Add Claim");  // Updated title to reflect the function
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
