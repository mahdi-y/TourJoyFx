package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.User;
import services.userService;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class registrationController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private Button register;
    private userService userService;



    @FXML
    public void initialize() {
        userService = new userService(); // Initialize userService
        register.setOnAction(event -> registerUser());
    }






    @FXML
    void registerUser()  {
        try {
        String email = emailField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        int phoneNumber = Integer.parseInt(phoneNumberField.getText()); // Assuming phone number is an int
        LocalDateTime createdAt = LocalDateTime.now();

        User user = new User(email, password, firstName, lastName, phoneNumber, createdAt);

        userService.registerUser(user);
            System.out.println("User registered successfully!"); // Add this line
            showAlert(Alert.AlertType.INFORMATION, "Success", "user added successfully!");
    } catch (SQLException e) {
        showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding user to the database.");
    }



//        close registration menu
        Stage stage = (Stage) register.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
