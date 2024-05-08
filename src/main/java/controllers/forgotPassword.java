package controllers;

import com.example.javafx.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.User;
import Services.userService;
import utils.GMailer;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class forgotPassword {

    public PasswordField password;
    public PasswordField confirm;
    @FXML
    public Button updatePassword;
    @FXML
    public Label passwordLabel;
    @FXML
    public Label confirmLabel;
    public Label phoneLabel;
    public Label emailLabel;
    public Label codeLabel;
    public Button send_mail;
    public Button code_button;
    @FXML
    private TextField mail;

    @FXML
    private TextField codeField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private RadioButton radioEmail;

    @FXML
    private RadioButton radioSMS;

    private String resetCode;
    private int generatedCodeEmail;
    private int generatedCodeSMS;


    private String resetCodeByEmail;
    private String resetCodeBySMS;

    @FXML
    private ToggleGroup toggleGroup = new ToggleGroup();

    @FXML
    private void initialize() {
        radioEmail.setToggleGroup(toggleGroup);
        radioSMS.setToggleGroup(toggleGroup);
        List<Node> components = Arrays.asList(
                password, confirm, updatePassword, passwordLabel, confirmLabel,mail,phoneNumberField, codeField,send_mail,phoneLabel,emailLabel, codeLabel,code_button
        );
        components.forEach(component -> component.setVisible(false));
        radioEmail.setOnAction(event -> updateUIBasedOnSelection());
        radioSMS.setOnAction(event -> updateUIBasedOnSelection());
        updateUIBasedOnSelection();
    }
    @FXML
    private void updateUIBasedOnSelection() {
        boolean isEmailSelected = radioEmail.isSelected();
        boolean isPhoneSelected = radioSMS.isSelected();
        emailLabel.setVisible(isEmailSelected);
        mail.setVisible(isEmailSelected);

        phoneLabel.setVisible(isPhoneSelected);
        phoneNumberField.setVisible(isPhoneSelected);

        send_mail.setVisible(isEmailSelected || isPhoneSelected);


    }


    @FXML
    private void handleResetPassword(ActionEvent event) {
        String email = mail.getText().trim();
        String codeText = codeField.getText().trim();

        if (radioEmail.isSelected()) {
            List<Node> components = Arrays.asList(
                    emailLabel, mail, send_mail
            );
            components.forEach(component -> component.setVisible(true));
            sendResetCodeByEmail(email, codeText);
        }
        else if (radioSMS.isSelected()) {
            List<Node> components = Arrays.asList(
                    phoneLabel, phoneNumberField, send_mail
            );
            components.forEach(component -> component.setVisible(true));
            sendResetCodeByEmail(email, codeText);
            String phoneNumber = phoneNumberField.getText().trim();
            sendResetCodeBySMS(phoneNumber, codeText);
        }
        else {
            System.out.println("Veuillez choisir le mode d'envoi du code de vérification.");
        }
    }

    private void sendResetCodeByEmail(String email, String code) {
        // Générer un code aléatoire de 6 chiffres
        Random random = new Random();
        generatedCodeEmail = 100000 + random.nextInt(900000); // Génère un nombre entre 100000 et 999999

        // Enregistrer le code généré dans une variable de classe pour pouvoir le vérifier ultérieurement
        this.resetCodeByEmail = String.valueOf(generatedCodeEmail);
        // Envoyer le code par e-mail à l'utilisateur
        try {
            GMailer mailer = new GMailer();
            mailer.sendResetCode(email, String.valueOf(generatedCodeEmail));
            showAlert(Alert.AlertType.INFORMATION, "Tourjoy", "Code successfully sent, check your inbox!");

            System.out.println("Code envoyé avec succès par e-mail. Veuillez vérifier votre boîte de réception.");

            // Afficher le champ pour saisir le code
            codeField.setVisible(true);
            code_button.setVisible(true);
        } catch (Exception e) {
            // Gérer l'erreur d'envoi de l'e-mail
            e.printStackTrace();
        }
    }

    private void sendResetCodeBySMS(String phoneNumber, String code) {
        // Vérifier si le numéro de téléphone existe dans la base de données
        userService userService = new userService();
        User utilisateur = userService.selectByPhoneNumber(phoneNumber);

        if (utilisateur != null) {
            // Générer un code aléatoire de 6 chiffres
            Random random = new Random();
            generatedCodeSMS = 100000 + random.nextInt(900000); // Génère un nombre entre 100000 et 999999

            // Enregistrer le code généré dans une variable de classe pour pouvoir le vérifier ultérieurement
            this.resetCodeBySMS = String.valueOf(generatedCodeSMS);

            // Envoyer le code par SMS à l'utilisateur
            try {
                String NumeroTelephone = "+216" + phoneNumber;
                SMS.sendSMS(NumeroTelephone, "Your verification code : " + String.valueOf(generatedCodeSMS));

                System.out.println("Code envoyé avec succès par SMS. Veuillez vérifier votre téléphone.");

                // Afficher le champ pour saisir le code
                codeField.setVisible(true);
                code_button.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Le numéro de téléphone n'est pas associé à un compte utilisateur.");
        }
    }

    @FXML
    private void handleVerifyCode(ActionEvent event) {
        String codeText = codeField.getText().trim();
        if (!codeText.isEmpty()) {
            try {
                int enteredCode = Integer.parseInt(codeText);
                if (radioEmail.isSelected()) {
                    if (enteredCode == Integer.parseInt(resetCodeByEmail)) {
                        showAlert(Alert.AlertType.INFORMATION, "Tourjoy", "Correct code, please update your password!");
                        System.out.println("Code correct pour l'e-mail, vous pouvez réinitialiser votre mot de passe.");
                        String email = mail.getText();
                        userService userService = new userService();
                        User user = userService.selectByEmail(email);
                        List<Node> components = Arrays.asList(
                                password, confirm, updatePassword, passwordLabel, confirmLabel
                        );
                        components.forEach(component -> component.setVisible(true));

                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Incorrect code, try again!");
                        System.out.println("Code incorrect pour l'e-mail, veuillez réessayer.");
                    }
                } else if (radioSMS.isSelected()) {
                    if (enteredCode == Integer.parseInt(resetCodeBySMS)) {
                        System.out.println("Code correct pour le SMS, vous pouvez réinitialiser votre mot de passe.");
                        String phoneNumber = phoneNumberField.getText().trim();
                        userService userService = new userService();
                        User user = userService.selectByPhoneNumber(phoneNumber);

                        List<Node> components = Arrays.asList(
                                password, confirm, updatePassword, passwordLabel, confirmLabel
                        );
                        components.forEach(component -> component.setVisible(true));
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Incorrect code, try again!");
                        System.out.println("Code incorrect pour le SMS, veuillez réessayer.");
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Choose verification method!");
                    System.out.println("Veuillez choisir le mode d'envoi du code de vérification.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Incorrect code, try again.");
                System.out.println("Veuillez saisir un code valide.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                System.out.println("Erreur lors de la récupération de l'utilisateur depuis la base de données : " + e.getMessage());
            }
        } else {
            System.out.println("Veuillez saisir le code reçu par e-mail ou par SMS.");
        }
    }

    private void loadNewPasswordPage() throws IOException {
        HelloApplication.loadFXML("/login.fxml");
    }

    @FXML
    void updatePassword(ActionEvent event) throws IOException {
        String newpassword = password.getText();
        String retype_newpassword = confirm.getText();
        if (newpassword.equals(retype_newpassword)) {
            userService.updateforgottenpassword(mail.getText(), newpassword);
            showAlert(Alert.AlertType.INFORMATION, "Tourjoy", "Password updated, please log in with your new credentials.");
            loadNewPasswordPage();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String error, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle(error);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    private Stage getPrimaryStage() {
        return HelloApplication.getPrimaryStage();
    }

    public void minimizeWindow(javafx.event.ActionEvent actionEvent) {
        getPrimaryStage().setIconified(true);
    }

    public void expandWindow(javafx.event.ActionEvent actionEvent) {
        Stage stage = getPrimaryStage();
        stage.setMaximized(!stage.isMaximized());
    }

    public void closeWindow(javafx.event.ActionEvent actionEvent) {
        getPrimaryStage().close();
    }
}




