package Controller;

import Entities.Accomodation;
import Services.ServiceAccomodation;
import Services.ServiceFavs;

import com.example.javafx.HelloApplication;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.*;
import Entities.Favs;
import Controller.Favorites;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import javafx.scene.control.Button;

public class AccFront {
    @FXML
    public Button AccButton;
    @FXML
    private GridPane AccommodationsGrid;
    @FXML
    private TextField searchAcc;
    public Button ClaimsButton;
    public Button HomeButton;
    public Button Acc;
    @FXML
    private Button guideButton;
    @FXML
    private Button homeButton;

    @FXML
    private Button SubscriptionButton;
    @FXML
    private Button MonumentButton;
    @FXML private CheckBox checkboxPrice1;
    @FXML private CheckBox checkboxPrice2;
    @FXML private CheckBox checkboxPrice3;
    @FXML private CheckBox checkboxTypeVilla;
    @FXML private CheckBox checkboxTypeGuesthouse;
    @FXML private CheckBox checkboxTypeApartment;
    @FXML private CheckBox checkboxTypeFarmHouse;
    @FXML private CheckBox checkboxTypePrivateHouse;
    @FXML private CheckBox nb0;
    @FXML private CheckBox nb1;
    @FXML private CheckBox nb2;
    @FXML private CheckBox nb3;
    @FXML private CheckBox nb4;
    @FXML private CheckBox nb5;
    @FXML private CheckBox nb6;
    @FXML
    public Button filter;

    private ServiceAccomodation serviceAccomodation;
    private Favorites favsController;

    @FXML
    public void initialize() {
        serviceAccomodation = new ServiceAccomodation();
        favsController = new Favorites();
        loadAccommodatons();
    }

    @FXML
    void searchA() throws SQLException {
        // Get the user's search criteria
        String searchText = searchAcc.getText().toLowerCase();

        // Filter the list of patients based on the search criteria
        List<Accomodation> accomodations = serviceAccomodation.Read(); // Get all patients
        List<Accomodation> filtererv = new ArrayList<>();


        for (Accomodation acc : accomodations) {
            boolean matchesName = acc.getName().toLowerCase().contains(searchText);
            boolean matchesType = acc.getType().toLowerCase().contains(searchText);
            boolean matchesLoc = acc.getLocation().toLowerCase().contains(searchText);


            // If any of the fields match the search criteria, add the patient to the filtered list
            if (matchesName|| matchesType || matchesLoc  ) {
                filtererv.add(acc);
            }
        }

        // Clear existing accommodations from the GridPane
        AccommodationsGrid.getChildren().clear();

// Variables to manage grid positioning
        int columns = 3;
        int colIndex = 0;
        int rowIndex = 0;

// Repopulate the GridPane with the filtered accommodations
        for (Accomodation accomodation : filtererv) {
            AnchorPane accomodationEntry = createAccomodationEntry(accomodation);
            AccommodationsGrid.add(accomodationEntry, colIndex, rowIndex);
            if (++colIndex >= columns) {
                colIndex = 0;
                rowIndex++;
            }
        }

    }


    @FXML
    public void filterButton(ActionEvent event) throws SQLException {
        boolean filterPrice1 = checkboxPrice1.isSelected();
        boolean filterPrice2 = checkboxPrice2.isSelected();
        boolean filterPrice3 = checkboxPrice3.isSelected();
        boolean filterT1 = checkboxTypeVilla.isSelected();
        boolean filterT2 = checkboxTypeApartment.isSelected();
        boolean filterT3 = checkboxTypeFarmHouse.isSelected();
        boolean filterT4 = checkboxTypeGuesthouse.isSelected();
        boolean filterT5 = checkboxTypePrivateHouse.isSelected();
        boolean filterR0 = nb0.isSelected();
        boolean filterR1 = nb1.isSelected();
        boolean filterR2 = nb2.isSelected();
        boolean filterR3 = nb3.isSelected();
        boolean filterR4 = nb4.isSelected();
        boolean filterR5 = nb5.isSelected();
        boolean filterR6 = nb6.isSelected();


        // Now apply these filters to your accommodations list
        List<Accomodation> filteredAccommodations = filterAccommodations();
        // Display the filtered list
        displayFilteredAccommodations(filteredAccommodations);
    }

    private List<Accomodation> filterAccommodations() throws SQLException {
        // Get all accommodations
        List<Accomodation> accommodations = serviceAccomodation.Read(); // Replace with your method to fetch accommodations
        List<Accomodation> filteredList = new ArrayList<>();
        for (Accomodation acc : accommodations) {
            // Add your filtering logic here based on the state of checkboxes
            // For example, if checkboxPrice1 is selected, add accommodations within that price range
            // Similarly check for type and room number

            if (shouldIncludeAccommodation(acc)) {
                filteredList.add(acc);
            }
        }
        return filteredList;
    }

    private boolean shouldIncludeAccommodation(Accomodation acc) {
        // Assume all accommodations are included unless a filter restricts it
        boolean includeByPrice = !checkboxPrice1.isSelected() && !checkboxPrice2.isSelected() && !checkboxPrice3.isSelected();
        boolean includeByType = !checkboxTypeVilla.isSelected() && !checkboxTypeGuesthouse.isSelected() && !checkboxTypeApartment.isSelected() && !checkboxTypeFarmHouse.isSelected() && !checkboxTypePrivateHouse.isSelected();
        boolean includeByRoomNumber = !nb0.isSelected() && !nb1.isSelected() && !nb2.isSelected() && !nb3.isSelected() && !nb4.isSelected() && !nb5.isSelected() && !nb6.isSelected();

        // Apply price filters if selected
        if (checkboxPrice1.isSelected() && acc.getPrice() >= 100 && acc.getPrice() <= 500) includeByPrice = true;
        if (checkboxPrice2.isSelected() && acc.getPrice() > 500 && acc.getPrice() <= 1000) includeByPrice = true;
        if (checkboxPrice3.isSelected() && acc.getPrice() > 1000) includeByPrice = true;

        // Apply type filters if selected
        if (checkboxTypeVilla.isSelected() && "Villa".equals(acc.getType())) includeByType = true;
        if (checkboxTypeGuesthouse.isSelected() && "Guesthouse".equals(acc.getType())) includeByType = true;
        if (checkboxTypeApartment.isSelected() && "Apartment".equals(acc.getType())) includeByType = true;
        if (checkboxTypeFarmHouse.isSelected() && "Farm house".equals(acc.getType())) includeByType = true;
        if (checkboxTypePrivateHouse.isSelected() && "Private house".equals(acc.getType())) includeByType = true;

        // Apply room number filters if selected
        if (nb0.isSelected() && acc.getNb_rooms() == 0) includeByRoomNumber = true;
        if (nb1.isSelected() && acc.getNb_rooms() == 1) includeByRoomNumber = true;
        if (nb2.isSelected() && acc.getNb_rooms() == 2) includeByRoomNumber = true;
        if (nb3.isSelected() && acc.getNb_rooms() == 3) includeByRoomNumber = true;
        if (nb4.isSelected() && acc.getNb_rooms() == 4) includeByRoomNumber = true;
        if (nb5.isSelected() && acc.getNb_rooms() == 5) includeByRoomNumber = true;
        if (nb6.isSelected() && acc.getNb_rooms() == 6) includeByRoomNumber = true;

        // Only include the accommodation if it meets all active filter criteria
        return includeByPrice && includeByType && includeByRoomNumber;
    }



    private void displayFilteredAccommodations(List<Accomodation> accommodations) {
        // Clear the existing accommodations from the GridPane
        AccommodationsGrid.getChildren().clear();

        // Add the filtered accommodations to the GridPane
        int columns = 3;
        int colIndex = 0;
        int rowIndex = 0;
        for (Accomodation accomodation : accommodations) {
            AnchorPane accomodationEntry = createAccomodationEntry(accomodation);
            AccommodationsGrid.add(accomodationEntry, colIndex, rowIndex);
            if (++colIndex >= columns) {
                colIndex = 0;
                rowIndex++;
            }
        }
    }

    private void loadAccommodatons() {
        try {
            List<Accomodation> accomodations = serviceAccomodation.Read();
            int columns = 5;
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

        ImageView imageView = loadImage(accomodation.getImage_name());
        imageView.getStyleClass().add("accommodation-image");
        imageView.setFitHeight(150); // Set a fixed height for the image
        imageView.setFitWidth(200);  // Set a fixed width for the image
        imageView.setPreserveRatio(true);

        VBox detailsBox = new VBox(5); // Adjust the spacing if necessary
        detailsBox.getStyleClass().add("accommodation-details");
        detailsBox.setPrefHeight(150); // The remaining height of the box after the image
        VBox.setVgrow(detailsBox, Priority.ALWAYS); // This will allow the VBox to expand within the card

        Label nameLabel = new Label(accomodation.getName());
        nameLabel.getStyleClass().add("accommodation-title");
        nameLabel.setMaxWidth(Double.MAX_VALUE); // Ensure the label uses all available horizontal space
        nameLabel.setAlignment(Pos.CENTER); // Center align the text








        nameLabel.getStyleClass().add("accommodation-title");
        nameLabel.setMaxWidth(Double.MAX_VALUE); // Ensure the label uses all available horizontal space
        nameLabel.setAlignment(Pos.CENTER); // Center align the text


        Button detailsA = createDetailsButton(accomodation);
        detailsA.getStyleClass().add("accommodation-button");
        styleButton(detailsA);

        Button favoriteButton = createFavoriteButton(accomodation);
        favoriteButton.getStyleClass().add("accommodation-button");
        styleButton(detailsA);

        HBox buttonBox = new HBox(10);  // Spacing between buttons
        buttonBox.getChildren().addAll(detailsA, favoriteButton);
        buttonBox.setAlignment(Pos.CENTER);


        detailsBox.getChildren().addAll(nameLabel,buttonBox);
        AnchorPane.setTopAnchor(imageView, 0.0);
        AnchorPane.setLeftAnchor(imageView, 0.0);
        AnchorPane.setRightAnchor(imageView, 0.0);
        AnchorPane.setTopAnchor(detailsBox, 150.0); // Position the details box directly under the image
        AnchorPane.setLeftAnchor(detailsBox, 0.0);
        AnchorPane.setRightAnchor(detailsBox, 0.0);
        AnchorPane.setBottomAnchor(detailsBox, 0.0);

        accomodationCard.getChildren().addAll(imageView,detailsBox);

        return accomodationCard;
    }
    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20px;");
    }
    private Button createFavoriteButton(Accomodation accomodation) {
        Button favoriteButton = new Button("\u2764");
        favoriteButton.setOnAction(event -> {
            favsController.addAccommodationToFavorites(accomodation.getRefA(), 1);  // Use the instance of FavsController
        });
        styleButton(favoriteButton);
        return favoriteButton;
    }



    private ImageView loadImage(String image_name) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(false);

        Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        imageView.setClip(clip);

        imageView.setSmooth(true);
        imageView.setCache(true);

        Image image = null;
        try {
            if (image_name.startsWith("http://") || image_name.startsWith("https://") || image_name.startsWith("file:/")) {
                image = new Image(image_name, imageView.getFitWidth(), imageView.getFitHeight(), false, true);
            } else {
                // Attempt to load from the classpath
                InputStream is = getClass().getResourceAsStream(image_name.startsWith("/") ? image_name : "/" + image_name);
                if (is == null) {
                    // If resource stream is null, load default image
                    is = getClass().getResourceAsStream("/images/no_image_found.png");
                }
                image = new Image(is, imageView.getFitWidth(), imageView.getFitHeight(), false, true);
            }
        } catch (Exception e) {
            // Fallback to default image in any exception
            InputStream is = getClass().getResourceAsStream("/images/no_image_found.png");
            image = new Image(is, imageView.getFitWidth(), imageView.getFitHeight(), false, true);
        }

        imageView.setImage(image);
        return imageView;
    }





    private Button createDetailsButton(Accomodation accomodation) {
        Button detailsButton = new Button("Explore !");
        detailsButton.setOnAction(event -> showAccomodationDetails(accomodation));
        return detailsButton;
    }

    private void showAccomodationDetails(Accomodation accomodation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafx/AccDetails.fxml"));
            // The root of the AccDetails.fxml is now a StackPane, so cast it correctly here.
            StackPane dialogContent = loader.load();
            detailsA detailsController = loader.getController();
            detailsController.initData(accomodation);

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Accommodation Details");
            // Create a scene with the StackPane as the root.
            dialogStage.setScene(new Scene(dialogContent));
            dialogStage.showAndWait();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load accommodation details view.");
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
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
    public void goToSubscriptions(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/test/tjv2/SubscriptionFront.fxml");
        if (url == null) {
            System.err.println("Cannot find SubscriptionFront.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                SubscriptionButton.getScene().setRoot(root);
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
