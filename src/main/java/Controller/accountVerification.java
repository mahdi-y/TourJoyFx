package Controller;

import javafx.event.ActionEvent;
import Services.userService;
import com.example.tourjoy.HelloApplication;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.SessionManager;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import javafx.scene.control.TextField;

public class accountVerification {

    public TextField codeField;
    public Button verifyButton;
    private int generatedCodeSMS;
    private String resetCodeBySMS;
//    UserSession session = UserSession.getInstance();


    private void resendResetCodeBySMS() {
//        userService userService = new userService();
        int phoneNumberSession = Integer.parseInt(SessionManager.getCurrentUser().getPhoneNumber().toString());
        Random random = new Random();
        generatedCodeSMS = 100000 + random.nextInt(900000);

        this.resetCodeBySMS = String.valueOf(generatedCodeSMS);

        try {
            String NumeroTelephone = "+216" + phoneNumberSession;
            SMS.sendSMS(NumeroTelephone, "Your verification code : " + String.valueOf(generatedCodeSMS));

            System.out.println("Code envoyé avec succès par SMS. Veuillez vérifier votre téléphone.");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleVerify(ActionEvent event) {
        String codeText = codeField.getText().trim();
        if (!codeText.isEmpty()) {
            try {
                int enteredCode = Integer.parseInt(codeText);
                int expectedCode = Integer.parseInt(resetCodeBySMS);

                if (enteredCode == expectedCode) {
                    userService userService = new userService();
                    userService.verifyUser(SessionManager.getCurrentUser().getEmail());
                    System.out.println("Code correct for SMS, you can reset your password.");
                    System.out.println("the current user's email : " + SessionManager.getCurrentUser().getEmail());
                    showAlert(Alert.AlertType.INFORMATION, "Verification Successful", "Your account has been verified.");
                    login();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Incorrect code, try again!");
                    System.out.println("Incorrect code for SMS, please try again.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid code.");
                System.out.println("Please enter a valid code.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Code cannot be empty.");
        }
    }


    private void showAlert(Alert.AlertType alertType, String error, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle(error);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    public void resendCode(ActionEvent actionEvent) {
        resendResetCodeBySMS();
    }

    public void login() throws IOException{
        HelloApplication.loadFXML("/login.fxml");
    }
}
