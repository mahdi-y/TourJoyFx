package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

public class Home {
    @FXML
    private Button guideButton;
    @FXML
    private Button homeButton;

    @FXML
    private void goToGuideView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/guidesFront.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) guideButton.getScene().getWindow(); // Retrieves the current window
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private void goToHomeView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Home.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) homeButton.getScene().getWindow(); // Retrieves the current window
        stage.setScene(new Scene(root));
        stage.show();
    }

}
