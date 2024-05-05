package Controller;

import Entities.Accomodation;
import Entities.Reservation;
import Services.ServiceReservation;
import Services.ServiceAccomodation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import javax.xml.namespace.QName;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Date;
import java.util.Map;
import java.sql.Date;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.*;

public class ResController implements Initializable{



    @FXML
    private DatePicker startPicker;
    @FXML
    private DatePicker endPicker;
    @FXML
    private ComboBox<String> NameComboBox;
    @FXML
    private Button bookButton;
    @FXML
    private Button weatherButton;


    private ServiceReservation serviceReservation;
    private ServiceAccomodation serviceAccomodation;
    private Map<String, Integer> accommodationNameIdMap;
    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            serviceReservation = new ServiceReservation();
            serviceAccomodation = new ServiceAccomodation();
            loadAccommodations();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error initializing database connection: " + e.getMessage());
            return;  // Exit if there's an error in initial setup
        }
        bookButton.setOnAction(event -> BookAccommodation());


        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();

        // Initialize the calendar after setting up accommodations
        NameComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            // Redraw the calendar whenever a new accommodation is selected
            if (newValue != null) {
                drawCalendar();
            }
        });

        // Draw the calendar for the first time
        drawCalendar();
    }


    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }
    private void loadAccommodations() throws SQLException {
        accommodationNameIdMap = serviceAccomodation.getAccommodationNameIdMap();
        NameComboBox.getItems().addAll(accommodationNameIdMap.keySet());
    }



    @FXML
    void BookAccommodation() {
        try {

            // Get start and end dates from DatePickers
            Date start_date = Date.valueOf(startPicker.getValue());
            Date end_date = Date.valueOf(endPicker.getValue());

            // Get the name of the selected accommodation
            String selectedAccommodationName = NameComboBox.getValue();
            Integer name = accommodationNameIdMap.get(selectedAccommodationName);
            //Validate Fields
            if (name == null) {
                showAlert(Alert.AlertType.ERROR, "Invalid Selection", "Please select a valid accommodation.");
                return;
            }
            if (start_date == null || end_date == null) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please select both start and end dates.");
                return;
            }
            Date today = new Date(System.currentTimeMillis());
            if (start_date.before(today)) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Check-in must be after today.");
                return;
            }
            if (end_date.before(start_date)) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Check-out must be after Check-in.");
                return;
            }
            // Create reservation object with only the name
            Reservation reservation = new Reservation( start_date, end_date, name);

            // Add reservation to database
            serviceReservation.add(reservation);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Reservation added successfully!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid ID.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding reservation to the database.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(dateFocus.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double dayCellWidth = (calendarWidth - calendar.getHgap() * 6) / 7;
        double dayCellHeight = (calendarHeight - calendar.getVgap() * 5) / 6;

        calendar.getChildren().clear();

        LocalDate firstOfMonth = LocalDate.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1);
        int daysToPrepend = firstOfMonth.getDayOfWeek().getValue() % 7;
        daysToPrepend = (daysToPrepend + 6) % 7; // Adjust to start from Sunday (if needed)

        // Add empty cells for days before the first of the month
        for (int i = 0; i < daysToPrepend; i++) {
            calendar.getChildren().add(createEmptyDayCell(dayCellWidth, dayCellHeight));
        }

        LocalDate currentDate = LocalDate.now();
        int daysInMonth = YearMonth.from(dateFocus).lengthOfMonth();
        List<LocalDate> bookedDates = new ArrayList<>();
        try {
            if (NameComboBox.getValue() != null) {
                bookedDates = serviceReservation.getBookedDates(accommodationNameIdMap.get(NameComboBox.getValue()));
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load booked dates.");
        }

        // Create cells for each day of the month
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate cellDate = LocalDate.of(dateFocus.getYear(), dateFocus.getMonthValue(), day);
            StackPane dayCell = createDayCell(dayCellWidth, dayCellHeight, day, cellDate, currentDate, bookedDates);
            calendar.getChildren().add(dayCell);
        }
    }

    private StackPane createEmptyDayCell(double width, double height) {
        StackPane cell = new StackPane();
        Rectangle rect = new Rectangle(width, height);
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(Color.BLACK);
        cell.getChildren().add(rect);
        return cell;
    }

    private StackPane createDayCell(double width, double height, int day, LocalDate cellDate, LocalDate currentDate, List<LocalDate> bookedDates) {
        StackPane dayCell = new StackPane();
        Text dayText = new Text(String.valueOf(day));
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);

        if (cellDate.equals(currentDate)) {
            rectangle.setFill(Color.LIGHTBLUE);
            dayText.setFill(Color.RED);
        } else if (bookedDates.contains(cellDate)) {
            rectangle.setFill(Color.web("#f69697"));  // Highlight booked dates
            dayText.setFill(Color.WHITE);
        }

        dayCell.getChildren().addAll(rectangle, dayText);
        StackPane.setAlignment(dayText, Pos.TOP_RIGHT);
        StackPane.setMargin(dayText, new Insets(5, 10, 0, 0));
        return dayCell;
    }


    public void initializeWithAccommodationName(String accommodationName) {
        if (accommodationName != null && !accommodationName.isEmpty()) {
            NameComboBox.getItems().clear(); // Clear existing items if any
            NameComboBox.getItems().add(accommodationName); // Add the passed accommodation name
            NameComboBox.getSelectionModel().select(accommodationName); // Select the passed name
        }
        initialize(); // Continue with other initializations
    }

    private void initialize() {
    }

    public void seeW(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/javafx/weather.fxml"));
            weatherButton.getScene().setRoot(root);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
