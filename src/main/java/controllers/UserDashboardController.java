package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import utils.UserSession;

public class UserDashboardController {
    @FXML
    private Label emailLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label countryLabel;
    @FXML
    private Label roleLabel;

    @FXML
    public void initialize() {

        UserSession session = UserSession.getInstance();

        emailLabel.setText("Email: " + session.getEmail());
        nameLabel.setText("Name: " + session.getFirstname() + " " + session.getLastname());
        phoneLabel.setText("Phone: " + session.getPhonenumber());
        countryLabel.setText("Country: " + session.getCountry());
        roleLabel.setText("Role: " + session.getRole());
    }
}
