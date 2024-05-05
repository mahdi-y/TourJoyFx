package controllers;

import com.example.tourjoy.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.User;
import Services.userService;
import utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import utils.UserSession;


public class registrationController {

    public PasswordField confirmPasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private Button register;
    private userService userService;

    @FXML
    private TextField passwordFieldVisible;
    @FXML
    private TextField confirmPasswordFieldVisible;

    @FXML
    private CheckBox toggleVisibilityCheckbox;

    private String resetCodeBySMS;
    private int generatedCodeSMS;




    @FXML
    public void initialize() {
        userService = new userService();
        register.setOnAction(event -> {
            try {
                registerUser();
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void registerUser() throws IOException, SQLException {
        if (!validateInputs()) {
            return;
        }
        String email = emailField.getText();
        String password = passwordField.getText();
        int phoneNumber = Integer.parseInt(phoneNumberField.getText());
        String confirmPassword = confirmPasswordField.getText();
        LocalDateTime createdAt = LocalDateTime.now();
        String[] roles = {"ROLE_USER"};

        User user = new User(email, roles, password, phoneNumber, createdAt);

        SessionManager.setCurrentUser(user);

        userService.registerUser(user);
//        sendResetCodeBySMS();
        System.out.println("User registered successfully!");
        showAlert(Alert.AlertType.INFORMATION, "Success", "Account successfully created! Please verify your account.");

        accountVerification();

    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void login() throws IOException {
        HelloApplication.loadFXML("/login.fxml");
    }

    private boolean validateInputs() throws SQLException {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String phoneNumber = phoneNumberField.getText().trim();

        if (email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Empty Field", "Email cannot be empty.");
            return false;
        }

        if (password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Empty Field", "Password cannot be empty.");
            return false;
        }

        if (confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Empty Field", "Confirm password cannot be empty.");
            return false;
        }

        if (phoneNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Empty Field", "Phone number cannot be empty.");
            return false;
        }

        if (!email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return false;
        }

        if (password.length() < 8) {
            showAlert(Alert.AlertType.ERROR, "Invalid Password", "Password must be at least 8 characters long.");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match.");
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            showAlert(Alert.AlertType.ERROR, "Weak Password", "Password must contain at least one uppercase letter.");
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            showAlert(Alert.AlertType.ERROR, "Weak Password", "Password must contain at least one digit.");
            return false;
        }


        if (userService.emailExists(email)) {
            showAlert(Alert.AlertType.ERROR, "Email Exists", "This email address is already registered.");
            return false;
        }

        /*if (userService.phoneNumberExists(Integer.parseInt(phoneNumber))){
            showAlert(Alert.AlertType.ERROR,"Phone Number Exists", "This phone number is already registered.");
            return false;
        }*/

        if (!phoneNumber.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Phone Number", "Phone number should contain only digits.");
            return false;
        }
        if (phoneNumber.length() != 8) {
            showAlert(Alert.AlertType.ERROR, "Invalid Phone Number", "Phone number must contain exactly 8 digits.");
            return false;
        }

        // If all checks pass
        return true;
    }

    @FXML
    void loginInstead() throws IOException {
        HelloApplication.loadFXML("/login.fxml");
    }
    public void handleSignIn(ActionEvent actionEvent) throws IOException {
        loginInstead();
    }

    public void accountVerification() throws IOException{
        HelloApplication.loadFXML("/accountVerification.fxml");
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


    @FXML
    void togglePasswordVisibility() {
        if (passwordField.isVisible()) {
            passwordFieldVisible.setText(passwordField.getText());
            passwordFieldVisible.setVisible(true);
            passwordFieldVisible.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);

            confirmPasswordFieldVisible.setText(confirmPasswordField.getText());
            confirmPasswordFieldVisible.setVisible(true);
            confirmPasswordFieldVisible.setManaged(true);
            confirmPasswordField.setVisible(false);
            confirmPasswordField.setManaged(false);

            toggleVisibilityCheckbox.setText("Hide password");
        } else {
            passwordField.setText(passwordFieldVisible.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordFieldVisible.setVisible(false);
            passwordFieldVisible.setManaged(false);

            confirmPasswordField.setText(confirmPasswordFieldVisible.getText());
            confirmPasswordField.setVisible(true);
            confirmPasswordField.setManaged(true);
            confirmPasswordFieldVisible.setVisible(false);
            confirmPasswordFieldVisible.setManaged(false);
            toggleVisibilityCheckbox.setText("Show password");
        }
    }

    @FXML
    private void generateStrongPassword() {
        String strongPassword = generateRandomPassword(24);
        passwordField.setText(strongPassword);
        confirmPasswordField.setText(strongPassword);

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(strongPassword);
        clipboard.setContent(content);

        showAlert(Alert.AlertType.INFORMATION, "Password Copied", "A strong password has been generated and copied to your clipboard.");

    }

    private String generateRandomPassword(int length) {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = upperCaseLetters.toLowerCase();
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()_-+=<>?";
        String combinedChars = upperCaseLetters + lowerCaseLetters + digits + specialChars;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        // Ensure the password has at least one character of each type
        sb.append(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        sb.append(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        sb.append(digits.charAt(random.nextInt(digits.length())));
        sb.append(specialChars.charAt(random.nextInt(specialChars.length())));

        // Fill the rest of the password
        for (int i = sb.length(); i < length; i++) {
            sb.append(combinedChars.charAt(random.nextInt(combinedChars.length())));
        }

        // Shuffle the characters to avoid a predictable pattern
        List<Character> letters = sb.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        Collections.shuffle(letters);
        StringBuilder shuffled = new StringBuilder();
        for (char letter : letters) {
            shuffled.append(letter);
        }

        return shuffled.toString();
    }
    private void sendResetCodeBySMS() {
        if (phoneNumberField.getText() != null) {
            Random random = new Random();
            generatedCodeSMS = 100000 + random.nextInt(900000);

            this.resetCodeBySMS = String.valueOf(generatedCodeSMS);

            try {
                String NumeroTelephone = "+216" + phoneNumberField.getText().trim();
                SMS.sendSMS(NumeroTelephone, "Your verification code : " + String.valueOf(generatedCodeSMS));

                System.out.println("Code envoyé avec succès par SMS. Veuillez vérifier votre téléphone.");


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Le numéro de téléphone n'est pas associé à un compte utilisateur.");
        }
    }
}
