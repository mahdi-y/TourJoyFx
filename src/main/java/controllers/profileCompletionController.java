package controllers;

import com.example.tourjoy.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import models.User;
import services.userService;
import utils.SessionManager;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;

import java.io.File;

public class profileCompletionController {
    public TextField lastNameField;
    public TextField firstNameField;
    public ComboBox<String> countryComboBox;
    public ImageView profileImageView;
    public TextField imagePathField;
    public Label welcomeLabel;
    private userService userService;
    private static final String IMAGE_DIRECTORY = "./src/main/resources/images/profilePictures";


    public void initialize() {
        userService = new userService();
        User currentUser = SessionManager.getCurrentUser();
        ObservableList<String> countries = FXCollections.observableArrayList("Palestine", "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe");
        countryComboBox.setItems(countries);
        if (currentUser != null && currentUser.getEmail() != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getEmail() + "!");
        } else {
            welcomeLabel.setText("Welcome!");
        }
    }


    @FXML
    void updateProfile(ActionEvent event) throws IOException, SQLException {
        if (!validateInputs()) {
            return;
        }
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Session Error", "No user is currently logged in.");
            return;
        }
        try {
            System.out.println("Before Update: " + firstNameField.getText() + ", " + lastNameField.getText() + ", " + countryComboBox.getValue() + ", " + imagePathField.getText());

            currentUser.setFirstName(firstNameField.getText().trim());
            currentUser.setLastName(lastNameField.getText().trim());
            currentUser.setCountry(countryComboBox.getValue());
            currentUser.setProfilePicture(imagePathField.getText().trim());
            currentUser.setModifiedAt(LocalDateTime.now());
            Path path = Paths.get(imagePathField.getText());
            String fileName = path.getFileName().toString();
            Path destinationPath = Paths.get("src/main/resources/images/profilePictures", fileName);

            userService.updateProfile(currentUser, currentUser.getEmail());

            try {
                Files.copy(path, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating profile: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Unexpected Error", "An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
//            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            UserDashboardPage();
        }
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

    @FXML
    void UserDashboardPage() throws IOException {
        HelloApplication.loadFXML("/Home.fxml");
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
}
