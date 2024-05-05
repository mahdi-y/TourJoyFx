package Controller;

import Entities.Accomodation;
import Entities.Reservation;
import Services.ServiceAccomodation;
import Services.ServiceResBack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ResBackController {


    @FXML
    private TableView<Reservation> DisplayReservations;

    @FXML
    private TableColumn<Reservation, String> nameRColumn;
    @FXML
    private TableColumn<Reservation, Date> startColumn;
    @FXML
    private TableColumn<Reservation, Date> endColumn;
    @FXML
    private Button deleteRButton;

    @FXML
    private Button manageAccomButton;
    @FXML
    private PieChart accPie;
    private ServiceResBack serviceResBack;



    @FXML
    void initialize() {
        serviceResBack = new ServiceResBack();
        nameRColumn.setCellValueFactory(new PropertyValueFactory<>("accommodationName"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end_date"));


        deleteRButton.setOnAction(event -> deleteRes());

        // Load reservations into the table
        try {
            List<Reservation> listReservations = serviceResBack.Read();
            DisplayReservations.getItems().addAll(listReservations);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch reservations from the database.");
        }

    }



    @FXML
    void deleteRes() {
        Reservation selectedReservation = DisplayReservations.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            try {
                serviceResBack.delete(selectedReservation);
                DisplayReservations.getItems().remove(selectedReservation);
                showAlert(Alert.AlertType.INFORMATION, "Success", "The booking is deleted successfully!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error deleting reservation from the database.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a reservation to delete.");
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void goToAccomManagement(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/javafx/Accomodation.fxml"));
            manageAccomButton.getScene().setRoot(root);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    public void ExportRes() {
        FileChooser fileChooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        // Show save file dialog
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook(); FileOutputStream outputStream = new FileOutputStream(file)) {
                Sheet sheet = workbook.createSheet("Reservations");

                // Create the header row
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Accommodation Name");
                header.createCell(1).setCellValue("Check-in Date");
                header.createCell(2).setCellValue("Check-out Date");

                // Fill data
                int rowIndex = 1;
                for (Reservation res : DisplayReservations.getItems()) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(res.getName());
                    row.createCell(1).setCellValue(res.getStart_date().toString());
                    row.createCell(2).setCellValue(res.getEnd_date().toString());
                }

                // Autosize columns
                for (int i = 0; i < 3; i++) {
                    sheet.autoSizeColumn(i);
                }

                workbook.write(outputStream);
                showAlert(Alert.AlertType.INFORMATION, "Export Successful", "Data exported to Excel file successfully!");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "File Error", "Failed to write to file.");
            }
        }
    }
}
