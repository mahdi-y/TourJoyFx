package Controller;

import Entities.Accomodation;
import Services.ServiceAccomodation;
import Services.ServiceReservation;
import com.example.javafx.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PieAcc {

    @FXML
    private PieChart piechart;

    private ServiceAccomodation serviceAccomodation;
    private ServiceReservation serviceReservation;

    @FXML
    private Button backA;
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

    public void backA(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/example/javafx/Accomodation.fxml");
        if (url == null) {
            System.err.println("Cannot find back.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                backA.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }
}
