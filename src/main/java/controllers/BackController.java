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
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Message;
import models.categories;
import models.claims;
import models.notification;
import Services.ServiceClaims;
import utils.DBConnection;
import utils.UserSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class BackController {
    @FXML
    public ImageView chatImageView;
    @FXML
    public Button frontoffice;
    @FXML
    private Button chat;
    public Button ClaimB;
    @FXML
    private Button catB;
    @FXML
    private Button sort;
    @FXML
    private Button statsB;
    @FXML
    private ImageView notificationImageView;

    @FXML
    private VBox notificationPanel; // Initial visibility set in FXML

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
    private TextField search;
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
    ComboBox<String> order;
    @FXML
    ComboBox<String> field;
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

    UserSession session = UserSession.getInstance();


    @FXML
    private TextField titleM;
    private ServiceClaims ServiceClaims;

    @FXML
    private ListView<Message> messageListView;
    @FXML
    private TextField messageInputField;

    @FXML
    private ListView<notification> notificationListView;







    @FXML
    void initialize() {

        ServiceClaims=new ServiceClaims();

        stateBox.getItems().addAll("Not treated", "Treated"); // Optionally, populate ComboBox items if not set in FXML

        updateButton.setOnAction(event -> updateClaims());
        deleteButton.setOnAction(event -> deleteClaims());
        notificationImageView.onMouseClickedProperty();
        chatImageView.onMouseClickedProperty();
        try {
            List<categories> categories = loadCategories(); // This should return a list of Category objects
            catBo.setItems(FXCollections.observableArrayList(categories));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load categories.");
        }
        loadClaimsData();
        setupTableColumns();
        loadNotifications();

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
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                searchData(newValue);
            } else {
                loadClaimsData();
            }
        });

        // Set up the options for the order ComboBox
        order.setItems(FXCollections.observableArrayList("Ascending", "Descending"));

        // Set up the options for the field ComboBox
        field.setItems(FXCollections.observableArrayList(
                "Title", "Description", "Creation Date", "State", "Category", "Reply"));



    }
    @FXML
    private void handleChatClick() {
        // Logic to open the chat interface
        handleChat();
    }


    @FXML
    void handleNotificationClick() {
        toggleNotificationPanel();
    }
    @FXML
    private void toggleNotificationPanel() {
        // Toggle the visibility of the notification panel
        if (notificationPanel.isVisible()) {
            notificationPanel.setVisible(false);
        } else {
            updateNotifications(); // Load or refresh notifications
            notificationPanel.setVisible(true);
        }
    }

    private void updateNotifications() {
        try {
            List<notification> notifications = ServiceClaims.getAllNotifications();
            notificationListView.getItems().setAll(notifications);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void loadNotifications() {
        try {
            List<notification> notifications = ServiceClaims.getAllNotifications();
            notificationListView.getItems().setAll(notifications);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    @FXML
    private void handleSortButtonAction() {
        String selectedField = field.getValue();
        String sortOrder = order.getValue();

        Comparator<claims> comparator;
        switch (selectedField) {
            case "Title":
                comparator = Comparator.comparing(claims::getTitle);
                break;
            case "Description":
                comparator = Comparator.comparing(claims::getDescription);
                break;
            case "Create Date":
                comparator = Comparator.comparing(claims::getCreateDate);
                break;
            case "State":
                comparator = Comparator.comparing(claims::getState);
                break;
            case "Category":
                comparator = Comparator.comparing(claims::getFkC);
                break;
            case "Reply":
                comparator = Comparator.comparing(claims::getReply);
                break;
            default:
                return;
        }

        // Reverse the comparator if descending order is selected
        if ("Descending".equals(sortOrder)) {
            comparator = comparator.reversed();
        }

        FXCollections.sort(claimsTableView.getItems(), comparator);
    }


    private void searchData(String searchTerm) {
        ObservableList<claims> filteredData = FXCollections.observableArrayList();
        Predicate<claims> titlePredicate = claim -> claim.getTitle().toLowerCase().contains(searchTerm.toLowerCase());
        Predicate<claims> descriptionPredicate = claim -> claim.getDescription().toLowerCase().contains(searchTerm.toLowerCase());
        Predicate<claims> statePredicate = claim -> claim.getState().toLowerCase().contains(searchTerm.toLowerCase());
        Predicate<claims> replyPredicate = claim -> claim.getReply().toLowerCase().contains(searchTerm.toLowerCase());
        Predicate<claims> fkCPredicate = claim -> String.valueOf(claim.getFkC()).contains(searchTerm);
        Predicate<claims> createDatePredicate = claim -> claim.getCreateDate().toString().contains(searchTerm);

        try {
            for (claims claim : ServiceClaims.ReadBackList()) {
                if (titlePredicate.or(descriptionPredicate).or(statePredicate).or(replyPredicate).or(fkCPredicate).or(createDatePredicate).test(claim)) {
                    filteredData.add(claim);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        claimsTableView.setItems(filteredData);
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
        idR.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId())); // Handles Integer directly, including nulls
        titleR.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        descriptionR.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        createDateR.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCreateDate()));
        stateR.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState()));
        catView.setCellValueFactory(cellData -> {
            Integer categoryId = cellData.getValue().getFkC();
            String categoryName = categoryId != null ? getCategoryNameById(categoryId) : "Unknown"; // Handle null categoryId
            return new SimpleStringProperty(categoryName);
        });
        replyR.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReply()));
    }

    private void loadClaimsData() {
        try {
            List<claims> claimsList = ServiceClaims.ReadBackList();
            ObservableList<claims> observableClaims = FXCollections.observableArrayList(claimsList);
            claimsTableView.setItems(observableClaims);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading claims data from the database.");
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
                ServiceClaims.update(selectedClaims, session.getId());

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

    @FXML
    private void handleStats() {
        try {
            // Load the second FXML file
            // Ensure that the FXMLLoader uses the correct class to find the resource relative to its location in the classpath.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tourjoy/stats.fxml"));
            // Change 'loader' to 'fxmlLoader' to match the initialized FXMLLoader object.
            Parent root = fxmlLoader.load();

            // Create a new stage or use an existing one
            Stage stage = new Stage();
            stage.setTitle("stats");  // Updated title to reflect the function
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
    private void handleChat() {
        try {
            // Load the second FXML file
            // Ensure that the FXMLLoader uses the correct class to find the resource relative to its location in the classpath.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tourjoy/chatBot.fxml"));
            // Change 'loader' to 'fxmlLoader' to match the initialized FXMLLoader object.
            Parent root = fxmlLoader.load();

            // Create a new stage or use an existing one
            Stage stage = new Stage();
            stage.setTitle("chat");  // Updated title to reflect the function
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
