package Controller;

import Entities.Country;
import Entities.Monument;
import Services.ExchangeRateAPI;
import Services.ServiceCountry;
import Services.ServiceMonument;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

public class MonumentFrontController {
    public Button MonumentButton;
    public Button guideButton;
    public Button SubscriptionButton;
    public Pagination pagination;
    public TextField searchField;
    public ComboBox countryComboBox;
    public Slider priceSlider;
    @FXML
    private GridPane monumentsGrid;
    private ServiceMonument serviceMonument;
    private ServiceCountry serviceCountry;

    @FXML
    private ComboBox<Label> currencyComboBox;
    private List<Monument> monuments; // This should be initialized when loading monuments
    private String selectedCurrency; // Class member variable to keep track of the selected currency

    private ObservableList<Monument> masterData = FXCollections.observableArrayList();
    private FilteredList<Monument> filteredData;

    private final int rowsPerPage = 2; // 2 rows
    private final int columnsPerPage = 3; // 3 columns


    @FXML
    public void initialize() {
        serviceMonument = new ServiceMonument();
        serviceCountry = new ServiceCountry();
        initializeSlider();
        setupPriceFilter();   // Setup price filter
        loadMonumentsData();
        setupSearchFilter();
        populateCurrencyComboBox();
        selectedCurrency = "TND"; // Set TND as the default currency

        // Find the label with the text "TND" and set it as the selected value
        for (Label label : currencyComboBox.getItems()) {
            if (label.getText().equals(selectedCurrency)) {
                currencyComboBox.setValue(label);
                break;
            }
        }
        pagination.setPageFactory(pageIndex -> createPage(pageIndex));

        loadMonumentsWithConversion(selectedCurrency); // Initial load without conversion
        currencyComboBox.setOnAction(event -> onCurrencyChanged());
        populateCountryComboBox();  // Load and populate countries
        setupCountryFilter();
      //  pagination.setPageFactory(pageIndex -> createPage(pageIndex));
       // pagination.setPageFactory(this::createPage); // Set up pagination


    }
    private void populateCountryComboBox() {
        try {
            List<Country> countries = serviceCountry.Read();
            List<String> countryNames = countries.stream()
                    .map(Country::getName)
                    .collect(Collectors.toList());
            ObservableList<String> countryList = FXCollections.observableArrayList();
            countryList.add("Select Country"); // Add default selection option
            countryList.addAll(countryNames);
            countryComboBox.setItems(countryList);
            countryComboBox.setValue("Select Country"); // Set default value to show all monuments
        } catch (SQLException e) {
            System.out.println("Error loading countries from the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupCountryFilter() {
        countryComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateFilters();
        });
    }

    private void setupPriceFilter() {
        priceSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateFilters();  // Call updateFilters to apply both text and price filters
        });
    }
    private void updateFilters() {
        String searchText = searchField.getText().toLowerCase();
        double maxPrice = priceSlider.getValue();
        String selectedCountry = (String) countryComboBox.getValue();  // Cast to String

        filteredData.setPredicate(monument -> {
            boolean matchesSearch = searchText.isEmpty() || monument.getName().toLowerCase().contains(searchText);
            boolean withinPriceRange = monument.getEntryPrice() <= maxPrice;
            boolean matchesCountry = "Select Country".equals(selectedCountry) || monument.getCountry().getName().equalsIgnoreCase(selectedCountry);
            return matchesSearch && withinPriceRange && matchesCountry;
        });
        updatePagination();
    }



    private void initializeSlider() {
        // Assuming priceSlider is the name of your Slider object
        priceSlider.setMin(0);  // Set to the minimum price of your monuments
        priceSlider.setMax(100);  // Set to the maximum price of your monuments
        priceSlider.setBlockIncrement(10);  // Adjust as needed for smoother sliding

        // Initial values can be set to encompass the entire range
        priceSlider.setValue(0);
    }

    private void loadMonumentsData() {
        try {
            List<Monument> monuments = serviceMonument.Read();
            masterData.setAll(monuments); // Assume Read() returns a list of monuments
            filteredData = new FilteredList<>(masterData, p -> true); // Initialize filteredData with a predicate that always returns true
            updatePagination(); // Refresh pagination based on the filtered list
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }
    private void updatePagination() {
        int totalPages = (int) Math.ceil((double) filteredData.size() / (rowsPerPage * columnsPerPage));
        pagination.setPageCount(totalPages);
        pagination.setPageFactory(this::createPage); // This should re-trigger the display logic
    }
    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(monument -> {
                if (newValue == null || newValue.isEmpty()) {
                //    System.out.println("Search is empty, showing all monuments.");
                    return true; // Show all monuments when the search field is empty
                }
                String lowerCaseFilter = newValue.toLowerCase();

                boolean matches = monument.getName().toLowerCase().contains(lowerCaseFilter);
              //  System.out.println("Filtering for: " + lowerCaseFilter + " - Match: " + matches);
                return matches; // Return the filtering result
            });
            updatePagination();
        });
    }

    private Node createPage(int pageIndex) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);  // Center the grid within the Pagination
        grid.setHgap(30);  // Increase horizontal gap
        grid.setVgap(50);  // Increase vertical gap
        grid.setPadding(new Insets(15, 15, 15, 15));

        int pageOffset = pageIndex * rowsPerPage * columnsPerPage;
        List<Monument> pageMonuments = filteredData.subList(
                Math.max(0, pageOffset),
                Math.min(pageOffset + rowsPerPage * columnsPerPage, filteredData.size())
        );

        for (int i = 0; i < pageMonuments.size(); i++) {
            grid.add(createMonumentEntry(pageMonuments.get(i)), i % columnsPerPage, i / columnsPerPage);
        }
        return grid;
    }


    @FXML
    private void onCurrencyChanged() {
        Label selectedLabel = currencyComboBox.getSelectionModel().getSelectedItem();
        if (selectedLabel != null) {
            selectedCurrency = selectedLabel.getText();
            System.out.println("Currency changed to: " + selectedCurrency);
            loadMonumentsWithConversion(selectedCurrency); // This will re-fetch, convert, and update the UI
        }
    }





    public void loadMonumentsWithConversion(String currency) {
        try {
            // Read all monuments regardless of their status, to allow reuse of this method
            monuments = serviceMonument.Read(); // Ensure this method handles any necessary exception

            double exchangeRate = ("TND".equals(currency)) ? 1.0 : getExchangeRateForCurrency("TND", currency);

            // Apply conversion and filter in one pass
            monuments = monuments.stream()
                    .filter(monument -> "active".equalsIgnoreCase(monument.getStatus()))
                    .peek(monument -> {
                        int convertedPrice = (int) Math.round(monument.getEntryPrice() * exchangeRate);
                        monument.setConvertedEntryPrice(convertedPrice);
                    })
                    .collect(Collectors.toList());

            // Recalculate the total pages every time the list is reloaded or currency is changed
            int totalMonuments = monuments.size();
            int totalPages = (int) Math.ceil((double) totalMonuments / (rowsPerPage * columnsPerPage));
            pagination.setPageCount(totalPages);
            pagination.setCurrentPageIndex(0); // Start from the first page
            pagination.setPageFactory(this::createPage); // Reset the page factory to refresh the view
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading monuments.");
        }
        masterData.clear();
        masterData.addAll(monuments);
        updatePagination(); // Ensure the pagination updates to reflect changes
    }






    private void loadMonuments() {
        // Clear the existing monuments from the grid
        monumentsGrid.getChildren().clear();

        // Use the monuments list which should already have the converted prices
        int columns = 3;
        int colIndex = 0;
        int rowIndex = 0;
        for (Monument monument : monuments) {
            AnchorPane monumentEntry = createMonumentEntry(monument);
            monumentsGrid.add(monumentEntry, colIndex, rowIndex);
            if (++colIndex >= columns) {
                colIndex = 0;
                rowIndex++;
            }
        }

        // Request layout to refresh the grid
        monumentsGrid.requestLayout();
    }



    private AnchorPane createMonumentEntry(Monument monument) {
        AnchorPane monumentCard = new AnchorPane();
        monumentCard.getStyleClass().add("monument-box");
        monumentCard.setPrefSize(200, 300);

        ImageView imageView = loadImage(monument.getImagePath());
        imageView.getStyleClass().add("monument-image");
        imageView.setFitHeight(150);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);

        VBox detailsBox = new VBox(5);
        detailsBox.getStyleClass().add("monument-details");
        detailsBox.setPrefHeight(150);
        VBox.setVgrow(detailsBox, Priority.ALWAYS);

        Label nameLabel = new Label(monument.getName());
        nameLabel.getStyleClass().add("monument-title");
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.setAlignment(Pos.CENTER);

        // Use the converted price if available, otherwise fall back to the entry price
        String displayPrice = (monument.getConvertedEntryPrice() != null ?
                monument.getConvertedEntryPrice().toString() :
                Integer.toString(monument.getEntryPrice())) + " " + selectedCurrency;

        Label priceLabel = new Label("Entry Price: " + displayPrice);
        priceLabel.getStyleClass().add("monument-price");
        priceLabel.setMaxWidth(Double.MAX_VALUE);
        priceLabel.setAlignment(Pos.CENTER);

        Button detailsButton = createDetailsButton(monument);
        detailsButton.getStyleClass().add("monument-button");

        detailsBox.getChildren().addAll(nameLabel, priceLabel, detailsButton);

        AnchorPane.setTopAnchor(imageView, 0.0);
        AnchorPane.setLeftAnchor(imageView, 0.0);
        AnchorPane.setRightAnchor(imageView, 0.0);

        AnchorPane.setTopAnchor(detailsBox, 150.0);
        AnchorPane.setLeftAnchor(detailsBox, 0.0);
        AnchorPane.setRightAnchor(detailsBox, 0.0);
        AnchorPane.setBottomAnchor(detailsBox, 0.0);

        monumentCard.getChildren().addAll(imageView, detailsBox);

        return monumentCard;
    }



    private ImageView loadImage(String imagePath) {
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
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://") || imagePath.startsWith("file:/")) {
                image = new Image(imagePath, imageView.getFitWidth(), imageView.getFitHeight(), false, true);
            } else {
                // Attempt to load from the classpath
                InputStream is = getClass().getResourceAsStream(imagePath.startsWith("/") ? imagePath : "/" + imagePath);
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



    private Button createDetailsButton(Monument monument) {
        Button detailsButton = new Button("Show Details");
        detailsButton.setOnAction(event -> showMonumentDetails(monument));
        return detailsButton;
    }

    private void showMonumentDetails(Monument monument) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/test/tjv2/MonumentDetails.fxml"));
            // The root of the MonumentDetails.fxml is now a StackPane, so cast it correctly here.
            StackPane dialogContent = loader.load();
            MonumentDetailsController detailsController = loader.getController();
            detailsController.initData(monument);

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Monument Details");
            // Create a scene with the StackPane as the root.
            dialogStage.setScene(new Scene(dialogContent));
            dialogStage.showAndWait();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load monument details view.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void goToMonuments(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/test/tjv2/MonumentFront.fxml"));
            MonumentButton.getScene().setRoot(root);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    private void populateCurrencyComboBox() {
        // Define currency codes and corresponding file names for the flag images
        String[] currencyCodes = {"USD", "EUR", "GBP", "TND"};
        String[] flagImagePaths = {"images/usa.png", "images/euro.png", "images/uk.png", "images/tunisia.png"}; // Make sure to have these flag images in your resources folder

        // Populate the combo box with labels that contain both the flag and the currency code
        for (int i = 0; i < currencyCodes.length; i++) {
            Image flagImage = new Image(getClass().getResourceAsStream("/" + flagImagePaths[i]), 30, 20, true, true);
            ImageView flagView = new ImageView(flagImage);
            flagView.setFitHeight(20); // Adjust the size of the flag as necessary
            flagView.setFitWidth(30);
            Label label = new Label(currencyCodes[i], flagView);
            currencyComboBox.getItems().add(label);
        }

        // Set a custom cell factory to display labels with text and graphic in the dropdown
        currencyComboBox.setCellFactory(lv -> new ListCell<Label>() {
            @Override
            protected void updateItem(Label item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getText());
                    setGraphic(item.getGraphic());
                }
            }
        });

        // Set the default value (you should adjust this based on your needs)
        currencyComboBox.getSelectionModel().selectFirst();
    }
// Call this method in the initialize() method to populate the ComboBox





    private double getExchangeRateForCurrency(String baseCurrency, String targetCurrency) {
        try {
            return ExchangeRateAPI.getExchangeRate(baseCurrency, targetCurrency);
        } catch (IOException e) {
            e.printStackTrace(); // Add proper logging here
            showAlert(Alert.AlertType.ERROR, "Exchange Rate API Error", "Error fetching exchange rate.");
            return 0;
        }
    }



    private void updateMonumentPrices(String selectedCurrency) {
        try {
            double exchangeRate = getExchangeRateForCurrency("TND", selectedCurrency);
            System.out.println("Exchange Rate: " + exchangeRate); // Debug print

            for (Monument monument : monuments) {
                double priceInTND = monument.getEntryPrice(); // Get the original price
                double convertedPrice = priceInTND * exchangeRate;
                monument.setConvertedEntryPrice((int) Math.round(convertedPrice)); // Set the converted price
            }

            // Reload the monuments with updated prices
            loadMonuments();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Currency Update Error", "Error updating monument prices.");
        }
    }

    private String getExchangeRates(String baseCurrency) {
        String apiKey = "4d1fe7b1a33b2a238069b621";
        String urlString = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + baseCurrency;

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } else {
                throw new IOException("Error: HTTP response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Add proper logging here
            return null; // Return null or handle the error accordingly
        }
    }


}
