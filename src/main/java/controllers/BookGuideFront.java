package controllers;

import entities.Booking;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import services.BookingServices;
import utils.MyDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
        populateGuideComboBox();
        if (selectedGuideName != null && !selectedGuideName.isEmpty()) {
            selectGuideByName(selectedGuideName);  // Use the new method for selecting the guide
        }
        bookButton.setOnAction(event -> addBooking());
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
    private void addBooking() {
        // Get selected guide name from ComboBox
        String selectedGuideName = guideComboBox.getValue();

        // Retrieve the guide map from ComboBox user data
        Map<Integer, String> guideMap = (Map<Integer, String>) guideComboBox.getUserData();

        // Get selected date from DatePicker
        LocalDate selectedDate = datePicker.getValue();

        boolean bookingAdded = bookingService.addBooking(selectedGuideName, guideMap, selectedDate);

        if (bookingAdded) {
            // Alert the user that booking was successful
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Added");
            alert.setHeaderText(null);
            alert.setContentText("Booking has been successfully added!");
            alert.showAndWait();
        } else {
            // Alert the user if booking failed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Booking Error");
            alert.setHeaderText(null);
            alert.setContentText("Guide already booked on this date!");
            alert.showAndWait();
        }
    }

    // Close the connection when the controller is destroyed
    @FXML
    public void finalize() {
        bookingService.closeConnection();
    }


    public void selectGuideByName(String guideName) {
        for (int i = 0; i < guideComboBox.getItems().size(); i++) {
            if (guideComboBox.getItems().get(i).equalsIgnoreCase(guideName)) {
                guideComboBox.getSelectionModel().select(i);
                break;
            }
        }
    }


}
