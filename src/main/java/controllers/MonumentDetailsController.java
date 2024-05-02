package controllers;

import entities.Monument;
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
        descriptionText.setStyle("-fx-fill: white;"); // Set the text color directly
        nameText.setText(monument.getName());
        typeText.setText(monument.getType());
        priceText.setText(String.valueOf(monument.getEntryPrice()));
        statusText.setText(monument.getStatus());

        // Reset previous texts and add new text
        descriptionFlow.getChildren().clear();
        Text descriptionContent = new Text(monument.getDescription());
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

        URL url = getClass().getResource("/map.html");
        if (url != null) {
            webEngine.load(url.toExternalForm());
        } else {
            System.err.println("Failed to find map.html resource.");
        }
    }
}
