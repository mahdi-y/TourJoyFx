package Controller;

import Entities.Country;
import Entities.Monument;
import Services.ServiceCountry;
import Services.ServiceMonument;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import Services.WikipediaAPI;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MonumentController {

    public Button SelectImageMonumentButton;
    public Pane inner_pane;
    public Pane most_inner_pane;
    public AnchorPane side_ankerpane;
    public Button btn_workbench;
    public Button btn_workbench211;
    public Button btn_workbench2;
    public Button btn_workbench21;
    public Button btn_workbench1;
    public Button btn_workbench11;
    public TextField searchField;
    public Button clearButton1;
    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    @FXML
    private TextField idField;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private TextField entryPriceField;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField latitudeField;

    @FXML
    private TextField longitudeField;

    @FXML
    private TableView<Monument> displayMonuments;



    @FXML
    private TableColumn<Monument, String> countryColumn;

    @FXML
    private TableColumn<Monument, String> nameColumn;

    @FXML
    private TableColumn<Monument, String> typeColumn;

    @FXML
    private TableColumn<Monument, Integer> entryPriceColumn;

    @FXML
    private TableColumn<Monument, String> statusColumn;

    @FXML
    private TableColumn<Monument, String> descriptionColumn;

    @FXML
    private TableColumn<Monument, Integer> latitudeColumn;

    @FXML
    private TableColumn<Monument, Integer> longitudeColumn;

    @FXML
    private Button selectImageMonumentButton;

    @FXML
    private ImageView imageViewMonument;
    private ObservableList<Monument> masterData = FXCollections.observableArrayList();


    private String imagePath = "images/image.png"; // Default image path
    @FXML
    private StackedBarChart<String, Number> monumentChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    private ServiceMonument serviceMonument;
    private ServiceCountry serviceCountry;

    public MonumentController() {
        this.serviceMonument = new ServiceMonument();
        this.serviceCountry = new ServiceCountry();
    }
    private void loadDefaultImage() {
        try {
            String defaultImagePath = "/images/image.png";  // Corrected path assuming your images folder is directly under resources
            URL imageURL = getClass().getResource(defaultImagePath);
            if (imageURL != null) {
                Image defaultImage = new Image(imageURL.toExternalForm());
                imageViewMonument.setImage(defaultImage);
            } else {
                System.out.println("Default image not found: " + defaultImagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadMonumentStatsToChart() throws SQLException {
        // Set chart title
        monumentChart.setTitle("Statistics of Monuments per Country");
        monumentChart.setLegendVisible(false);

        // Clear existing chart data
        monumentChart.getData().clear();

        // Fetch monument statistics and update chart data
        Map<String, Integer> stats = serviceMonument.getMonumentCountByCountry();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        final AtomicInteger countryIndex = new AtomicInteger(1);

        // Determine the maximum count value for setting the upper bound of the y-axis
        int maxCount = Collections.max(stats.values());

        stats.forEach((country, count) -> {
            XYChart.Data<String, Number> data = new XYChart.Data<>(country, count);
            data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                if (newNode != null) {
                    // Assign a unique style class based on country index
                    newNode.getStyleClass().add("chart-bar-" + countryIndex.getAndIncrement());
                }
            });
            series.getData().add(data);
        });

        monumentChart.getData().add(series);

        // Configure Y-axis to have ticks representing only whole numbers
        yAxis.setAutoRanging(false); // Disable auto-ranging
        yAxis.setLowerBound(0); // Explicitly set the lower bound
        yAxis.setUpperBound(maxCount + 1); // Set the upper bound based on the max count
        yAxis.setTickUnit(1); // Set the tick unit to 1
        yAxis.setMinorTickCount(0); // Remove minor tick counts
        yAxis.setForceZeroInRange(true);

        // Apply styles and layout the chart to reflect the changes
        monumentChart.applyCss();
        monumentChart.layout();

        // Apply custom coloring after the chart data is updated (if you have this method)
        applyCustomColoring(stats.size());
    }

    private void applyCustomColoring(int numberOfCountries) {
        monumentChart.applyCss();
        monumentChart.layout();

        int colorIndex = 1; // Start from 1 to match CSS classes

        for (XYChart.Series<String, Number> series : monumentChart.getData()) {
            int dataCount = series.getData().size();
            for (int i = 0; i < dataCount; i++) {
                XYChart.Data<String, Number> data = series.getData().get(i);
                Node node = data.getNode();
                if (node != null) {
                    int styleIndex = (i % 6) + 1; // This ensures a cycle through 1 to 6
                    String styleClass = "chart-bar-" + styleIndex;
                    node.getStyleClass().add(styleClass);
                    System.out.println("Applied style: " + node.getStyleClass()); // Debugging line
                }
            }
        }


    }




    private void setupColumnBindings() {
       // idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        countryColumn.setCellValueFactory(cellData -> {
            Monument monument = cellData.getValue();
            Country country = monument.getCountry();
            if (country != null) {
                return new SimpleStringProperty(country.getName());
            } else {
                return new SimpleStringProperty("");
            }
        });
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        entryPriceColumn.setCellValueFactory(new PropertyValueFactory<>("entryPrice"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        latitudeColumn.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        longitudeColumn.setCellValueFactory(new PropertyValueFactory<>("longitude"));

    }
    private void loadData() {
        masterData = FXCollections.observableArrayList();  // Initialize masterData list
        try {
            List<Monument> monuments = serviceMonument.Read();

            masterData.addAll(monuments);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch Monuments from the database.");
        }
    }
    @FXML
    public void initialize() throws SQLException {

        loadDefaultImage();

        setupColumnBindings();
        try {
            populateCountryComboBox();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadData();
        setupSearchFilter();
        displayMonuments.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            updateFormFields(newSelection);
        });
        loadMonumentStatsToChart();  // Make sure this is called after all setups



    }

    private WikipediaAPI wikipediaService = new WikipediaAPI();

    private void updateFormFields(Monument monument) {
        if (monument != null) {
            countryComboBox.setValue(monument.getCountry().getName());
            nameField.setText(monument.getName());
            typeComboBox.setValue(monument.getType());
            entryPriceField.setText(String.valueOf(monument.getEntryPrice()));
            statusComboBox.setValue(monument.getStatus());
            latitudeField.setText(String.valueOf(monument.getLatitude()));
            longitudeField.setText(String.valueOf(monument.getLongitude()));

            // Fetch and update the description asynchronously
            String description = wikipediaService.getMonumentDescription(monument.getName().replace(" ", "_"));
            descriptionField.setText(description);

            // Set image if exists
            try {
                String imagePath = monument.getImagePath();
                if (imagePath != null && !imagePath.isEmpty()) {
                    imageViewMonument.setImage(new Image(imagePath));
                } else {
                    loadDefaultImage();
                }
            } catch (Exception e) {
                System.err.println("Error setting image: " + e.getMessage());
                loadDefaultImage();
            }
        } else {
            clearFields();
        }
    }

    @FXML
    public void fetchDescription() {
        String query = nameField.getText().trim().replace(" ", "_");
        String description = wikipediaService.getMonumentDescription(query);
        descriptionField.setText(description);
    }



    private void setupSearchFilter() {
        FilteredList<Monument> filteredData = new FilteredList<>(masterData, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(monument -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return monument.getName().toLowerCase().contains(lowerCaseFilter) ||
                        monument.getType().toLowerCase().contains(lowerCaseFilter) ;
            });
        });
        SortedList<Monument> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(displayMonuments.comparatorProperty());
        displayMonuments.setItems(sortedData);
    }
    @FXML
    void selectImageMonument() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");

        // Filter files to show only images
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif")
        );

        // Show file dialog
        File selectedFile = fileChooser.showOpenDialog(null);

        // Check if a file is selected
        if (selectedFile != null) {
            // Get the path to the selected image
            imagePath = selectedFile.toURI().toString();

            // Load the image into the ImageView
            try {
                Image image = new Image(imagePath);
                imageViewMonument.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Error loading image", "Failed to load the selected image.");
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void populateCountryComboBox() throws SQLException {
        List<Country> countries = serviceCountry.Read();
        List<String> countryNames = new ArrayList<>();
        for (Country country : countries) {
            countryNames.add(country.getName());
        }
        ObservableList<String> countryList = FXCollections.observableArrayList(countryNames);
        countryComboBox.setItems(countryList);
    }



    @FXML
    public void addMonument() {
        // Start by ensuring all input fields are validated
        if (!validateInput()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please check your inputs and try again.");
            return;
        }

        try {
            String selectedCountryName = countryComboBox.getValue();
            Country selectedCountry = serviceCountry.getCountryByName(selectedCountryName);
            if (selectedCountry == null) {
                showAlert(Alert.AlertType.ERROR, "Selection Error", "Selected country is not available.");
                return;
            }

            // Gather all inputs
            String name = nameField.getText();
            String type = typeComboBox.getValue();
            String status = statusComboBox.getValue();
            String description = descriptionField.getText();
            String selectedImagePath = imagePath;
            int entryPrice = Integer.parseInt(entryPriceField.getText());
            double latitude = Double.parseDouble(latitudeField.getText());
            double longitude = Double.parseDouble(longitudeField.getText());

            // Create a new Monument object
            Monument monument = new Monument(selectedCountry, name, type, entryPrice, status, description, latitude, longitude, selectedImagePath);

            // Add the monument to the ObservableList for UI
            masterData.add(monument);

            // Attempt to add the monument to the database
            serviceMonument.add(monument);

            // UI cleanup and refresh after successful addition
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Monument added successfully!");
            searchField.setText("");
            // Notify the TableView to update
            displayMonuments.setItems(null);
            displayMonuments.layout();
            displayMonuments.setItems(masterData); // This will force the TableView to update

            loadMonumentStatsToChart();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numeric values for Entry Price, Latitude, and Longitude.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding monument to the database: " + e.getMessage());
        }
    }

    public void refreshTableView() {
        try {
            loadData();
        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }

    private boolean validateInput() {
        // Vérifier si une option est sélectionnée dans la ComboBox country
        if (countryComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Please select a country.");
            return false;
        }

        // Vérifier si une option est sélectionnée dans la ComboBox type
        if (typeComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Please select a type.");
            return false;
        }

        // Vérifier si une option est sélectionnée dans la ComboBox status
        if (statusComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Please select a status.");
            return false;
        }

        // Vérifier les autres champs
        if (nameField.getText().isEmpty() || descriptionField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Name and Description fields cannot be empty.");
            return false;
        }

        if (!entryPriceField.getText().matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Entry Price must be a positive integer.");
            return false;
        }

        return true;
    }


    @FXML
    public void deleteMonument() throws SQLException {
        Monument selectedMonument = displayMonuments.getSelectionModel().getSelectedItem();
        if (selectedMonument != null) {
            try {
                serviceMonument.delete(selectedMonument);
                masterData.remove(selectedMonument); // Remove from the master data list
                showAlert(Alert.AlertType.INFORMATION, "Success", "Monument deleted successfully!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error deleting monument from the database.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a monument to delete.");
        }

        clearFields(); // Clear input fields after adding the monument
        loadMonumentStatsToChart(); // Refresh chart data after adding

    }


    @FXML
    public void updateMonument() throws SQLException {
        Monument selectedMonument = displayMonuments.getSelectionModel().getSelectedItem();
        if (selectedMonument != null) {
            try {
                // Validate input fields
                if (validateInput()) {
                   // int id = Integer.parseInt(idField.getText());
                    String selectedCountryName = countryComboBox.getValue(); // Get the selected country name
                    Country selectedCountry = serviceCountry.getCountryByName(selectedCountryName);

                    String name = nameField.getText();
                    String type = typeComboBox.getValue();
                    int entryPrice = Integer.parseInt(entryPriceField.getText());
                    String status = statusComboBox.getValue();
                    String description = descriptionField.getText();
                    double latitude = Double.parseDouble(latitudeField.getText());
                    double longitude = Double.parseDouble(longitudeField.getText());

                   // selectedMonument.setId(id);
                    selectedMonument.setCountry(selectedCountry);
                    selectedMonument.setName(name);
                    selectedMonument.setType(type);
                    selectedMonument.setEntryPrice(entryPrice);
                    selectedMonument.setStatus(status);
                    selectedMonument.setDescription(description);
                    selectedMonument.setLatitude(latitude);
                    selectedMonument.setLongitude(longitude);

                    // Update the image path if a new image is selected
                    if (!imagePath.equals(selectedMonument.getImagePath())) {
                        selectedMonument.setImagePath(imagePath);
                    }

                    serviceMonument.update(selectedMonument);

                    displayMonuments.refresh();

                    showAlert(Alert.AlertType.INFORMATION, "Success", "Monument updated successfully!");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter valid numeric values for ID, Entry Price, Latitude, and Longitude.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating monument in the database.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a monument to update.");
        }
        clearFields(); // Clear input fields after updating the monument
        loadMonumentStatsToChart(); // Refresh chart data after adding

    }


    private void clearFields() {
        countryComboBox.getSelectionModel().clearSelection();
        nameField.clear();
        typeComboBox.getSelectionModel().clearSelection();
        entryPriceField.clear();
        statusComboBox.getSelectionModel().clearSelection();
        descriptionField.clear();
        latitudeField.clear();
        longitudeField.clear();

        // Load and set the default image
        Image defaultImage = new Image(getClass().getResourceAsStream("/images/image.png"));
        imageViewMonument.setImage(defaultImage);
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goToMonument(ActionEvent event) {
        URL url = getClass().getResource("/com/test/tjv2/Monument.fxml");
        if (url == null) {
            System.err.println("Cannot find Monument.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                addButton.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }


    }


    public void goToCountry(ActionEvent event) {
        URL url = getClass().getResource("/com/test/tjv2/Country.fxml");
        if (url == null) {
            System.err.println("Cannot find Monument.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                addButton.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }

    }

    public void clearMonument(ActionEvent actionEvent) {
        clearFields();
    }
}
