package controllers;

import com.example.tourjoy.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.userService;
import utils.SessionManager;
import utils.UserSession;
import models.User;

import java.io.IOException;
import java.util.Arrays;

public class Home {

    public Button adminButton;
    private userService userService;

    public void initialize(){
        userService = new userService();
        User currentUser = SessionManager.getCurrentUser();

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

    public boolean isAdmin() {
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
    }




}
