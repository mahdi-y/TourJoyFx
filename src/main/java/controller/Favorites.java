package Controller;

import Entities.Accomodation;
import Services.ServiceFavs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import Entities.Favs;
import Services.ServiceAccomodation;
import Entities.Accomodation;
import Services.ServiceFavs;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.sql.SQLException;
import java.util.List;
import java.sql.SQLException;
import java.util.List;

public class Favorites {

    @FXML
    private GridPane favoritesGrid;
    private ServiceFavs serviceFavs = new ServiceFavs();

    @FXML
    public void initialize() {
        displayFavorites();
    }

    private void displayFavorites() {
        try {
            List<Accomodation> favorites = serviceFavs.getAllFavorites();
            favoritesGrid.getChildren().clear();
            int row = 0, col = 0;
            for (Accomodation fav : favorites) {
                ImageView imageView = new ImageView(new Image(fav.getImage_name()));
                imageView.setFitHeight(200);
                imageView.setFitWidth(200);

                Label nameLabel = new Label(fav.getName());
                nameLabel.setStyle("-fx-font-size: 16px;");

                Button deleteButton = new Button("Delete");
                deleteButton.setStyle("-fx-font-size: 14px;-fx-background-color: black; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20px; -fx-padding: 5 10 5 10;");
                deleteButton.setOnAction(event -> deleteFavorite(fav.getRefA()));

                VBox vbox = new VBox(25, imageView, nameLabel, deleteButton);
                vbox.setAlignment(Pos.CENTER);
                vbox.setStyle(" -fx-padding: 10px; ;-fx-border-style: solid inside;-fx-border-width: 2px;-fx-border-insets: 5px; -fx-border-radius: 5px;-fx-border-color: rgb(128,128,128);");

                favoritesGrid.add(vbox, col, row);
                col++;
                if (col == 3) { // Adjust for your number of columns
                    col = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteFavorite(int accRef) {
        try {
            serviceFavs.deleteFavorite(accRef, 1); // Assuming user ID is constant, for example 1
            displayFavorites(); // Refresh the display
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addAccommodationToFavorites(int acc, int user) {
        ServiceFavs serviceFavs = new ServiceFavs(); // Assume you have a service layer for handling favorites
        Favs newFavorite = new Favs(user, acc);
        try {
            serviceFavs.addFavorite(newFavorite);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add to favorites.");
            e.printStackTrace();
        }
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void clearFavorites() {
        try {
            serviceFavs.deleteAllFavorites(); // Call the method in ServiceFavs to delete all favorites
            displayFavorites(); // Refresh the display to show that all favorites have been cleared
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to clear all favorites.");
            e.printStackTrace();
        }
    }





}
