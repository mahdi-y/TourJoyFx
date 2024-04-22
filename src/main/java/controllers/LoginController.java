package controllers;

import com.example.tourjoy.HelloApplication;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import services.userService;

import utils.UserSession;


import models.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class LoginController {

    public PasswordField passwordField;
    public Button signInButton;
    public TextField emailField;



    @FXML
    public void handleSignIn(ActionEvent actionEvent) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // Ensure both fields are filled in
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter both email and password.");
            return;
        }

        try {
            User user = userService.loginUser(email, password);
            if (user != null) {
                UserSession.getInstance(user.getId(),user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getCountry(),user.getProfilePicture(), String.valueOf(user.getPhoneNumber().intValue()), Arrays.toString(user.getRoles())).setUser(user);
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + user.getEmail() + "!");
                System.out.println("Roles: " + Arrays.toString(user.getRoles()));

                if (Arrays.asList(user.getRoles()).contains("ROLE_ADMIN")) {
                    HelloApplication.loadFXML("/userDashboard.fxml");
                } else {
                    HelloApplication.loadFXML("/usersList.fxml");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while attempting to sign in.");
            e.printStackTrace();
        }
    }

    @FXML
    void registerInstead() throws IOException {
        HelloApplication.loadFXML("/signup.fxml");
    }

    public void handleSignUp(ActionEvent actionEvent) throws IOException {
        registerInstead();
    }




    private void showAlert(Alert.AlertType alertType, String error, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle(error);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
    }
