package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.User;
import services.userService;
import utils.UserSession;

import java.sql.SQLException;

public class userProfileController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField profilePictureField;

    private final userService userService;

    public userProfileController(userService userService) {
        this.userService = userService;
    }

    public void initialize() {
        UserSession userSession = UserSession.getInstance();
        firstNameField.setText(userSession.getFirstname());
        lastNameField.setText(userSession.getLastname());
        phoneNumberField.setText(userSession.getPhonenumber());
        countryField.setText(userSession.getCountry());
        emailField.setText(userSession.getEmail());
        profilePictureField.setText(userSession.getPicture());
    }

    @FXML
    private void saveChanges(ActionEvent actionEvent) {
        UserSession userSession = UserSession.getInstance();
        try {
            int phoneNumber = Integer.parseInt(phoneNumberField.getText());
            User user = new User(userSession.getId(), emailField.getText(),
                    firstNameField.getText(), lastNameField.getText(), countryField.getText(), profilePictureField.getText(), phoneNumber);
            userService.updateProfile(user, userSession.getEmail());
            userSession.setUser(user);
            System.out.println("Profile updated successfully!");
        } catch (NumberFormatException e) {
            System.err.println("Invalid phone number format: " + phoneNumberField.getText());
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to update profile: " + e.getMessage());
        }
    }

    @FXML
    private void cancelUpdate(ActionEvent actionEvent) {
        UserSession userSession = UserSession.getInstance();
        firstNameField.setText(userSession.getFirstname());
        lastNameField.setText(userSession.getLastname());
        phoneNumberField.setText(userSession.getPhonenumber());
        countryField.setText(userSession.getCountry());
        emailField.setText(userSession.getEmail());
        profilePictureField.setText(userSession.getPicture());
    }

}
