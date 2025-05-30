package Controller;

import Entities.Guide;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import Services.BookingServices;
import Entities.Booking;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class guideStatisticsController {
    @FXML
    private PieChart bookingsPieChart;

    private BookingServices bookingServices = new BookingServices();
    @FXML
    private Button gobackG;

    @FXML
    public void initialize() {
        try {
            loadBookingData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBookingData() throws SQLException {
        List<Booking> bookings = bookingServices.Read(); // Ensure this method exists and is correct
        Map<String, Integer> guideBookingsCount = new HashMap<>();

        for (Booking booking : bookings) {
            Guide guide = bookingServices.fetchGuideById(booking.getGuide()); // Ensure this method correctly fetches a Guide object
            if (guide != null) {
                String fullName = guide.getFirstname_g() + " " + guide.getLastname_g();
                guideBookingsCount.merge(fullName, 1, Integer::sum); // Correct usage for String keys and Integer values
            }
        }

        updatePieChart(guideBookingsCount);
    }
    private void updatePieChart(Map<String, Integer> data) {
        bookingsPieChart.getData().clear();  // Clear existing data
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            // Combine the guide's full name and booking count for the label
            String fullName = entry.getKey(); // Guide's full name
            Integer bookingCount = entry.getValue(); // Number of bookings
            String label = fullName + " (" + bookingCount + " bookings)";

            PieChart.Data slice = new PieChart.Data(label, bookingCount);

            // Set the name for tooltip to include both guide's name and booking count
            slice.setName(label);  // This sets the tooltip text

            bookingsPieChart.getData().add(slice);
        }
    }


    public void goBackG(ActionEvent actionEvent) {
        URL url = getClass().getResource("/AddGuide.fxml");
        if (url == null) {
            System.err.println("Cannot find stats.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                gobackG.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }
}
