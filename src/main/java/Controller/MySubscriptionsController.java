package Controller;

import Entities.Subscription;
import Entities.Transport;
import com.example.javafx.HelloApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import utils.MyDB;
import utils.UserSession;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MySubscriptionsController {
    @FXML
    private TableView<Subscription> MySubs;
    public Button ClaimsButton;
    public Button HomeButton;
    public Button Acc;
    public Button SubscriptionButton;
    @FXML
    private Button guideButton;
    @FXML
    private Button homeButton;

    @FXML
    private Button MonumentButton;
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


    public MySubscriptionsController()
    {

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
        URL url = getClass().getResource("/com/example/tourjoy/add-rec.fxml");
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
        URL url = getClass().getResource("/com/test/tjv2/SubscriptionFront.fxml");

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
    private void loadUserSubscriptions() {
        UserSession session = UserSession.getInstance();
        int userId = session.getId();
        try {
            List<Subscription> userSubscriptions = fetchUserSubscriptions(userId);
            MySubs.getItems().setAll(userSubscriptions);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }
    }
    public void initialize() {
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        planColumn.setCellValueFactory(new PropertyValueFactory<>("plan"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        //typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeT().getType_t()));

        // Load user's subscriptions
        loadUserSubscriptions();
    }
    private List<Subscription> fetchUserSubscriptions(int userid) throws SQLException {
        UserSession session = UserSession.getInstance();
        List<Subscription> subscriptions = new ArrayList<>();
        String query = "SELECT s.id, s.plan, s.duration " +
                "FROM subscription s " +
                "JOIN user_subscriptions us ON s.id = us.fk_subscription " +
                "WHERE us.fk_user = ?";

        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, session.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String plan = resultSet.getString("plan");
                    int duration = resultSet.getInt("duration");


                    // Assuming you have a constructor for Subscription that takes id, plan, duration, and type as parameters
                    Subscription subscription = new Subscription(id, plan, duration);
                    subscriptions.add(subscription);
                }
            }
        }
        return subscriptions;
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
