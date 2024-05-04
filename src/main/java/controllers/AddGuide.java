package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

import Entities.Country;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Scene;

import Entities.Booking;
import Entities.Guide;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import Services.BookingServices;
import Services.GuideServices;
import Services.ServiceCountry;
import test.GMailer;
import utils.MyDB;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;


public class AddGuide {
    private BookingServices bookingServices;

    private TableView<Booking> ListBookings;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Guide> ListGuides;

    @FXML
    private TableColumn<Guide, Integer> cinColumn;

    @FXML
    private TableColumn<Guide, Double> priceColumn;
    @FXML
    private TableColumn<Guide, Double> countryColumn;
    @FXML
    private TableColumn<Guide, String> firstNameColumn;

    @FXML
    private TableColumn<Guide, String> lastNameColumn;

    @FXML
    private TableColumn<Guide, String> emailColumn;

    @FXML
    private TableColumn<Guide, String> phoneNumberColumn;

    @FXML
    private TableColumn<Guide, String> genderColumn;

    @FXML
    private TableColumn<Guide, String> languageColumn;


    @FXML
    private TableColumn<Guide, String> dobColumn;

    @FXML
    private TextField cinField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField genderField;

    @FXML
    private ComboBox<String> languageField; // Ensure you have this line to link with FXML
    @FXML
    private ComboBox<String> countryField; // Ensure you have this line to link with FXML

    @FXML
    private DatePicker dobPicker;

    @FXML
    private TextField priceField;


    private GuideServices guideServices;
    private Connection cnx;
    @FXML
    private ImageView imageViewGuide;  // Reference to the ImageView in FXML
    @FXML
    private Button frontoffice;
    @FXML
    private Button selectImageButton;  // Button to trigger image selection
    private Button exportExcelButton;

    List<Guide> listGuides = new ArrayList<>();
    private Guide selectedGuide;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private ToggleGroup genderToggleGroup;


    public AddGuide() {
        this.bookingServices = new BookingServices(); // Already there
        this.guideServices = new GuideServices(); // Instantiate GuideServices here
    }
    private ServiceCountry countryServices = new ServiceCountry();


    private ObservableList<Guide> masterData = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        // Initialize services and toggle group
        guideServices = new GuideServices();
        genderToggleGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(genderToggleGroup);
        femaleRadioButton.setToggleGroup(genderToggleGroup);
        maleRadioButton.setSelected(true);

        // Setup languages
        List<String> languages = Arrays.asList(
                "English", "Mandarin Chinese", "Hindi", "Spanish", "French",
                "Standard Arabic", "Bengali", "Russian", "Portuguese", "Indonesian"
        );
        languageField.getItems().addAll(languages);
        List<String> countryNames = countryServices.getAllCountryNames();
        countryField.getItems().addAll(countryNames);
        // Load default image
        loadDefaultImage();

        // Setup button actions
        selectImageButton.setOnAction(event -> selectImage());

        // Setup table column bindings
        setupColumnBindings();

        // Load data from database
        loadData();

        // Setup search filter
        setupSearchFilter();

        // Setup listener for selection changes to update form fields
        ListGuides.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            updateFormFields(newSelection);
        });}

    private void loadDefaultImage() {
        try {
            Image defaultImage = new Image(getClass().getResourceAsStream("/image/image.png"));
            imageViewGuide.setImage(defaultImage);
        } catch (Exception e) {
            System.out.println("Default image not found: " + e.getMessage());
        }
    }

    private void setupColumnBindings() {
        cinColumn.setCellValueFactory(new PropertyValueFactory<>("CIN"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname_g"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname_g"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailaddress_g"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phonenumber_g"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender_g"));
        languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    private void loadData() {
        masterData = FXCollections.observableArrayList();  // Initialize masterData list
        try {
            List<Guide> guides = guideServices.Read();
            masterData.addAll(guides);
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Failed to fetch guides from the database.");
        }
    }
    public void send() throws Exception {
        new GMailer().sendMail("eya.benouhiba@esprit.tn", "Booking Cancelled", "Your booking has been cancelled.");
    }
    private void setupSearchFilter() {
        FilteredList<Guide> filteredData = new FilteredList<>(masterData, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(guide -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return guide.getFirstname_g().toLowerCase().contains(lowerCaseFilter) ||
                        guide.getLastname_g().toLowerCase().contains(lowerCaseFilter) ||
                        (guide.getFirstname_g().toLowerCase() + " " + guide.getLastname_g().toLowerCase()).contains(lowerCaseFilter) ||
                        String.valueOf(guide.getCIN()).contains(lowerCaseFilter);
            });
        });
        SortedList<Guide> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(ListGuides.comparatorProperty());
        ListGuides.setItems(sortedData);
    }

    private void updateFormFields(Guide guide) {
        if (guide != null) {
            cinField.setText(String.valueOf(guide.getCIN()));
            firstNameField.setText(guide.getFirstname_g());
            lastNameField.setText(guide.getLastname_g());
            emailField.setText(guide.getEmailaddress_g());
            phoneNumberField.setText(guide.getPhonenumber_g());
            languageField.setValue(guide.getLanguage());
            dobPicker.setValue(LocalDate.parse(guide.getDob()));
            priceField.setText(String.valueOf(guide.getPrice()));
            RadioButton selectedRadioButton = guide.getGender_g().equals("Male") ? maleRadioButton : femaleRadioButton;
            genderToggleGroup.selectToggle(selectedRadioButton);
            try {
                imageViewGuide.setImage(new Image(guide.getImage()));
            } catch (Exception e) {
                System.err.println("Error setting image: " + e.getMessage());
                imageViewGuide.setImage(null);
            }
        } else {
            clearFormFields();
        }
    }

    private void clearFormFields() {
        cinField.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneNumberField.clear();
        languageField.getSelectionModel().clearSelection();
        dobPicker.setValue(null);
        priceField.clear();
        imageViewGuide.setImage(null);
    }



    private boolean validateFields() throws SQLException {
        String cin = cinField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        LocalDate dob = dobPicker.getValue();
        String priceStr = priceField.getText().trim();

        // Check for empty fields
        if (cin.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || dob == null || priceStr.isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "All fields must be filled out.");
            return false;
        }

        // Validate CIN
        if (!cin.matches("\\d{8}")) {
            showAlert(AlertType.ERROR, "Validation Error", "CIN must be 8 digits.");
            return false;
        }


        // Validate Email
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            showAlert(AlertType.ERROR, "Validation Error", "Invalid email format.");
            return false;
        }

        // Validate Phone Number
        if (!phoneNumber.matches("\\d{8}")) {
            showAlert(AlertType.ERROR, "Validation Error", "Phone number must be 8 digits.");
            return false;
        }

        // Validate Date of Birth
        LocalDate minDate = LocalDate.of(1965, 1, 1);
        LocalDate maxDate = LocalDate.of(2003, 12, 31);
        if (dob.isBefore(minDate) || dob.isAfter(maxDate)) {
            showAlert(AlertType.ERROR, "Validation Error", "Date of birth must be between 1965 and 2003.");
            return false;
        }

        // Validate Price
        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                showAlert(AlertType.ERROR, "Validation Error", "Price must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Validation Error", "Price must be a number.");
            return false;
        }

        // Validate First Name and Last Name
        if (!firstName.matches("[a-zA-Z]+")) {
            showAlert(AlertType.ERROR, "Validation Error", "First name must contain only letters.");
            return false;
        }
        if (!lastName.matches("[a-zA-Z]+")) {
            showAlert(AlertType.ERROR, "Validation Error", "Last name must contain only letters.");
            return false;
        }

        // If all checks pass
        return true;
    }




    @FXML
    public void clearGuide() {
        // Clear all text fields
        cinField.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneNumberField.clear();
        languageField.getSelectionModel().selectFirst(); // Optionally set the first language as the default
        countryField.getSelectionModel().selectFirst(); // Optionally set the first language as the default
        dobPicker.setValue(null);
        priceField.clear();

        // Reset the image to a default image
        try {
            String defaultImagePath = "image/image.png";  // Correct path inside your resources directory
            Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(defaultImagePath)));
            imageViewGuide.setImage(defaultImage);
        } catch (NullPointerException e) {
            System.err.println("Default image not found");
            imageViewGuide.setImage(null); // Set to null if default image is not found
        }}

    @FXML
    void Addguide() throws SQLException {
        if (!validateFields()) {
            return;
        }

        // Gather information from form
        int cin = Integer.parseInt(cinField.getText());
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        RadioButton selectedGenderButton = (RadioButton) genderToggleGroup.getSelectedToggle();
        String gender = selectedGenderButton != null ? selectedGenderButton.getText() : "";
        String language = languageField.getValue();
        LocalDate dob = dobPicker.getValue();
        Double price = Double.parseDouble(priceField.getText());
        String imagePath = imageViewGuide.getImage() != null ? imageViewGuide.getImage().getUrl() : "";
        int country_id = getCountryId(countryField.getValue()); // Assuming countryField is a ComboBox<String>


        // Create new Guide object
        Guide guide = new Guide(cin, firstName, lastName, email, phoneNumber, gender, language, dob.toString(), price, imagePath, country_id);

        // Add the guide to the database
        guideServices.add(guide);

        // Add to the master data for immediate GUI update
        masterData.add(guide);

        showAlert(AlertType.INFORMATION, "Success", "Guide added successfully!");

        // Clear the search field and refresh the list to show all items
        searchField.setText("");
        // Optionally clear the form
        clearGuide();
    }


    private void refreshTableView() {
        // Clear and re-load data from the database or directly refresh the table view if data already loaded
        loadData();  // Reload all guides to include the newly added one

    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void deleteGuide() {
        Guide selectedGuide = ListGuides.getSelectionModel().getSelectedItem();
        if (selectedGuide != null) {
            try {
                guideServices.delete(selectedGuide);

                // Update TableView
                masterData.remove(selectedGuide);  // Assuming masterData is the ObservableList backing the TableView
                ListGuides.setItems(masterData);    // Refresh the TableView
                showAlert(AlertType.INFORMATION, "Success", "Guide deleted successfully!");
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Database Error", "Error deleting guide from the database: " + e.getMessage());
                System.err.println("SQL Exception: " + e.getMessage());  // Print more detailed error message to console
                e.printStackTrace();  // Consider logging this instead if in a production environment
            }
        } else {
            showAlert(AlertType.WARNING, "Selection Error", "Please select a guide to delete.");
        }
    }





    public void updateGuide() {
        Guide selectedGuide = ListGuides.getSelectionModel().getSelectedItem();
        if (selectedGuide == null) {
            showAlert(AlertType.WARNING, "Selection Error", "Please select a guide to update.");
            return;
        }

        try {
            if (!validateFields()) {
                showAlert(AlertType.ERROR, "Validation Error", "Please check your input fields.");
                return;
            }

            int oldCIN = selectedGuide.getCIN();
            int newCIN = Integer.parseInt(cinField.getText().trim());
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String phoneNumber = phoneNumberField.getText();
            RadioButton selectedGenderButton = (RadioButton) genderToggleGroup.getSelectedToggle();
            String gender = (selectedGenderButton != null) ? selectedGenderButton.getText() : "";
            String language = languageField.getValue();
            LocalDate dob = dobPicker.getValue();
            double price = Double.parseDouble(priceField.getText());
            String imagePath = imageViewGuide.getImage() != null ? imageViewGuide.getImage().getUrl() : "";
            int countryId = getCountryId(countryField.getValue());

            if (!isValidCountryId(countryId)) {
                showAlert(AlertType.ERROR, "Invalid Country ID", "The selected country does not exist.");
                return;
            }

            selectedGuide.setCIN(newCIN);
            selectedGuide.setFirstname_g(firstName);
            selectedGuide.setLastname_g(lastName);
            selectedGuide.setEmailaddress_g(email);
            selectedGuide.setPhonenumber_g(phoneNumber);
            selectedGuide.setGender_g(gender);
            selectedGuide.setLanguage(language);
            selectedGuide.setDob(dob.toString());
            selectedGuide.setPrice(price);
            selectedGuide.setImage(imagePath);
            selectedGuide.setCountry(countryId);

            guideServices.update(selectedGuide, oldCIN);
            ListGuides.refresh();
            showAlert(AlertType.INFORMATION, "Success", "Guide updated successfully!");
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Invalid Input", "Please enter a valid numeric value. Error: " + e.getMessage());
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Error updating guide in the database: " + e.getMessage());
        }
    }
    private boolean isValidCountryId(int countryId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Country WHERE id = ?";
        try (Connection conn = MyDB.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, countryId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Check if there is at least one record
                }
            }
        }
        return false;
    }



    public void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();
            Image image = new Image(imagePath);
            imageViewGuide.setImage(image);  // Set the image in ImageViewGuide
        }
    }


    @FXML
    private void guideBookings() throws Exception {
        Guide selectedGuide = ListGuides.getSelectionModel().getSelectedItem();
        if (selectedGuide != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BookingsPerGuide.fxml"));
            Parent root = loader.load();

            BookingsPerGuideController bookingsController = loader.getController();

            // Fetch bookings for the selected guide
            List<Booking> bookings = bookingServices.getBookingsByGuide(selectedGuide.getCIN());  // Implement this method in your BookingServices
            bookingsController.setBookingsData(bookings);

            Stage stage = new Stage();
            stage.setTitle("Bookings for " + selectedGuide.getFirstname_g() + " " + selectedGuide.getLastname_g());
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a guide to view bookings.");
        }
    }
    private int getCountryId(String countryName) throws SQLException {
        ServiceCountry countryServices = new ServiceCountry(); // Instantiate ServiceCountry
        return countryServices.getCountryIdByName(countryName);
    }

    @FXML
    private void gotoFrontOffice() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Home.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) frontoffice.getScene().getWindow(); // Retrieves the current window
        stage.setScene(new Scene(root));
        stage.show();
    }


}
