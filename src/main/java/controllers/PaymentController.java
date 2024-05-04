package controllers;
import Entities.Booking;
import Entities.Guide;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import Test.PaymentService;
import Services.BookingServices;
import Services.GuideServices;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class PaymentController {
    @FXML private TextField cardNumberField;
    @FXML private TextField expiryDateField;
    @FXML private TextField cvcField;
    private Stage primaryStage;
    private PaymentService paymentService = new PaymentService("sk_test_51OqFkjEFVyu18y1ZCryKBaN30pD1JrG5u4ZVWIYlwF2NavSlVfa5QxLA69t5I2upCuT2a1UPhvsbzQR9TepmZh3z00c6QBIufa");


    private GuideServices guideServices;
    private BookingServices bookingServices;  // Declare the bookingServices
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage; // Method to set the primary stage from your main application class
    }
    public void initialize() {
        guideServices = new GuideServices();
        bookingServices = new BookingServices();  // Initialize it

        // Other initialization code
    }

    private boolean validateCardNumber(String cardNumber) {
        return cardNumber.matches("\\d{16}");
    }

    private boolean validateCVC(String cvc) {
        return cvc.matches("\\d{3}");  // Exactly 3 digits
    }

    private boolean validateExpiryDate(String expiryDate) {
        if (!expiryDate.matches("\\d{2}/\\d{2}")) {
            return false; // Ensure format is MM/YY
        }
        String[] parts = expiryDate.split("/");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]);
        return month >= 1 && month <= 12 && year >= 24;
    }

    @FXML
    private void handlePaymentProcess() {
        Connection connection = null;
        try {
            // Get the card information from the input fields
            String cardNumber = cardNumberField.getText();
            String expiryDate = expiryDateField.getText();
            String cvc = cvcField.getText();

            // Validate card details
            if (!validateCardNumber(cardNumber)) {
                showAlert("Validation Error", "Invalid Card Number.", Alert.AlertType.ERROR);
                return;
            }

            if (!validateCVC(cvc)) {
                showAlert("Validation Error", "Invalid CVC.", Alert.AlertType.ERROR);
                return;
            }

            if (!validateExpiryDate(expiryDate)) {
                showAlert("Validation Error", "Invalid Expiry Date.", Alert.AlertType.ERROR);
                return;
            }

            // Retrieve the last booking
            Booking lastBooking = bookingServices.getLastBooking();

            // Get the guide price for the last booking
            double guidePrice = guideServices.getGuidePrice(lastBooking.getGuide());

            // Placeholder for token received from payment API
            String token = "tok_visa";

            boolean paymentSuccessful = paymentService.processPayment(token, guidePrice);

            // Update booking status based on payment success
            if (paymentSuccessful) {
                lastBooking.setStatus("Paid");
                bookingServices.updateBooking(lastBooking);
                showAlert("Payment Successful", "Your payment was successful!", Alert.AlertType.INFORMATION);
                generatePDF(lastBooking, guideServices, primaryStage);
                closeWindow();  // Close the payment window
            } else {
                // Update booking status to unpaid
                lastBooking.setStatus("Unpaid");
                bookingServices.updateBooking(lastBooking);
                showAlert("Payment Failed", "Your payment could not be processed. Please try again.", Alert.AlertType.ERROR);
            }
        } catch (NullPointerException e) {
            showAlert("Data Error", "No recent bookings found.", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Database Error", "Error updating booking status in the database.", Alert.AlertType.ERROR);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void closeWindow() {
        // Get the current stage from any control, here using cardNumberField
        Stage currentStage = (Stage) cardNumberField.getScene().getWindow();
        currentStage.close(); // Closes the window
    }





    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void generatePDF(Booking booking, GuideServices guideServices, Stage primaryStage) {
        Guide guide = guideServices.fetchGuideById(booking.getGuide());

        if (guide == null) {
            showAlert("Error", "No guide found with ID: " + booking.getGuide(), Alert.AlertType.ERROR);
            return;
        }

        PDDocument document = new PDDocument();
        try {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Load and draw the image
          //  PDImageXObject pdImage = PDImageXObject.createFromFile("@resources/image/tourjoy.png", document); // Correct path
            //contentStream.drawImage(pdImage, 50, 700, 100, 100); // Adjust size and position as needed

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.newLineAtOffset(180, 700); // Adjust text position to not overlap the image
            contentStream.showText("Booking Information");
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.setLeading(14.5f);

            contentStream.newLine();
            contentStream.newLine();
            contentStream.showText("Guide Name: " + guide.getFirstname_g() + " " + guide.getLastname_g());
            contentStream.newLine();
            contentStream.showText("Booking Date: " + booking.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            contentStream.newLine();
            contentStream.showText("Price: $" + guide.getPrice());
            contentStream.endText();
            contentStream.close();

            // Instead of saving directly, call savePDF to let user choose location
            savePDF(document, primaryStage);  // Pass primaryStage to use it as the parent for FileChooser

        } catch (IOException e) {
            showAlert("PDF Generation Error", "Failed to generate the booking details PDF.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void savePDF(PDDocument document, Stage primaryStage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        // Set the initial file name to "PaymentConfirmation.pdf"
        fileChooser.setInitialFileName("PaymentConfirmation.pdf");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            document.save(file);
            showAlert("PDF Generated", "Booking details PDF has been saved.", Alert.AlertType.INFORMATION);
        }
        document.close();
    }
}
