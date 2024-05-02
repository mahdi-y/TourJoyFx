package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Dashboard {
    @FXML
    private Button gotoGuideManagement;

    @FXML
    private Button frontoffice;
    @FXML
    private Button gotoMonumentManagement;

    @FXML
    private void gotoGuideManagement() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/AddGuide.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) gotoGuideManagement.getScene().getWindow(); // Retrieves the current window
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private void gotoFrontOffice() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Home.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) frontoffice.getScene().getWindow(); // Retrieves the current window
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void gotoMonumentManagement() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Monument.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) gotoGuideManagement.getScene().getWindow(); // Retrieves the current window
        stage.setScene(new Scene(root));
        stage.show();
    }
}
