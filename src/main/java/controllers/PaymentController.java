package controllers;
import entities.Booking;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import test.PaymentService;
import services.BookingServices;
import services.GuideServices;


import java.sql.SQLException;

public class PaymentController {
    @FXML private TextField cardNumberField;
    @FXML private TextField expiryDateField;
    @FXML private TextField cvcField;

    private PaymentService paymentService = new PaymentService("sk_test_51OqFkjEFVyu18y1ZCryKBaN30pD1JrG5u4ZVWIYlwF2NavSlVfa5QxLA69t5I2upCuT2a1UPhvsbzQR9TepmZh3z00c6QBIufa");
    @FXML
    private GuideServices guideServices;
    private BookingServices bookingServices;  // Declare the bookingServices

    public void initialize() {
        guideServices = new GuideServices();
        bookingServices = new BookingServices();  // Initialize it

        // Other initialization code
    }
    @FXML
    private void handlePaymentProcess() {
        try {
            Booking lastBooking = bookingServices.getLastBooking();
            double guidePrice = guideServices.getGuidePrice(lastBooking.getGuide());

            String cardNumber = cardNumberField.getText();
            String expiryDate = expiryDateField.getText();
            String cvc = cvcField.getText();

            String token = "tok_visa"; // Placeholder for token received from payment API

            if (paymentService.processPayment(token, guidePrice)) {
                showAlert("Payment Successful", "Your payment was successful!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Payment Failed", "Your payment could not be processed. Please try again.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Error accessing database information.", Alert.AlertType.ERROR);
        } catch (NullPointerException e) {
            showAlert("Data Error", "No recent bookings found.", Alert.AlertType.ERROR);
        }
    }


    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
