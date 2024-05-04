package controller;
import Entities.Accomodation;
import Entities.Images;
import Services.ServiceAccomodation;
import Services.ServiceReservation;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.stream.Collectors;


public class AccomodationController {

    @FXML
    private TableView<Accomodation> DisplayAccommodations;
    @FXML
    private TableColumn<Accomodation, String> nameColumn;
    @FXML
    private TableColumn<Accomodation, String> typeColumn;
    @FXML
    private TableColumn<Accomodation, String> locationColumn;
    @FXML
    private TableColumn<Accomodation, Float> priceColumn;
    @FXML
    private TableColumn<Accomodation, Integer> roomColumn;
    @FXML
    private PieChart piechart;
    @FXML
    private TextField refAField;

    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private TextField locationField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField roomField;

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    private int currentAccommodationId = -1;
    @FXML
    private Button SeeRes;


    @FXML
    private ImageView imageViewAccommodation;

    @FXML
    private ListView<ImageView> ListViewAcc;  // Ensure this matches the fx:id in your FXML



    private String image_name = "images/image.jpg"; // Default image path




    private ServiceAccomodation serviceAccomodation;
    private ServiceReservation serviceReservation;


    @FXML
    void initialize() {
        try {
            String defaultImage_name = "/images/image.jpg";  // Corrected path assuming your images folder is directly under resources
            Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(defaultImage_name)));
            imageViewAccommodation.setImage(defaultImage);
        } catch (NullPointerException e) {
            System.out.println("Default image not found");
        }
        // Initialize the ImageView with the default image
        String defaultImage_name = "/images/image.jpg"; // Assuming "images" is a package in your resources folder
        Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(defaultImage_name)));
        imageViewAccommodation.setImage(defaultImage);
        serviceAccomodation = new ServiceAccomodation();
        serviceReservation = new ServiceReservation();
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));

        typeComboBox.getItems().addAll(
                "Villa", "Guesthouse", "Private house", "Apartment", "Farm house"
        );
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        roomColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNb_rooms()).asObject());
        priceColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getPrice()).asObject());
        addButton.setOnAction(event -> addAccomodation());
        deleteButton.setOnAction(event -> deleteAccomodation());
        updateButton.setOnAction(event -> updateAccomodation());
      //  clear1.setOnAction(event -> ClearAcc());

        // Load accommodations into the table
        try {
            List<Accomodation> listAccomodations = serviceAccomodation.Read();
            DisplayAccommodations.getItems().addAll(listAccomodations);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch accommodations from the database.", "Failed to load the selected image.");
        }

        // Add listener to handle row selection
        DisplayAccommodations.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                currentAccommodationId = newSelection.getRefA();  // Assuming getRefA() returns the ID of the accommodation
                nameField.setText(newSelection.getName());
                typeComboBox.setValue(newSelection.getType());
                locationField.setText(newSelection.getLocation());
                priceField.setText(String.valueOf(newSelection.getPrice()));
                roomField.setText(String.valueOf(newSelection.getNb_rooms()));

                try {
                    // Assuming getImage_name() returns the URL/path to the primary image
                    Image image = new Image(newSelection.getImage_name());
                    imageViewAccommodation.setImage(image);
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error Loading Image", "Could not load the image.", e.getMessage());
                }
            } else {
                currentAccommodationId = -1; // Invalidate ID when there is no selection
                clearInputForm(); // Clear form if nothing is selected
            }
        });
        loadPieChartData();

    }
    @FXML
    void selectImageAccommodation() {
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
            image_name = selectedFile.toURI().toString();

            // Load the image into the ImageView
            try {
                Image image = new Image(image_name);
                imageViewAccommodation.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Error loading image", "Failed to load the selected image.");
            }
        }
    }
    @FXML
    void addAccomodation() {
        try {
            String name = nameField.getText();
            String type = typeComboBox.getValue();
            String location = locationField.getText();
            int nb_rooms= Integer.parseInt(roomField.getText());
            float price= Float.parseFloat(priceField.getText());



            String selectedImage_name = image_name;




            // Validate fields

            if (name.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Name is obligatory.", "Failed to load the selected image.");
                return;
            }
            if(name.length()<6){
                showAlert(Alert.AlertType.ERROR, "Input Error", "Name is too short.", "Failed to load the selected image.");
                return;
            }
            if (type == null || type.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Type is obligatory.", "Failed to load the selected image.");
                return;
            }
            if (location.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Location is obligatory.", "Failed to load the selected image.");
                return;
            }
            if (price <= 0) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Price must be positive.", "Failed to load the selected image.");
                return;
            }
            if (nb_rooms < 0 || nb_rooms > 6) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Number of rooms must be between 0 and 6.", "Failed to load the selected image.");
                return;
            }

            // Create accommodation object
            Accomodation accomodation = new Accomodation( name, type, location, price, nb_rooms,selectedImage_name);


            // Add accommodation to database
            serviceAccomodation.add(accomodation);

            // Update TableView
            DisplayAccommodations.getItems().add(accomodation);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Accommodation added successfully!", "Failed to load the selected image.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid ID.", "Failed to load the selected image.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding Accommodation to the database.", "Failed to load the selected image.");
        }
    }

    @FXML
    void deleteAccomodation() {
        Accomodation selectedAccomodation = DisplayAccommodations.getSelectionModel().getSelectedItem();
        if (selectedAccomodation != null) {
            try {
                serviceAccomodation.delete(selectedAccomodation);
                DisplayAccommodations.getItems().remove(selectedAccomodation);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Accommodation deleted successfully!", "Failed to load the selected image.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error deleting accommodation from the database.", "Failed to load the selected image.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select an accommodation to delete.", "Failed to load the selected image.");
        }
    }

    @FXML
    void updateAccomodation() {
        Accomodation selectedAccomodation = DisplayAccommodations.getSelectionModel().getSelectedItem();
        if (selectedAccomodation != null) {
            try {
                int refA=Integer.parseInt(refAField.getText());
                String name = nameField.getText();
                String type = typeComboBox.getValue();
                String location = locationField.getText();
                int nb_rooms= Integer.parseInt(roomField.getText());
                float price= Float.parseFloat(priceField.getText());

                // Validate fields

                if (name.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Name is obligatory.", "Failed to load the selected image.");
                    return;
                }
                if(name.length()<6){
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Name is too short.", "Failed to load the selected image.");
                    return;
                }
                if (type == null || type.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Type is obligatory.", "Failed to load the selected image.");
                    return;
                }
                if (location.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Location is obligatory.", "Failed to load the selected image.");
                    return;
                }
                if (price <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Price must be positive.", "Failed to load the selected image.");
                    return;
                }
                if (nb_rooms < 0 || nb_rooms > 6) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Number of rooms must be between 0 and 6.", "Failed to load the selected image.");
                    return;
                }

                // Update selected accommodation object
                selectedAccomodation.setName(name);
                selectedAccomodation.setType(type);
                selectedAccomodation.setLocation(location);
                selectedAccomodation.setPrice(price);
                selectedAccomodation.setNb_rooms(nb_rooms);



                // Update accommodation in the database
                serviceAccomodation.update(selectedAccomodation);

                // Refresh TableView
                DisplayAccommodations.refresh();

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Accommodation updated successfully!", "Failed to load the selected image.");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid ID.", "Failed to load the selected image.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating accommodation in the database.", "Failed to load the selected image.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a accommodation to update.", "Failed to load the selected image.");
        }
    }

    @FXML
    void selectMultipleImages() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Images");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                String imagePath = file.toURI().toString();
                Images image = new Images(currentAccommodationId, imagePath);
                System.out.println("Current Accommodation ID: " + currentAccommodationId);  // Debugging line
                try {
                    serviceAccomodation.addImage(image);
                    ListViewAcc.getItems().add(new ImageView(new Image(imagePath)));
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add image to database", e.getMessage());
                }
            }
        }
    }








    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    private void clearInputForm() {
        // Clear all input fields and the image list
        nameField.clear();
        refAField.clear();
        typeComboBox.getSelectionModel().clearSelection();
        locationField.clear();
        priceField.clear();
        roomField.clear();
        ListViewAcc.getItems().clear();
    }


    @FXML
    private void loadPieChartData() {
        try {
            Map<Integer, Integer> bookingCounts = serviceReservation.getBookingCounts();
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            List<Accomodation> accommodations = serviceAccomodation.Read();
            for (Accomodation accomodation : accommodations) {
                Integer bookings = bookingCounts.getOrDefault(accomodation.getRefA(), 0);
                pieChartData.add(new PieChart.Data(accomodation.getName(), bookings));
            }

            piechart.setData(pieChartData);
            piechart.setTitle("Booking Statistics");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch booking statistics.", e.getMessage());
        }
    }

    public void SeeRes(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/javafx/resBack.fxml"));
            SeeRes.getScene().setRoot(root);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }



}








