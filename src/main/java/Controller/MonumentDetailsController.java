package Controller;

import Entities.Monument;
import com.example.javafx.HelloApplication;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


import javax.sound.sampled.AudioInputStream;
import java.net.URL;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MonumentDetailsController {
    public VBox detailsBox;
    public ImageView backgroundImage;
    public Button playDescriptionButton;
    public Text descriptionText;
    @FXML private Text nameText;
    @FXML private Text typeText;
    @FXML private Text priceText;
    @FXML private Text statusText;
    @FXML private TextFlow descriptionFlow;
    @FXML private Text latitudeText;
    @FXML private Text longitudeText;
    @FXML private WebView webView;

    public void initData(Monument monument) {
        descriptionText.setText(monument.getDescription());
        descriptionText.setFill(Color.WHITE); // Set the fill color to white
        descriptionText.setStyle("-fx-text-fill: white; -fx-fill: white; -fx-font-size: 18px;"); // Set text size

        nameText.setText(monument.getName());
        nameText.setStyle("-fx-font-size: 16px;");
        typeText.setText(monument.getType());
        typeText.setStyle("-fx-font-size: 16px;");
        priceText.setText(String.valueOf(monument.getEntryPrice()));
        priceText.setStyle("-fx-font-size: 16px;");
        // statusText.setText(monument.getStatus());
        // statusText.setStyle("-fx-font-size: 16px;");

        // Reset previous texts and add new text with updated style
        descriptionFlow.getChildren().clear();
        Text descriptionContent = new Text(monument.getDescription());
        descriptionContent.setFill(Color.WHITE);
        descriptionContent.setStyle("-fx-text-fill: white; -fx-fill: white; -fx-font-size: 18px;"); // Set text size
        descriptionFlow.getChildren().add(descriptionContent);

        updateMapView(monument.getLatitude(), monument.getLongitude());
    }


    private void updateMapView(double latitude, double longitude) {
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);

        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                webEngine.executeScript("updateMapView(" + latitude + ", " + longitude + ");");
            }
        });

        URL url = getClass().getResource("/com/test/tjv2/map.html");
        if (url != null) {
            webEngine.load(url.toExternalForm());
        } else {
            System.err.println("Failed to find map.html resource.");
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