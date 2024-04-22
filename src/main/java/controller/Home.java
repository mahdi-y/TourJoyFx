package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class Home {
    public Button AccButton;
    public Label idU;
    public ImageView heroImageView;
    public ImageView profileImageView;
    public Button guideButton;
    public Button MonumentButton;

    public void goToAcc(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/javafx/accFront.fxml"));
            AccButton.getScene().setRoot(root);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
