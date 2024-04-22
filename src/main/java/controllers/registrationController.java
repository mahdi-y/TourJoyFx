package controllers;

import com.example.tourjoy.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.User;
import services.userService;
import utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class registrationController {

    public PasswordField confirmPasswordField;
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private Button register;
    private userService userService;


    @FXML
    public void initialize() {
        userService = new userService();
        register.setOnAction(event -> {
            try {
                registerUser();
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }


    @FXML
    void registerUser() throws IOException, SQLException {
        if (!validateInputs()) {
            return;
        }
        String email = emailField.getText();
        String password = passwordField.getText();
        int phoneNumber = Integer.parseInt(phoneNumberField.getText());
        String confirmPassword = confirmPasswordField.getText();
        LocalDateTime createdAt = LocalDateTime.now();
        String[] roles = {"ROLE_USER"};

        User user = new User(email, roles, password, phoneNumber, createdAt);

        userService.registerUser(user);

        SessionManager.setCurrentUser(user);

        System.out.println("User registered successfully!");
        showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");

        Stage stage = (Stage) register.getScene().getWindow();

        profileCompletionPage();

    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void profileCompletionPage() throws IOException {
        HelloApplication.loadFXML("/profileCompletion.fxml");
    }

    private boolean validateInputs() throws SQLException {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String phoneNumber = phoneNumberField.getText().trim();

        if (email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Empty Field", "Email cannot be empty.");
            return false;
        }

        if (password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Empty Field", "Password cannot be empty.");
            return false;
        }

        if (confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Empty Field", "Confirm password cannot be empty.");
            return false;
        }

        if (phoneNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Empty Field", "Phone number cannot be empty.");
            return false;
        }

        if (!email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return false;
        }

        if (password.length() < 8) {
            showAlert(Alert.AlertType.ERROR, "Invalid Password", "Password must be at least 8 characters long.");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match.");
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            showAlert(Alert.AlertType.ERROR, "Weak Password", "Password must contain at least one uppercase letter.");
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            showAlert(Alert.AlertType.ERROR, "Weak Password", "Password must contain at least one digit.");
            return false;
        }


        if (userService.emailExists(email)) {
            showAlert(Alert.AlertType.ERROR, "Email Exists", "This email address is already registered.");
            return false;
        }

        // Phone number validation
        if (!phoneNumber.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Phone Number", "Phone number should contain only digits.");
            return false;
        }
        if (phoneNumber.length() != 8) {
            showAlert(Alert.AlertType.ERROR, "Invalid Phone Number", "Phone number must contain exactly 8 digits.");
            return false;
        }

        // If all checks pass
        return true;
    }

    @FXML
    void loginInstead() throws IOException {
        HelloApplication.loadFXML("/login.fxml");
    }
    public void handleSignIn(ActionEvent actionEvent) throws IOException {
        loginInstead();
    }

    private Stage getPrimaryStage() {
        return HelloApplication.getPrimaryStage();
    }

    public void minimizeWindow(ActionEvent actionEvent) {
        getPrimaryStage().setIconified(true);
    }

    public void expandWindow(ActionEvent actionEvent) {
        Stage stage = getPrimaryStage();
        stage.setMaximized(!stage.isMaximized());
    }

    public void closeWindow(ActionEvent actionEvent) {
        getPrimaryStage().close();
    }
}
