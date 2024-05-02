package controllers;

import com.example.tourjoy.HelloApplication;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.userService;
import utils.SessionManager;
import utils.UserSession;
import models.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Home {

    public Button adminButton;
    public Label firstNameLabel;

    public void initialize(){
        UserSession session = UserSession.getInstance();

        firstNameLabel.setText("Welcome " + session.getFirstname() + "!");

        if (session.hasRole("ROLE_ADMIN")) {
//            showAlert(Alert.AlertType.INFORMATION, "Admin", "YES!");
            adminButton.setVisible(true);  // Show admin features

        } else {
//            showAlert(Alert.AlertType.ERROR, "SIKE", "NOOOOOOOO!");
            adminButton.setVisible(true);  // Hide or disable admin features
        }


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
    @FXML
    private void profile(MouseEvent mouseEvent) throws IOException {
        HelloApplication.loadFXML("/userProfile.fxml");
    }

    public void logoutUser(ActionEvent actionEvent) throws IOException {
        UserSession.getInstance().cleanUserSession();
        HelloApplication.loadFXML("/login.fxml");
    }

    public void backoffice(ActionEvent actionEvent) throws IOException {
        HelloApplication.loadFXML("/usersList.fxml");
    }
        /*public boolean isAdmin() {
            Gson gson = new Gson();
            String rolesJson = UserSession.getInstance().getRoles();  // Ensure this returns a JSON array string like '["ROLE_USER", "ROLE_ADMIN"]'
            try {
                String[] rolesArray = gson.fromJson(rolesJson, String[].class);  // Correctly parse the JSON array
                Set<String> roles = new HashSet<>(Arrays.asList(rolesArray));
                return roles.contains("ROLE_ADMIN");
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                return false;
            }
        }*/


    /*public boolean isAdmin() {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            String roles = Arrays.toString(currentUser.getRoles());
            String[] roleArray = roles.split(",");
            for (String role : roleArray) {
                if (role.trim().equals("ROLE_ADMIN")) {
                    return true;
                }
            }
        }
        return false;
    }*/
    private void showAlert(Alert.AlertType alertType, String error, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle(error);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
}
