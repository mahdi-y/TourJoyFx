package controller;

import Entities.Accomodation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections; // if you're using FXCollections.observableArrayList()
import java.awt.*;

public class Favorites {
    @FXML
    private VBox favoritesVBox; // This VBox will be used to display favorites

    private static ObservableList<Accomodation> favoritesList = FXCollections.observableArrayList();

    public static void addFavorite(Accomodation accomodation) {
        if (!favoritesList.contains(accomodation)) {
            favoritesList.add(accomodation);
        }
    }

    @FXML
    public void initialize() {
        displayFavorites();
    }

    private void displayFavorites() {
        favoritesVBox.getChildren().clear();
        for (Accomodation acc : favoritesList) {
            Label nameLabel = new Label(acc.getName());
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(event -> removeFavorite(acc));
            favoritesVBox.getChildren().addAll(nameLabel, deleteButton);
        }
    }

    private void removeFavorite(Accomodation accomodation) {
        favoritesList.remove(accomodation);
        displayFavorites();
    }

    @FXML
    private void clearFavorites() {
        favoritesList.clear();
        displayFavorites();
    }
}
