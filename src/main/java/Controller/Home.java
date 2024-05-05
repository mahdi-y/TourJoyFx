package Controller;

import com.example.tourjoy.HelloApplication;
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
    public Button ClaimsButton;
    public Button HomeButton;
    @FXML
    private Button guideButton;
    @FXML
    private Button homeButton;

    @FXML
    private Button MonumentButton;




    public void goToMonuments(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/test/tjv2/MonumentFront.fxml");
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

    public void goToClaims(ActionEvent actionEvent){
        // Correct the package path in the getResource method
        URL url = getClass().getResource("/com/example/tourjoy/add-rec.fxml");
        if (url == null) {
            System.err.println("Cannot find add-rec.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                ClaimsButton.getScene().setRoot(root);
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

    public void backoffice() throws IOException {
        HelloApplication.loadFXML("/Dashboard.fxml");
    }

    public void backoffice(ActionEvent actionEvent) throws IOException {
    backoffice();
    }

    public void goToHome(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/test/tjv2/Home.fxml");
        if (url == null) {
            System.err.println("Cannot find Home.fxml");
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
