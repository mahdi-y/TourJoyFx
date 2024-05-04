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
    private Button monumentsButton;

    @FXML
    private void goToGuideView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/test/tjv2/guidesFront.fxml")); // Make sure the path is correct
        Parent root = loader.load();

        Scene scene = new Scene(root);
        String css = this.getClass().getResource("/style/styles.css").toExternalForm(); // Update the path to your CSS file
        scene.getStylesheets().add(css); // Adding CSS to the scene

        Stage stage = (Stage) guideButton.getScene().getWindow(); // Retrieves the current window
        stage.setScene(scene);
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
