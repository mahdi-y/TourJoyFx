package controllers;

import com.example.tourjoy.HelloApplication;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import services.userService;

import utils.SessionManager;
import utils.UserSession;


import models.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.google.gson.Gson;

public class LoginController {

    public PasswordField passwordField;
    public Button signInButton;
    public TextField emailField;



    @FXML
    public void handleSignIn(ActionEvent actionEvent) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter both email and password.");
            return;
        }

        try {
            User user = userService.loginUser(email, password);
            if (user != null) {
                UserSession.getInstance(user.getId(), user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getCountry(), user.getProfilePicture(), String.valueOf(user.getPhoneNumber().intValue()), user.getRoles()).setUser(user);
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + user.getEmail() + "!");



                SessionManager.setCurrentUser(user);

                if (UserSession.getInstance().hasRole("ROLE_ADMIN")) {
                    showAlert(Alert.AlertType.INFORMATION, "Admin Access", "You have admin privileges.");
                    redirect_passwordpage();
                }else{
                    showAlert(Alert.AlertType.INFORMATION, "Client Access", "You don't have admin privileges.");
                    redirect_homePage();
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

    public void redirect_passwordpage() throws IOException {
        HelloApplication.loadFXML("/forgotPassword.fxml");
    }

    public void redirect_homePage() throws IOException{
        HelloApplication.loadFXML("/Home.fxml");
    }

    /*public boolean hasAdminRole(User user) {
        Set<String> roles = user.getRoles();
        return roles.contains("ROLE_ADMIN");
    }*/

}
