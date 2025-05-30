package Controller;

import com.example.javafx.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import Entities.Guide;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.RangeSlider;
import Services.GuideServices;
import javafx.geometry.Insets;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuidesFront {
    @FXML
    private Pagination pagination;
    @FXML
    private Button guideButton;
    @FXML
    private RangeSlider priceRangeSlider;
    @FXML
    private Button homeButton;
    @FXML
    private Button MonumentButton;
    @FXML
    private TextField minPriceField;
    public Button ClaimsButton;
    public Button HomeButton;
    public Button Acc;
@FXML
private Button SubscriptionButton;



    @FXML
    private TextField maxPriceField;


    private TableView<Guide> ListGuides;
    private Map<Integer, String> countryMap = new HashMap<>();
    @FXML
    private DatePicker datePickerAvailable;

    @FXML
    private CheckBox maleCheckBox;

    @FXML
    private CheckBox femaleCheckBox;

    @FXML
    private CheckBox rating1CheckBox;

    @FXML
    private CheckBox rating2CheckBox;

    @FXML
    private CheckBox rating3CheckBox;

    @FXML
    private CheckBox rating4CheckBox;
    @FXML
    private Button feedbackButton;
    @FXML
    private VBox rootVBox;
    @FXML
    private TextField searchField;
    private GuideServices guideServices = new GuideServices();
    private ObservableList<Guide> masterData = FXCollections.observableArrayList();
    private FilteredList<Guide> filteredData;
    private static final int rowsPerPage = 1;  // You want two rows per page
    private static final int columnsPerPage = 3;
    private static final int guidesPerPage = rowsPerPage * columnsPerPage;  // Total guides per page
    private Map<Integer, Double> averageRatingsCache = new HashMap<>();

    public void initialize() throws SQLException {
        loadGuideData();
        setupSearchFilter();
        updatePagination();
        priceRangeSlider.setMin(0);
        priceRangeSlider.setMax(1000);
        priceRangeSlider.setLowValue(0); // Initial minimum value
        priceRangeSlider.setHighValue(1000); // Initial maximum value

        // Add listener for changes in the price range
        priceRangeSlider.lowValueProperty().addListener((observable, oldValue, newValue) -> {
            // Handle changes in the minimum value
            System.out.println("Min Price: " + newValue);
            updatePriceFilter();
        });

        priceRangeSlider.highValueProperty().addListener((observable, oldValue, newValue) -> {
            // Handle changes in the maximum value
            System.out.println("Max Price: " + newValue);
            updatePriceFilter();
        });

        // Set the initial page factory with all guides before any search is performed
        pagination.setPageFactory(pageIndex -> createPage(pageIndex));
        setupListeners(); // This method will set up listeners for checkboxes and sliders
        pagination.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) { // newScene is null when the pagination is removed from the scene
                newScene.windowProperty().addListener((obs2, oldWindow, newWindow) -> {
                    if (newWindow != null) { // newWindow is null when scene is detached from the window
                        Stage stage = (Stage) newWindow;
                        stage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {
                            adjustLayout(newValue);
                        });
                    }
                });
            }
        });
    }

    private void adjustLayout(boolean isFullScreen) {
        if (isFullScreen) {
            // Adjust layout for full screen
            pagination.setTranslateY(-100); // Example adjustment
        } else {
            // Reset layout for windowed mode
            pagination.setTranslateY(0);
        }
    }


    private void setupListeners() {
        maleCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateFilters());
        femaleCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateFilters());
        rating1CheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateFilters());
        rating2CheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateFilters());
        rating3CheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateFilters());
        rating4CheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateFilters());
    }


    private void loadGuideData() throws SQLException {
        masterData.clear();
        List<Guide> guides = guideServices.Read();
        masterData.addAll(guides);
        filteredData = new FilteredList<>(masterData, p -> true);
        loadAverageRatings(); // Load ratings after guides are fetched
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(guide -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Show all guides when the search field is empty
                }
                String lowerCaseFilter = newValue.toLowerCase();

                // Check if guide name (first name and last name combined) matches the search text
                String fullName = guide.getFirstname_g().toLowerCase() + " " + guide.getLastname_g().toLowerCase();
                if (fullName.contains(lowerCaseFilter)) {
                    return true;
                }

                // Check if the guide's language or country matches the search text
                if (guide.getLanguage().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Language matches the search text
                }

                // Check if the price matches the search text
                // Assume price is stored as a double, convert to string to check if it contains the search text
                if (String.valueOf(guide.getPrice()).contains(lowerCaseFilter)) {
                    return true;
                }

                return false; // Does not match any criteria
            });
            updatePagination();
        });



        // Apply filter when the "Apply" button is clicked

    }

    private void updatePriceFilter() {
        filteredData.setPredicate(guide -> {
            double minPrice = priceRangeSlider.getLowValue();
            double maxPrice = priceRangeSlider.getHighValue();

            // Check if guide's price falls within the specified range
            return guide.getPrice() >= minPrice && guide.getPrice() <= maxPrice;
        });
        updatePagination();
    }

    private boolean checkGender(Guide guide) {
        boolean maleSelected = maleCheckBox.isSelected();
        boolean femaleSelected = femaleCheckBox.isSelected();

        // If both checkboxes are not selected, show all. If selected, match the gender.
        if (!maleSelected && !femaleSelected) {
            return true;  // If neither is selected, don't filter out anything.
        }

        boolean isMale = guide.getGender_g().equalsIgnoreCase("Male");
        boolean isFemale = guide.getGender_g().equalsIgnoreCase("Female");

        return (maleSelected && isMale) || (femaleSelected && isFemale);
    }
    private boolean checkRating(Guide guide) {
        double averageRate = averageRatingsCache.getOrDefault(guide.getCIN(), 0.0);

        boolean rating1Selected = rating1CheckBox.isSelected() && (averageRate >= 0 && averageRate <= 1.5);
        boolean rating2Selected = rating2CheckBox.isSelected() && (averageRate > 1.5 && averageRate <= 3);
        boolean rating3Selected = rating3CheckBox.isSelected() && (averageRate > 3 && averageRate <= 4.5);
        boolean rating4Selected = rating4CheckBox.isSelected() && (averageRate > 4.5 && averageRate <= 5);

        return rating1Selected || rating2Selected || rating3Selected || rating4Selected || (!rating1CheckBox.isSelected() && !rating2CheckBox.isSelected() && !rating3CheckBox.isSelected() && !rating4CheckBox.isSelected());
    }

    private void updateFilters() {
        filteredData.setPredicate(guide -> {
            return checkGender(guide) && checkRating(guide);
        });
        updatePagination();
    }




    private void updatePagination() {
        int totalPages = (int) Math.ceil((double) filteredData.size() / guidesPerPage);
        pagination.setPageCount(totalPages);
        pagination.setPageFactory(this::createPage);
    }





    private Node createPage(int pageIndex) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(100, 10, 0, 150));

        int pageOffset = pageIndex * guidesPerPage;
        int pageEndIndex = Math.min(pageOffset + guidesPerPage, filteredData.size());
        List<Guide> pageGuides = filteredData.subList(pageOffset, pageEndIndex);

        for (int i = 0; i < pageGuides.size(); i++) {
            Guide guide = pageGuides.get(i);
            grid.add(createGuideBox(guide), i % columnsPerPage, i / columnsPerPage);
        }

        return grid;
    }
    private void loadAverageRatings() {
        for (Guide guide : masterData) {
            try {
                double averageRating = guideServices.calculateAverageRate(guide.getCIN());
                averageRatingsCache.put(guide.getCIN(), averageRating);
            } catch (SQLException e) {
                e.printStackTrace();
                averageRatingsCache.put(guide.getCIN(), 0.0); // Use 0.0 or appropriate fallback if the rating can't be loaded
            }
        }
    }



    private AnchorPane createGuideBox(Guide guide) {
        AnchorPane guideCard = new AnchorPane();
        guideCard.getStyleClass().add("guide-box");
        guideCard.setPrefSize(250, 350); // Adjust size as needed

        // Load image with predefined dimensions and clipping
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);  // Width of the image
        imageView.setFitHeight(150); // Height of the image
        imageView.setPreserveRatio(false); // Not preserving the ratio
        imageView.setSmooth(true);
        imageView.setCache(true);

        // Clipping to shape
        javafx.scene.shape.Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        imageView.setClip(clip);
        // Set the image source
        Image image = null;
        try {
            if (guide.getImage().startsWith("http://") || guide.getImage().startsWith("https://") || guide.getImage().startsWith("file:/")) {
                image = new Image(guide.getImage(), imageView.getFitWidth(), imageView.getFitHeight(), false, true);
            } else {
                // Attempt to load from the classpath
                InputStream is = getClass().getResourceAsStream(guide.getImage().startsWith("/") ? guide.getImage() : "/" + guide.getImage());
                if (is == null) {
                    is = getClass().getResourceAsStream("/image/image.png");
                }
                image = new Image(is, imageView.getFitWidth(), imageView.getFitHeight(), false, true);
            }
        } catch (Exception e) {
            InputStream is = getClass().getResourceAsStream("/image/image.png");
            image = new Image(is, imageView.getFitWidth(), imageView.getFitHeight(), false, true);
        }
        imageView.setImage(image);

        VBox detailsBox = new VBox(10);
        detailsBox.getStyleClass().add("guide-details");
        detailsBox.setPrefHeight(200);


        VBox.setVgrow(detailsBox, Priority.ALWAYS);

        Label nameLabel = new Label(guide.getFirstname_g() + " " + guide.getLastname_g());
        nameLabel.getStyleClass().add("guide-title");
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.setAlignment(Pos.CENTER);

        Label languageLabel = new Label("Language: " + guide.getLanguage());
        languageLabel.getStyleClass().add("guide-language");
        languageLabel.setMaxWidth(Double.MAX_VALUE);
        languageLabel.setAlignment(Pos.CENTER);

        Label countryLabel = new Label("Country: " + guide.getCountryName()); // Assuming you have a method to get the country name
        countryLabel.getStyleClass().add("guide-country");
        countryLabel.setMaxWidth(Double.MAX_VALUE);
        countryLabel.setAlignment(Pos.CENTER);

        Label priceLabel = new Label("Price: $" + String.format("%.2f", guide.getPrice()));
        priceLabel.getStyleClass().add("guide-price");
        priceLabel.setMaxWidth(Double.MAX_VALUE);
        priceLabel.setAlignment(Pos.CENTER);

        double averageRate = 0.0;
        try {
            averageRate = guideServices.calculateAverageRate(guide.getCIN());
        } catch (SQLException e) {
            System.err.println("Error calculating average rate: " + e.getMessage());
            e.printStackTrace();
        }



        Button bookButton = new Button("Book");
        bookButton.getStyleClass().addAll("guide-button");
        bookButton.setOnAction(event -> handleBooking(guide));

        Button feedbackButton = new Button("Give Feedback");
        feedbackButton.getStyleClass().addAll("guide-button");
        feedbackButton.setOnAction(event -> handleFeedback(guide));

        HBox buttonsBox = new HBox(10); // Horizontal box to hold buttons
        buttonsBox.getChildren().addAll(bookButton, feedbackButton);

        detailsBox.getChildren().addAll(nameLabel, languageLabel, countryLabel, priceLabel, buttonsBox);

        AnchorPane.setTopAnchor(imageView, 0.0);
        AnchorPane.setLeftAnchor(imageView, 0.0);
        AnchorPane.setRightAnchor(imageView, 0.0);

        AnchorPane.setTopAnchor(detailsBox, 160.0); // Adjust top anchor to leave space for the image
        AnchorPane.setLeftAnchor(detailsBox, 0.0);
        AnchorPane.setRightAnchor(detailsBox, 0.0);
        AnchorPane.setBottomAnchor(detailsBox, 0.0);

        guideCard.getChildren().addAll(imageView, detailsBox);

        return guideCard;
    }









    private void handleBooking(Guide guide) {
        try {
            // Load the FXML document for booking a guide
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BookGuideFront.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the selected guide to it
            BookGuideFront controller = loader.getController();
            // Assuming guide's fullname is a combination of first name and last name
            controller.selectGuideByName(guide.getFirstname_g() + " " + guide.getLastname_g());

            // Setup the stage and scene
            Stage stage = new Stage();
            stage.setTitle("Book Guide: " + guide.getFirstname_g() + " " + guide.getLastname_g());
            stage.initModality(Modality.APPLICATION_MODAL); // Block user interaction with other windows
            stage.setScene(new Scene(root));

            // Show the booking window
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }

    private void handleFeedback(Guide guide) {
        try {
            // Load the FXML document for booking a guide
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddFeedback.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the selected guide to it
            AddFeedback controller = loader.getController();
            // Assuming guide's fullname is a combination of first name and last name
            controller.selectGuideByName(guide.getFirstname_g() + " " + guide.getLastname_g());

            // Setup the stage and scene
            Stage stage = new Stage();
            stage.setTitle("Give Feedback: " + guide.getFirstname_g() + " " + guide.getLastname_g());
            stage.initModality(Modality.APPLICATION_MODAL); // Block user interaction with other windows
            stage.setScene(new Scene(root));

            // Show the booking window
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }



    @FXML
    private void goToHomeView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Home.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) homeButton.getScene().getWindow(); // Retrieves the current window
        stage.setScene(new Scene(root));
        stage.show();
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
