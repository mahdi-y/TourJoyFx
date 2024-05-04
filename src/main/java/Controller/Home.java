package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;

public class Home {
    @FXML
    private Button guideButton;
    @FXML
    private Button homeButton;

    @FXML
    private Button MonumentButton;




    public void goToMonuments(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/test/tjv2/MonumentDetails.fxml");
        if (url == null) {
            System.err.println("Cannot find MonumentDetails.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                MonumentButton.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    public void goToGuides(ActionEvent actionEvent) {
        URL url = getClass().getResource("/guidesFront.fxml");
        if (url == null) {
            System.err.println("Cannot find Guides.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                guideButton.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }
}
