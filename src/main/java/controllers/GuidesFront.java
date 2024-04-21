package controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import entities.Guide;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.GuideServices;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import javafx.scene.control.Button;


public class GuidesFront {

    @FXML
    private Button guideButton;

    @FXML
    private Button homeButton;
    @FXML
    private Button feedbackButton;
    @FXML
    private TilePane tilePane;
    private GuideServices guideServices = new GuideServices();


    public void initialize() {
        loadGuides();
    }

    private void loadGuides() {
        try {
            List<Guide> guides = guideServices.Read();  // Use the Read method from GuideServices
            for (Guide guide : guides) {
                VBox guideBox = createGuideBox(guide);
                tilePane.getChildren().add(guideBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions (e.g., show an error message)
        }
    }

    private VBox createGuideBox(Guide guide) {
        VBox box = new VBox(10);  // Adjusted the spacing to 10 for better aesthetics
        box.getStyleClass().add("guide-box");

        StackPane imagePane = new StackPane(); // StackPane to contain the image
        ImageView imageView = new ImageView(new Image(guide.getImage()));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        imagePane.getChildren().add(imageView);

        Label nameLabel = new Label(guide.getFirstname_g() + " " + guide.getLastname_g());
        nameLabel.getStyleClass().add("guide-name");

        Label languageLabel = new Label("Language: " + guide.getLanguage());
        languageLabel.getStyleClass().add("guide-detail");

        Button bookButton = new Button("Book");
        bookButton.setOnAction(event -> handleBooking(guide));  // Assuming you have a method to handle booking
        bookButton.getStyleClass().addAll("book-button", "orange-button"); // Add "orange-button" style class

        Button feedbackButton = new Button("Give Feedback");  // Corrected button text
        feedbackButton.setOnAction(event -> handleFeedback(guide));  // Assuming you have a method to handle feedback
        feedbackButton.getStyleClass().addAll("feedback-button", "orange-button"); // Add "orange-button" style class

        // Set the same size for all image panes
        imagePane.setMinSize(100, 100);
        imagePane.setMaxSize(100, 100);

        box.getChildren().addAll(imagePane, nameLabel, languageLabel, bookButton, feedbackButton);
        return box;
    }





    private void handleBooking(Guide guide) {
        try {
            // Load the FXML document for booking a guide
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BookGuideFront.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the selected guide to it
            BookGuideFront controller = loader.getController();
            // Assuming guide's fullname is a combination of first name and last name
            controller.selectGuideByName(guide.getFirstname_g() + " " + guide.getLastname_g());

            // Setup the stage and scene
            Stage stage = new Stage();
            stage.setTitle("Book Guide: " + guide.getFirstname_g() + " " + guide.getLastname_g());
            stage.initModality(Modality.APPLICATION_MODAL); // Block user interaction with other windows
            stage.setScene(new Scene(root));

            // Show the booking window
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }

    private void handleFeedback(Guide guide) {
        try {
            // Load the FXML document for booking a guide
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddFeedback.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the selected guide to it
            AddFeedback controller = loader.getController();
            // Assuming guide's fullname is a combination of first name and last name
            controller.selectGuideByName(guide.getFirstname_g() + " " + guide.getLastname_g());

            // Setup the stage and scene
            Stage stage = new Stage();
            stage.setTitle("Give Feedback: " + guide.getFirstname_g() + " " + guide.getLastname_g());
            stage.initModality(Modality.APPLICATION_MODAL); // Block user interaction with other windows
            stage.setScene(new Scene(root));

            // Show the booking window
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }

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