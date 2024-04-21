package controllers;

import entities.feedback;
import javafx.scene.control.Alert;
import services.FeedbackServices;
import utils.MyDB;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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

    private String selectedGuideName;

    @FXML
    private TextField CommentField;

    @FXML
    private Button adddF;
    private List<String> badWords = Arrays.asList("badword1", "badword2", "badword3"); // Example bad words


    @FXML
    public void initialize() {
        populateGuideComboBox();
        if (selectedGuideName != null && !selectedGuideName.isEmpty()) {
            selectGuideByName(selectedGuideName);  // Use the new method for selecting the guide
        }
        adddF.setOnAction(event -> addFeedback());
    }

    private void populateGuideComboBox() {
        try {
            String query = "SELECT CIN, firstname_g, lastname_g FROM Guide";
            Connection conn = MyDB.getInstance().getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            Map<Integer, String> guideMap = new HashMap<>();

            while (resultSet.next()) {
                String guideName = resultSet.getString("firstname_g") + " " + resultSet.getString("lastname_g");
                int guideId = resultSet.getInt("CIN");
                guideMap.put(guideId, guideName);
                guideCombo.getItems().add(guideName);
            }

            guideCombo.setUserData(guideMap);

            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle any errors here
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

        if (selectedGuideId != null && isValidComment(comment)) {
            feedback feedback = new feedback(selectedGuideId, comment);
            FeedbackServices feedbackServices = new FeedbackServices();
            if (feedbackServices.add(feedback)) {
                showAlert("Thank You", "Thank you for your feedback.", Alert.AlertType.INFORMATION);
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
        alert.showAndWait();
    }
    public void selectGuideByName(String guideName) {
        for (int i = 0; i < guideCombo.getItems().size(); i++) {
            if (guideCombo.getItems().get(i).equalsIgnoreCase(guideName)) {
                guideCombo.getSelectionModel().select(i);
                break;
            }
        }
    }
}
