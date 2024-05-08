package Controller;

import Entities.Subscription;
import Entities.Transport;
import Services.SubscriptionService;
import Services.TransportService;
import com.example.javafx.HelloApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import Services.SubscriptionService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class SubscriptionFrontController implements Initializable {
    private TransportService transportService;
    private SubscriptionService subscriptionService;
    public SubscriptionFrontController() {
        transportService = new TransportService();

        subscriptionService = new SubscriptionService();
    }
    @FXML
    private TableView<Subscription> subscriptionTable;
    public Button ClaimsButton;
    public Button HomeButton;
    public Button Acc;
    public Button SubscriptionButton;
    @FXML
    private Button guideButton;
    @FXML
    private TextField idTextField;
    @FXML
    private Button homeButton;

    @FXML
    private Button MonumentButton;
    private Subscription selectedSubscription;

    @FXML
    private Button MySubscriptions;
    private Map<String, Integer> typeToIdMap;
    @FXML
    private TableColumn<Subscription, Long> idColumn;


    @FXML
    private TableColumn<Subscription, String> planColumn;

    @FXML
    private TableColumn<Subscription, Integer> durationColumn;

    @FXML
    private TableColumn<Subscription, String> typeColumn;
    private MySubscriptionsController mySubscriptionsController;

    // Inject the MySubscriptionsController
    public void setMySubscriptionsController(MySubscriptionsController controller) {
        this.mySubscriptionsController = controller;
    }
    public void goToMonuments(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/test/tjv2/MonumentFront.fxml");
        if (url == null) {
            System.err.println("Cannot find MonumentDetails.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                MonumentButton.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    public void goToClaims(ActionEvent actionEvent){
        // Correct the package path in the getResource method
        URL url = getClass().getResource("/com/example/javafx/add-rec.fxml");
        if (url == null) {
            System.err.println("Cannot find add-rec.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                ClaimsButton.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }


    public void goToGuides(ActionEvent actionEvent) {
        URL url = getClass().getResource("/guidesFront.fxml");
        if (url == null) {
            System.err.println("Cannot find Guides.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                guideButton.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }



    public void goToHome(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/test/tjv2/Home.fxml");
        if (url == null) {
            System.err.println("Cannot find Home.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                guideButton.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }



    public void goToAcc(ActionEvent actionEvent) {
        // Get the URL of the FXML file
        URL url = getClass().getResource("/com/example/javafx/accFront.fxml");

        // Check if the URL is null, indicating that the resource was not found
        if (url == null) {
            System.err.println("Cannot find accFront.fxml");
        } else {
            try {
                // Load the FXML file into a Parent node
                Parent root = FXMLLoader.load(url);

                // Set the loaded FXML file as the root scene
                Acc.getScene().setRoot(root);
            } catch (IOException ex) {
                // Handle any IOException that occurs during loading
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    public void goToSubscriptionFront(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/example/javafx/SusbcriptionFront.fxml");

        // Check if the URL is null, indicating that the resource was not found
        if (url == null) {
            System.err.println("Cannot find SubscriptionFront.fxml");
        } else {
            try {
                // Load the FXML file into a Parent node
                Parent root = FXMLLoader.load(url);

                // Set the loaded FXML file as the root scene
                SubscriptionButton.getScene().setRoot(root);
            } catch (IOException ex) {
                // Handle any IOException that occurs during loading
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }

    }
    private void initializeDataTable() {
        try {
            List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
            subscriptionTable.getItems().setAll(subscriptions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        subscriptionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedSubscription = newSelection;
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //initializeTTS();
        List<Transport> transports;
        try {
            transports = transportService.getAllTransports();
            if (transports != null) {
                ObservableList<String> typeList = FXCollections.observableArrayList();
                typeToIdMap = new HashMap<>();
                for (Transport transport : transports) {
                    typeList.add(transport.getType_t());
                    typeToIdMap.put(transport.getType_t(), transport.getId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        planColumn.setCellValueFactory(new PropertyValueFactory<>("plan"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        typeColumn.setCellValueFactory(cellData -> {
            Transport transport = cellData.getValue().getTypeT();
            return transport == null ? null : new SimpleStringProperty(transport.getType_t());
        });

        initializeDataTable();



    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.showAndWait();
    }


    public void MySubscriptions(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/test/tjv2/MySubscriptions.fxml");

        // Check if the URL is null, indicating that the resource was not found
        if (url == null) {
            System.err.println("Cannot find SubscriptionFront.fxml");
        } else {
            try {
                // Load the FXML file into a Parent node
                Parent root = FXMLLoader.load(url);

                // Set the loaded FXML file as the root scene
                MySubscriptions.getScene().setRoot(root);
            } catch (IOException ex) {
                // Handle any IOException that occurs during loading
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }

    }

    @FXML
    private void HandleSubscribeButton(ActionEvent event) {
        Subscription selectedSubscription = subscriptionTable.getSelectionModel().getSelectedItem();
        if (selectedSubscription != null) {
            int subscriptionId = Math.toIntExact(selectedSubscription.getId());
            try {
                subscriptionService.subskrayb(subscriptionId);
            } catch (SQLException e) {
                // Handle SQL exception
                e.printStackTrace();
            }
        } else {
            // Handle case when no subscription is selected
        }
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

}
