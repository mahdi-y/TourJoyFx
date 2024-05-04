package controllers;

import Entities.Guide;
import Entities.feedback;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Services.FeedbackServices;
import Services.GuideServices;
import utils.MyDB;
import org.controlsfx.control.Rating;

import javafx.fxml.FXML;

import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFeedback {
    @FXML
    private ComboBox<String> guideCombo;
    @FXML
    private ScrollPane feedbackScrollPane;

    private String selectedGuideName;

    @FXML
    private TextArea CommentField;

    @FXML
    private Button adddF;
    @FXML
    private VBox feedbackContainer; // VBox to hold feedback items
    private List<String> badWords = Arrays.asList(
            "shit",
            "bitch",
            "Damn",
            "fuck",
            "Urination",
            "Bastard",
            "Cunt",
            "Twat",
            "Wanker",
            "Motherfucker",
            "Asshole",
            "Cocksucker",
            "wtf"
    );
    @FXML
    private Rating ratingControl;

    private FeedbackServices feedbackServices = new FeedbackServices();
    private GuideServices guideServices = new GuideServices();


    @FXML
    public void initialize() throws SQLException {
        feedbackContainer.setPadding(new Insets(10, 10, 10, 10));

        // Populate the ComboBox with guide names and ensure a default selection is made.
        populateGuideComboBox();
        // Set the default selection to the first guide if not specifically set.
        if (guideCombo.getItems().size() > 0 && guideCombo.getSelectionModel().getSelectedItem() == null) {
            guideCombo.getSelectionModel().selectFirst();
        }

        // Load feedback for the initially selected guide
        loadFeedbackForSelectedGuide();

        // Listen to changes in the ComboBox and load feedback accordingly.
        guideCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {  // Only load feedback if a new guide is actually selected
                try {
                    loadFeedbackForSelectedGuide();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        // Setup the action for the submit button
        adddF.setOnAction(event -> addFeedback());

    }


    private void populateGuideComboBox() throws SQLException {
        Map<Integer, String> guideMap = new HashMap<>();
        // Assuming guideService provides a list of guides
        List<Guide> guides = guideServices.Read();
        for (Guide guide : guides) {
            String guideName = guide.getFirstname_g() + " " + guide.getLastname_g();
            guideCombo.getItems().add(guideName);
            guideMap.put(guide.getCIN(), guideName);
        }
        guideCombo.setUserData(guideMap);
        if (!guides.isEmpty()) {
            guideCombo.getSelectionModel().selectFirst();
        } else {
            System.out.println("No guides available to select.");
        }
    }


    private boolean isValidComment(String comment) {
        if (comment == null || comment.trim().isEmpty()) {
            showAlert("Invalid Input", "Comment cannot be empty.", Alert.AlertType.ERROR);
            return false;
        }

        for (String word : badWords) {
            if (comment.toLowerCase().contains(word)) {
                showAlert("Invalid Input", "Comment contains inappropriate content.", Alert.AlertType.ERROR);
                return false;
            }
        }

        return true;
    }
    @FXML
    private void addFeedback() {
        String selectedGuideName = guideCombo.getValue();
        Map<Integer, String> guideMap = (Map<Integer, String>) guideCombo.getUserData();
        Integer selectedGuideId = guideMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(selectedGuideName))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        String comment = CommentField.getText();
        double rating = ratingControl.getRating(); // Get the current rating

        // Round the rating to one decimal place
        double roundedRating = Math.round(rating * 10) / 10.0;

        if (selectedGuideId != null && isValidComment(comment)) {
            feedback feedback = new feedback(selectedGuideId, comment, roundedRating); // Use the rounded rating
            FeedbackServices feedbackServices = new FeedbackServices();
            if (feedbackServices.add(feedback)) {
                showAlert("Thank You", "Thank you for your feedback.", Alert.AlertType.INFORMATION);
                CommentField.clear();  // Clear the comment field after submission
                ratingControl.setRating(0);  // Reset the rating control
            } else {
                showAlert("Error", "Failed to submit feedback.", Alert.AlertType.ERROR);
            }
        } else {
            System.out.println("No feedback submitted: Check that all fields are correct.");
        }
    }



    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        // We must modify this part to close the window after the alert is dismissed
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Close the current window, assuming `adddF` (the button) is part of the same window
                ((Stage) adddF.getScene().getWindow()).close();
            }
        });
    }

    public void selectGuideByName(String guideName) {
        for (int i = 0; i < guideCombo.getItems().size(); i++) {
            if (guideCombo.getItems().get(i).equalsIgnoreCase(guideName)) {
                guideCombo.getSelectionModel().select(i);
                break;
            }
        }
    }

    private void loadFeedbackForSelectedGuide() throws SQLException {
        feedbackContainer.getChildren().clear();
        String selectedGuideName = guideCombo.getValue();
        if (selectedGuideName == null) {
            System.out.println("No guide selected.");
            return;
        }

        Map<Integer, String> guideMap = (Map<Integer, String>) guideCombo.getUserData();
        Integer guideId = guideMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(selectedGuideName))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (guideId == null) {
            System.out.println("Guide ID not found for selected guide: " + selectedGuideName);
            return;
        }

        List<feedback> feedbacks = FeedbackServices.getFeedbackByGuideId(guideId);
        if (feedbacks.isEmpty()) {
            System.out.println("No feedback found for Guide ID: " + guideId);
            // Hide the ScrollPane when there are no feedback items
            feedbackScrollPane.setVisible(false);
        } else {
            // Show the ScrollPane when there are feedback items
            feedbackScrollPane.setVisible(true);
            feedbacks.forEach(this::displayFeedback);
        }
    }


    private void displayFeedback(feedback fb) {
        System.out.println("Displaying Feedback: " + fb.getComment() + ", Rating: " + fb.getRating());
        VBox feedbackBox = new VBox(5); // Increased spacing between elements
        feedbackBox.setPrefWidth(480); // Set the preferred width to match the scroll pane

        // Create label for the username and set its style
        Label userLabel = new Label("User: test");
        userLabel.getStyleClass().add("user-label");

        userLabel.getStyleClass().add("username-label");

        Label commentLabel = new Label(fb.getComment());
        commentLabel.setWrapText(true); // Wrap text to handle long comments

        Rating displayRating = new Rating();
        displayRating.getStyleClass().add("small-rating");
        displayRating.setRating(Math.round(fb.getRating() * 10) / 10.0);
        displayRating.setMax(5);
        displayRating.setPartialRating(true);
        displayRating.setDisable(true);

        feedbackBox.getStyleClass().add("feedback-box"); // Add CSS class to the feedback box

        // Set the preferred width of the comment label to the width of the scroll pane
        feedbackContainer.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue() - 20; // Adjust for padding
            commentLabel.setPrefWidth(width);
            feedbackBox.setPrefWidth(width); // Adjust feedback box width accordingly
        });

        // Add userLabel, displayRating, and commentLabel to the feedbackBox
        feedbackBox.getChildren().addAll(userLabel, displayRating, commentLabel);
        feedbackContainer.getChildren().add(feedbackBox);
    }











    @FXML
    public void onButtonEntered(javafx.scene.input.MouseEvent mouseEvent) {
        adddF.setStyle("-fx-background-color: #005a9e; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5; " +
                "-fx-padding: 5 22; " +
                "-fx-font-weight: bold;");// Revert background color when not hovered
    }


    @FXML
    public void onButtonExited(javafx.scene.input.MouseEvent mouseEvent) {
        adddF.setStyle("-fx-background-color: #000000; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 5; " +
                "-fx-padding: 5 22; " +
                "-fx-font-weight: bold;");// Revert background color when not hovered

    }
}
