package controllers;

import com.example.javafx.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import Services.userService;
import utils.SessionManager;
import utils.UserSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;

public class userProfileController {

    public ComboBox<String> countryComboBox;
    public TextField imagePathField;
    public ImageView profileImageView;
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

    private userService userService;

    public void initialize() {
        userService = new userService();
        User currentUser = SessionManager.getCurrentUser();
        emailField.setText("" +currentUser.getEmail());
        firstNameField.setText("" +currentUser.getFirstName());
        lastNameField.setText("" +currentUser.getLastName());
        phoneNumberField.setText("" + currentUser.getPhoneNumber());
//        countryField.setText("Country: " + currentUser.getCountry());
//        lastNameField.setText("Role: " + Arrays.toString(currentUser.getRoles()));
    }

    @FXML
    void saveChanges(ActionEvent event) throws IOException, SQLException {
        if (!validateInputs()) {
            return;
        }
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Session Error", "No user is currently logged in.");
            return;
        }
        try {
//            System.out.println("Before Update: " + firstNameField.getText() + ", " + lastNameField.getText() + ", " + countryComboBox.getValue() + ", " + imagePathField.getText());
            currentUser.setEmail(emailField.getText().trim());
            currentUser.setFirstName(firstNameField.getText().trim());
            currentUser.setLastName(lastNameField.getText().trim());
           // currentUser.setCountry(countryComboBox.getValue());
            //currentUser.setProfilePicture(imagePathField.getText().trim());
            currentUser.setModifiedAt(LocalDateTime.now());
            /*Path path = Paths.get(imagePathField.getText());
            String fileName = path.getFileName().toString();
            Path destinationPath = Paths.get("src/main/resources/images/profilePictures", fileName);*/

            userService.updateProfile(currentUser, currentUser.getEmail());

           /* try {
                Files.copy(path, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating profile: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Unexpected Error", "An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
//            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            
        }
    }

    @FXML
    private void cancelUpdate(ActionEvent actionEvent) {
        UserSession userSession = UserSession.getInstance();
        firstNameField.setText(userSession.getFirstname());
        lastNameField.setText(userSession.getLastname());
        phoneNumberField.setText(userSession.getPhonenumber());
        //countryField.setText(userSession.getCountry());
        emailField.setText(userSession.getEmail());
        //profilePictureField.setText(userSession.getPicture());
    }

    public void handleUploadImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload your profile picture");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String fileName = selectedFile.getName().toLowerCase();
            if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                imagePathField.setText(selectedFile.getPath());
                Image image = new Image(selectedFile.toURI().toString());
                profileImageView.setImage(image);
            } else {
                System.out.println("Invalid file format. Please select a PNG or JPG file.");
            }
        } else {
            System.out.println("No file selected");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateInputs() throws SQLException {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        if (firstName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid First Name", "First name cannot be empty.");
            return false;
        }
        if (!firstName.matches("[a-zA-Z]+")) {
            showAlert(Alert.AlertType.ERROR, "Invalid First Name", "First name should only contain alphabetic characters.");
            return false;
        }
        if (firstName.length() > 25) {
            showAlert(Alert.AlertType.ERROR, "Invalid First Name", "First name must contain less than 25 characters");
            return false;
        }
        if (firstName.length() < 3) {
            showAlert(Alert.AlertType.ERROR, "Invalid First Name", "First name must contain more than 2 characters");
            return false;
        }


        if (lastName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Last Name", "Last name cannot be empty.");
            return false;
        }
        if (!lastName.matches("[a-zA-Z]+")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Last Name", "Last name should only contain alphabetic characters.");
            return false;
        }
        if (lastName.length() > 25) {
            showAlert(Alert.AlertType.ERROR, "Invalid First Name", "First name must contain less than 25 characters");
            return false;
        }
        if (lastName.length() < 3) {
            showAlert(Alert.AlertType.ERROR, "Invalid First Name", "First name must contain more than 2 characters");
            return false;
        }
        return true;
    }

    private Stage getPrimaryStage() {
        return HelloApplication.getPrimaryStage();
    }
    public void minimizeWindow(ActionEvent actionEvent) {
        getPrimaryStage().setIconified(true);
    }

    public void expandWindow(ActionEvent actionEvent) {
        Stage stage = getPrimaryStage();
        stage.setMaximized(!stage.isMaximized());
    }

    public void closeWindow(ActionEvent actionEvent) {
        getPrimaryStage().close();
    }

    public void homepage(ActionEvent actionEvent) throws IOException {
        homepage();
    }

    public void homepage() throws IOException{
        HelloApplication.loadFXML("/Home.fxml");
    }
}
