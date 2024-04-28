package controllers;

import com.example.tourjoy.HelloApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.User;
import services.userService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class usersList {
    public Button deleteButton;
    public Button frontoffice;
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> idColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
//    @FXML
//    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, Integer> phoneNumberColumn;
    @FXML
    private TableColumn<User, String> countryColumn;
    @FXML
    private TableColumn<User, String> creationDateColumn;
    @FXML
    private TableColumn<User, Boolean> isVerifiedColumn;
    @FXML
    private TableColumn<User, Boolean> isBannedColumn;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private services.userService userService;

    @FXML
    public void initialize() {
        userService = new userService();
        setupTableColumns();
        loadUsers();

        deleteButton.setOnAction(event -> {
            User selectedUser = usersTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                try {
                    boolean deleted = userService.deleteUser(selectedUser);
                    if (deleted) {
                        showAlert(Alert.AlertType.INFORMATION, "User Deleted", "User successfully deleted.");
                        usersTable.getItems().remove(selectedUser);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Delete Failed", "Failed to delete user.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Delete Error", "An error occurred while deleting user.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "No User Selected", "Please select a user to delete.");
            }
        });
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
//        roleColumn.setCellValueFactory(cellData ->
//                new SimpleStringProperty(String.join(", ", cellData.getValue().getRoles())));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        creationDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCreatedAt().format(formatter)));
        isVerifiedColumn.setCellValueFactory(new PropertyValueFactory<>("isVerified"));
        isBannedColumn.setCellValueFactory(new PropertyValueFactory<>("isBanned"));
    }

    private void loadUsers() {
        try {
            List<User> users = userService.fetchAllUsers(); // Fetch all users
            ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
            usersTable.setItems(observableUsers);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Data Load Error", "Could not load user data from the database.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
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


    public void front(ActionEvent actionEvent) throws IOException {
        HelloApplication.loadFXML("/Home.fxml");
    }
}