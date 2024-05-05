package controller;

import Services.ServiceAccomodation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import Entities.Images;
import Entities.Accomodation;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class detailsA {

    @FXML private Text nameText, typeText, priceText, roomsText;
    @FXML private TilePane tilePane;

    @FXML private Button bookButton;
    private List<Images> imagesList;
    private Stage previewStage;
    private ImageView previewImageView;

    private ServiceAccomodation serviceAccomodation = new ServiceAccomodation();

    public void initData(Accomodation accomodation) {
        nameText.setText(accomodation.getName());
        typeText.setText(accomodation.getType());
        priceText.setText(String.valueOf(accomodation.getPrice()));
        roomsText.setText(String.valueOf(accomodation.getNb_rooms()));

        loadImages(accomodation.getRefA());
    }

    private void loadImages(int accommodationId) {
        try {
            imagesList = serviceAccomodation.getImagesByAccommodationId(accommodationId);
            tilePane.getChildren().clear();
            for (Images img : imagesList) {
                ImageView imageView = new ImageView(new Image(img.getName()));
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
                imageView.setPreserveRatio(true);
                imageView.setOnMouseClicked(event -> openPreview(imageView));
                tilePane.getChildren().add(imageView);
            }
        } catch (SQLException e) {
            System.out.println("Error loading images: " + e.getMessage());
        }
    }

    private void openPreview(ImageView imageView) {
        if (previewStage == null) {
            previewStage = new Stage();
            previewStage.initModality(Modality.APPLICATION_MODAL);
            previewStage.setTitle("Image Preview");

            previewImageView = new ImageView();
            previewImageView.setFitHeight(600);
            previewImageView.setFitWidth(800);
            previewImageView.setPreserveRatio(true);

            Button nextButton = new Button(">");
            nextButton.setOnAction(e -> navigateImage(1));
            Button prevButton = new Button("<");
            prevButton.setOnAction(e -> navigateImage(-1));

            HBox navigationBox = new HBox(10, prevButton, previewImageView, nextButton);
            previewStage.setScene(new Scene(navigationBox));
        }

        Image image = imageView.getImage();
        previewImageView.setImage(image);
        previewStage.show();
    }

    private void navigateImage(int direction) {
        int currentIndex = imagesList.indexOf(new Images(previewImageView.getImage().getUrl()));
        int newIndex = (currentIndex + direction + imagesList.size()) % imagesList.size();
        Image newImage = new Image(imagesList.get(newIndex).getName());
        previewImageView.setImage(newImage);
    }
    @FXML
    public void BookA(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafx/resForm.fxml"));
            Parent root = loader.load();
            ResController resController = loader.getController();
            resController.initializeWithAccommodationName(nameText.getText());  // Assuming nameText holds the name of the accommodation
            bookButton.getScene().setRoot(root);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String loadError, String s) {
    }


}