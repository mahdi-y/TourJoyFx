package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.IOException;

public class frontoffice {
    @FXML
    private Button guideButton;

    @FXML
    private void goToGuideView() throws IOException {


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/AddGuide.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) guideButton.getScene().getWindow(); // Retrieves the current window
        stage.setScene(new Scene(root));
        stage.show();
    }
}
