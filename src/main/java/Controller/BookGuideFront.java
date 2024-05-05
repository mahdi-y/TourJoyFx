package Controller;

import Entities.Booking;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Services.BookingServices;
import utils.MyDB;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

public class BookGuideFront {
    private String selectedGuideName;

    @FXML
    private ComboBox<String> guideComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button bookButton;

    private BookingServices bookingService;

    public BookGuideFront() {
        // Initialize BookingServices
        bookingService = new BookingServices();
    }

    @FXML
    void initialize() {
        //  guideComboBox.setDisable(true); // Disables the ComboBox

        populateGuideComboBox();
        if (selectedGuideName != null && !selectedGuideName.isEmpty()) {
            selectGuideByName(selectedGuideName);  // Use the new method for selecting the guide
        }
        bookButton.setOnAction(event -> {
            try {
                addBooking();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.isBefore(today.plusDays(1)));
            }
        });
    }


    private void populateGuideComboBox() {
        try {
            // Query to fetch guide names and IDs
            String query = "SELECT CIN, firstname_g, lastname_g FROM Guide";

            // Create statement and execute query
            Statement statement = MyDB.getInstance().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Map to store guide names and IDs
            Map<Integer, String> guideMap = new HashMap<>();

            // Populate the ComboBox with guide names and store IDs in the map
            while (resultSet.next()) {
                String guideName = resultSet.getString("firstname_g") + " " + resultSet.getString("lastname_g");
                int guideId = resultSet.getInt("CIN");
                guideMap.put(guideId, guideName);
                guideComboBox.getItems().add(guideName);
            }

            // Store the guide map as a property of the ComboBox
            guideComboBox.setUserData(guideMap);

            // Close resources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any errors here
        }
    }

    @FXML
    private void addBooking() throws SQLException {
        // Check if the DatePicker is empty
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            // Show an alert if the date is not selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Date Selection Required");
            alert.setHeaderText(null);
            alert.setContentText("Please select a date to proceed with the booking.");
            alert.showAndWait();
            return; // Exit the method early if no date is selected
        }

        // Get selected guide name from ComboBox
        String selectedGuideName = guideComboBox.getValue();

        // Retrieve the guide map from ComboBox user data
        Map<Integer, String> guideMap = (Map<Integer, String>) guideComboBox.getUserData();

        boolean bookingAdded = bookingService.addBooking(selectedGuideName, guideMap, selectedDate);

        if (bookingAdded) {
            // Alert the user that booking was successful
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Added");
            alert.setHeaderText(null);
            alert.setContentText("Booking has been successfully added!");
            alert.showAndWait();

            // Ask if the user wants to pay now or later
            List<String> choices = Arrays.asList("Pay Now", "Pay Later");
            ChoiceDialog<String> dialog = new ChoiceDialog<>("Pay Later", choices);
            dialog.setTitle("Payment");
            dialog.setHeaderText("Payment Options");
            dialog.setContentText("Would you like to pay now or pay later?");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                if (result.get().equals("Pay Now")) {
                    // Navigate to the payment interface
                    goToPaymentInterface();
                } else {
                    // Set the booking status as "Unpaid"
                    setBookingStatusUnpaid();
                    // Close the current stage
                    Stage currentStage = (Stage) guideComboBox.getScene().getWindow();
                    currentStage.close();
                }
            }
        } else {
            // Alert the user if booking failed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Booking Error");
            alert.setHeaderText(null);
            alert.setContentText("Guide already booked on this date!");
            alert.showAndWait();
        }
    }



    private void setBookingStatusUnpaid() throws SQLException {
        // Update the status of the booking to "Unpaid"
        // Get the last added booking or the booking you want to update
        Booking lastBooking = bookingService.getLastBooking(); // Implement this method accordingly

        // Set the status of the booking to "Unpaid"
        lastBooking.setStatus("Unpaid");

        // Update the booking status in the database
        try {
            bookingService.updateBooking(lastBooking);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQL exception appropriately
        }
    }


    private void goToPaymentInterface() {
        try {
            // Load the FXML file for the payment interface
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Payment.fxml"));
            Parent Payment = loader.load();

            // Get the current stage
            Stage currentStage = (Stage) guideComboBox.getScene().getWindow();

            // Create a new scene with the payment interface as the root node
            Scene paymentScene = new Scene(Payment);

            // Set the scene on the current stage
            currentStage.setScene(paymentScene);

            // Show the stage
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the FXML loading exception
            System.err.println("Error loading PaymentInterface.fxml: " + e.getMessage());
        }
    }






    // Close the connection when the controller is destroyed



    public void selectGuideByName(String guideName) {
        for (int i = 0; i < guideComboBox.getItems().size(); i++) {
            if (guideComboBox.getItems().get(i).equalsIgnoreCase(guideName)) {
                guideComboBox.getSelectionModel().select(i);
                break;
            }
        }
    }


}