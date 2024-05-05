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

    public void initialize() throws IOException {
        UserSession session = UserSession.getInstance();

        firstNameLabel.setText("Welcome " + session.getFirstname() + "!");
        System.out.println(session.getFirstname() + " " + session.getPhonenumber());

        if (isAdmin()) {
            adminButton.setVisible(true); // Show admin features if user is admin
        } else {
            adminButton.setVisible(false); // Hide admin features if user is not admin
        }

    }

    private boolean isAdmin() {
        String[] roles = UserSession.getInstance().getRoles();
        for (String role : roles) {
            if ("ROLE_ADMIN".equals(role)) {
                return true;
            }
        }
        return false;
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

    public void claims(ActionEvent actionEvent) throws IOException {
        HelloApplication.loadFXML("/com/example/tourjoy/add-rec.fxml");
    }
}
