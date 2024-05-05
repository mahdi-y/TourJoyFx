package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Dashboard {
    @FXML
    private Button gotoGuideManagement;

    @FXML
    private Button frontoffice;
    @FXML
    private Button gotoMonumentManagement;





    public void gotoGuideManagement(javafx.event.ActionEvent actionEvent) {
        URL url = getClass().getResource("/AddGuide.fxml");
        if (url == null) {
            System.err.println("Cannot find Guides.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                gotoGuideManagement.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    public void gotoMonumentManagement(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/test/tjv2/Monument.fxml");
        if (url == null) {
            System.err.println("Cannot find Monument.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                gotoMonumentManagement.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    public void gotoFrontOffice(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/test/tjv2/Home.fxml");
        if (url == null) {
            System.err.println("Cannot find Monument.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                frontoffice.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    public void gotoUserManagment(ActionEvent actionEvent) {
        URL url = getClass().getResource("/usersList.fxml");
        if (url == null) {
            System.err.println("Cannot find Monument.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                frontoffice.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    public void goToClaimsManagement(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/example/tourjoy/back.fxml");
        if (url == null) {
            System.err.println("Cannot find back.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                frontoffice.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }
}
