package Controller;

import Entities.Transport;

import Services.TransportService;

import com.example.javafx.HelloApplication;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class TransportController implements Initializable {

    private TransportService transportService = new TransportService();
    @FXML
    private TextField searchTransportTextField;

   @FXML
    private Button SubscriptionButton;
    @FXML
    private TableView<Transport> transportTable;

    @FXML
    private TableColumn<Transport, Integer> idColumn;

    @FXML
    private TableColumn<Transport, String> typeColumn;

    @FXML
    private TextField typeTextField;
    @FXML
    private Button gotoGuideManagement;
    @FXML
    private Button gotoMonumentManagement;
    @FXML
    private Button frontoffice;


    private void initializeDataTable() {
        Task<Void> updateTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    List<Transport> transports = transportService.getAllTransports();
                    ObservableList<Transport> observableTransports = FXCollections.observableArrayList(transports);
                    Platform.runLater(() -> transportTable.setItems(observableTransports));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread(updateTask).start();
    }
    public void handleRefreshButtonAction(ActionEvent event) {
        initializeDataTable();
    }

    public void handleAddButtonAction() {
        String type = typeTextField.getText().trim();

        // Validate input as string
        if (!isValidString(type)) {
            showAlert("Invalid input. Please enter a valid string.");
            return;
        }

        // Check uniqueness
        try {
            if (transportService.isTransportTypeExists(type)) {
                showAlert("Transport type already exists. Please enter a unique type.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error checking transport type uniqueness.");
            return;
        }

        Transport transport = new Transport(transportTable.getItems().size() + 1, type);
        transportTable.getItems().add(transport);

        try {
            transportService.addTransport(transport);
        } catch (SQLException e){
            e.printStackTrace();
        }

        typeTextField.clear();
    }

    // Method to validate input as string
    private boolean isValidString(String input) {
        return Pattern.matches("[a-zA-Z]+", input);
    }

    // Method to show an alert dialog
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleUpdateButtonAction() {
        Transport selectedTransport = transportTable.getSelectionModel().getSelectedItem();
        if (selectedTransport != null) {
            String newType = typeTextField.getText();
            selectedTransport.setType_t(newType);

            try {
                transportService.updateTransport(selectedTransport);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleDeleteButtonAction() {
        Transport selectedTransport = transportTable.getSelectionModel().getSelectedItem();
        if (selectedTransport != null) {
            transportTable.getItems().remove(selectedTransport);

            try {
                transportService.deleteTransport(selectedTransport.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<Transport> transports = transportService.getAllTransports();
            ObservableList<Transport> observableTransports = FXCollections.observableArrayList(transports);
            transportTable.setItems(observableTransports);

            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type_t"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Add listener to handle selection changes
        transportTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                typeTextField.setText(newSelection.getType_t());

            }
        });
    }


    public void goToSubscription(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafx/Subscription.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) SubscriptionButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void handleSearchButtonAction(ActionEvent event) {
        String keyword = searchTransportTextField.getText();
        try {
            List<Transport> searchResult = transportService.searchTransportsByType(keyword);
            // Clear the table and add the search results
            transportTable.getItems().clear();
            transportTable.getItems().addAll(searchResult);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your application's error handling strategy
        }
    }
    private Stage getPrimaryStage() {
        return HelloApplication.getPrimaryStage();
    }
    public void minimizeWindow(ActionEvent actionEvent) {
        getPrimaryStage().setIconified(true);
    }

    public void expandWindow(ActionEvent actionEvent) {
        Stage stage = getPrimaryStage();
        stage.setMaximized(!stage.isMaximized());
    }
    public void closeWindow(ActionEvent actionEvent) {
        getPrimaryStage().close();
    }

    public void front(ActionEvent actionEvent) throws IOException {
        HelloApplication.loadFXML("/com/test/tjv2/Home.fxml");
    }

    public void gotoGuideManagement(ActionEvent actionEvent) {
        URL url = getClass().getResource("/AddGuide.fxml");
        if (url == null) {
            System.err.println("Cannot find AddGuide.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                gotoGuideManagement.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }
    public void gotoMonumentManagement(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/test/tjv2/Monument.fxml");
        if (url == null) {
            System.err.println("Cannot find Monument.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                gotoMonumentManagement.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }
    public void gotoUserManagment(ActionEvent actionEvent) {
        URL url = getClass().getResource("/usersList.fxml");
        if (url == null) {
            System.err.println("Cannot find Monument.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                frontoffice.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }
    public void goToClaimsManagement(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/example/javafx/back.fxml");
        if (url == null) {
            System.err.println("Cannot find back.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                frontoffice.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }
    public void goToTransport(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/example/javafx/Subscription.fxml");
        if (url == null) {
            System.err.println("Cannot find back.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                frontoffice.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }
    public void goToAccManagement(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/example/javafx/Accomodation.fxml");
        if (url == null) {
            System.err.println("Cannot find back.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                frontoffice.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }
}