package controller;

import Entities.Accomodation;
import Services.ServiceAccomodation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.Button;

public class AccFront {
    @FXML
    public Button AccButton;
    @FXML
    private GridPane AccommodationsGrid;

    private ServiceAccomodation serviceAccomodation;

    @FXML
    public void initialize() {
        serviceAccomodation = new ServiceAccomodation();
        loadAccommodatons();
    }

    private void loadAccommodatons() {
        try {
            List<Accomodation> accomodations = serviceAccomodation.Read();
            int columns = 3;
            int colIndex = 0;
            int rowIndex = 0;

            for (Accomodation accomodation : accomodations) {
                AnchorPane accomodationEntry = createAccomodationEntry(accomodation);
                AccommodationsGrid.add(accomodationEntry, colIndex, rowIndex);
                if (++colIndex >= columns) {
                    colIndex = 0;
                    rowIndex++;
                }
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading accommodations.");
            ex.printStackTrace();
        }
    }

    private AnchorPane createAccomodationEntry(Accomodation accomodation) {
        AnchorPane accomodationCard = new AnchorPane();
        accomodationCard.getStyleClass().add("accommodation-box");
        accomodationCard.setPrefSize(200, 300); // Set the preferred size for the box

        VBox detailsBox = new VBox(5); // Adjust the spacing if necessary
        detailsBox.getStyleClass().add("accommodation-details");
        detailsBox.setPrefHeight(150); // The remaining height of the box after the image
        VBox.setVgrow(detailsBox, Priority.ALWAYS); // This will allow the VBox to expand within the card

        Label nameLabel = new Label(accomodation.getName());
        nameLabel.getStyleClass().add("accommodation-title");
        nameLabel.setMaxWidth(Double.MAX_VALUE); // Ensure the label uses all available horizontal space
        nameLabel.setAlignment(Pos.CENTER); // Center align the text

        Label LocationLabel = new Label(accomodation.getLocation());
        nameLabel.getStyleClass().add("location");
        nameLabel.setMaxWidth(Double.MAX_VALUE); // Ensure the label uses all available horizontal space
        nameLabel.setAlignment(Pos.CENTER); // Center align the text

        Label priceLabel = new Label("Price per night: " + accomodation.getPrice() + " TND");
        priceLabel.setMaxWidth(Double.MAX_VALUE); // Ensure the label uses all available horizontal space
        priceLabel.setAlignment(Pos.CENTER); // Center align the text

        nameLabel.getStyleClass().add("accommodation-title");
        nameLabel.setMaxWidth(Double.MAX_VALUE); // Ensure the label uses all available horizontal space
        nameLabel.setAlignment(Pos.CENTER); // Center align the text

        Button bookButton = new Button("Book Now");
        bookButton.setOnAction(event -> BookA(accomodation));

        detailsBox.getChildren().addAll(nameLabel, priceLabel, bookButton);

        AnchorPane.setTopAnchor(detailsBox, 150.0); // Position the details box directly under the image
        AnchorPane.setLeftAnchor(detailsBox, 0.0);
        AnchorPane.setRightAnchor(detailsBox, 0.0);
        AnchorPane.setBottomAnchor(detailsBox, 0.0);

        accomodationCard.getChildren().addAll(detailsBox);

        return accomodationCard;
    }

    public void BookA(Accomodation accomodation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafx/resForm.fxml")); // Correct path
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "Cannot load the booking form.");
            ex.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
