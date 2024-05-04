package controller;

import Entities.Accomodation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class Favorites {
    @FXML
    private VBox favoritesVBox;

    private ObservableList<Accomodation> favoritesList = FXCollections.observableArrayList();
    private static Favorites instance;

    public Favorites() { }
    // Ensure there are getters and setters if these fields need to be accessed outside directly
    public VBox getFavoritesVBox() {
        return favoritesVBox;
    }

    public void setFavoritesVBox(VBox favoritesVBox) {
        this.favoritesVBox = favoritesVBox;
    }

    // Make sure your FXML loader uses the existing instance

    public void addFavorite(Accomodation accomodation) {
        if (!favoritesList.contains(accomodation)) {
            Platform.runLater(() -> {
                favoritesList.add(accomodation);
                updateFavoritesDisplay(); // Update the display
                System.out.println("Added to favorites: " + accomodation.getName());  // Debugging line
            });
        } else {
            System.out.println("Accomodation already in favorites: " + accomodation.getName());
        }
    }


    @FXML
    public void initialize() {
        System.out.println("Initializing Favorites Controller, instance: " + this);
        if (favoritesVBox == null) {
            System.out.println("favoritesVBox is null at initialization!");
        } else {
            updateFavoritesDisplay();
        }
    }



    private void updateFavoritesDisplay() {
        Platform.runLater(() -> {
            if (favoritesVBox != null) {
                System.out.println("Updating favorites display, items count: " + favoritesList.size());
                favoritesVBox.getChildren().clear();
                for (Accomodation acc : favoritesList) {
                    Label nameLabel = new Label(acc.getName());
                    Button deleteButton = new Button("Delete");
                    deleteButton.setOnAction(event -> removeFavorite(acc));
                    favoritesVBox.getChildren().addAll(nameLabel, deleteButton);
                    System.out.println("Added to VBox: " + acc.getName());
                }
            } else {
                System.out.println("favoritesVBox is null - check FXML bindings and initialization");
            }
        });
    }



    private void removeFavorite(Accomodation accomodation) {
        favoritesList.remove(accomodation);
        updateFavoritesDisplay(); // Update display after removal
    }

    @FXML
    private void clearFavorites() {
        favoritesList.clear();
        updateFavoritesDisplay(); // Clear the entire list and update display
    }
}
