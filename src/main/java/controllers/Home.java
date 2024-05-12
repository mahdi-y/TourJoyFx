package controllers;

import com.example.tourjoy.HelloApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import Services.userService;
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

    public void initialize() throws IOException {
        UserSession session = UserSession.getInstance();

        String[] userRolesArray = session.getRoles();
        System.out.println("roles in homepage: " + Arrays.toString(userRolesArray));
        boolean isAdmin = isAdmin(userRolesArray);
        if (isAdmin) {
            // User is an admin
            System.out.println("User is an admin.");
        } else {
            // User is not an admin
            System.out.println("User is not an admin.");
        }

        firstNameLabel.setText("Welcome " + session.getFirstname() + "!");
        System.out.println(session.getFirstname() + " " + session.getPhonenumber());

            adminButton.setVisible(true);
//            System.out.println("Admin button should now be visible.");:+


    }



    public boolean isAdmin(String[] rolesArray) {
        for (String rolesString : rolesArray) {
            // Remove square brackets and quotation marks
            String cleanedRolesString = rolesString.replace("[", "").replace("]", "").replace("\"", "");

            // Split the cleanedRolesString into individual roles
            String[] roles = cleanedRolesString.split(", ");


            // Check each role for "ROLE_ADMIN"
            for (String role : roles) {
                if ("ROLE_ADMIN".equals(role)) {
                    return true; // Found ROLE_ADMIN
                }
            }
        }
        return false; // ROLE_ADMIN not found
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

    private void showAlert(Alert.AlertType alertType, String error, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle(error);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    public void profileCompletion() throws IOException{
        HelloApplication.loadFXML("/profileCompletion.fxml");
    }
}
