package controllers;

import entities.feedback;
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
import java.util.HashMap;
import java.util.Map;

public class AddFeedback {
    @FXML
    private ComboBox<String> guideCombo;

    @FXML
    private TextField CommentField;

    @FXML
    private Button adddF;

    @FXML
    public void initialize() {
        populateGuideComboBox();
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

    @FXML
    private void addFeedback() {
        String selectedGuideName = guideCombo.getValue();
        Map<Integer, String> guideMap = (Map<Integer, String>) guideCombo.getUserData();
        Integer selectedGuideId = guideMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(selectedGuideName))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (selectedGuideId != null && CommentField.getText() != null && !CommentField.getText().isEmpty()) {
            feedback feedback = new feedback(selectedGuideId, CommentField.getText());
            FeedbackServices feedbackServices = new FeedbackServices();
            feedbackServices.add(feedback);
        } else {
            System.out.println("No feedback submitted: Check that all fields are correct.");
        }
    }

}
