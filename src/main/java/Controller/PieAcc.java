package Controller;

import Entities.Accomodation;
import Services.ServiceAccomodation;
import Services.ServiceReservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PieAcc {

    @FXML
    private PieChart piechart;

    private ServiceAccomodation serviceAccomodation;
    private ServiceReservation serviceReservation;

    @FXML
    public void initialize() {
        serviceAccomodation = new ServiceAccomodation();
        serviceReservation = new ServiceReservation();
        loadPieChartData();
    }

    @FXML
    private void loadPieChartData() {
        try {
            Map<Integer, Integer> bookingCounts = serviceReservation.getBookingCounts();
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            List<Accomodation> accommodations = serviceAccomodation.Read();
            for (Accomodation accomodation : accommodations) {
                Integer bookings = bookingCounts.getOrDefault(accomodation.getRefA(), 0);
                pieChartData.add(new PieChart.Data(accomodation.getName(), bookings));
            }

            piechart.setData(pieChartData);
            piechart.setTitle("Booking Statistics");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch booking statistics.", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
