package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import entities.Guide;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.GuideServices;
import utils.MyDB;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;


public class AddGuide {

    @FXML
    private TableView<Guide> ListGuides;

    @FXML
    private TableColumn<Guide, Integer> cinColumn;

    @FXML
    private TableColumn<Guide, Double> priceColumn;

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
    private DatePicker dobPicker;

    @FXML
    private TextField priceField;


    private GuideServices guideServices;
    private Connection cnx;
    @FXML
    private ImageView imageViewGuide;  // Reference to the ImageView in FXML

    @FXML
    private Button selectImageButton;  // Button to trigger image selection
    private Button exportExcelButton;

    List<Guide> listGuides = new ArrayList<>();
    private Guide selectedGuide;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton femaleRadioButton;

    private ToggleGroup genderToggleGroup;




    @FXML
    void initialize() {
        List<String> languages = Arrays.asList(
                "English", "Mandarin Chinese", "Hindi", "Spanish",
                "French", "Standard Arabic", "Bengali", "Russian",
                "Portuguese", "Indonesian"
        );
        languageField.getItems().addAll(languages);
        genderToggleGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(genderToggleGroup);
        femaleRadioButton.setToggleGroup(genderToggleGroup);

        // Optionally set a default value
        maleRadioButton.setSelected(true); // Set
        selectImageButton.setOnAction(event -> selectImage());
        guideServices = new GuideServices();

        cinColumn.setCellValueFactory(new PropertyValueFactory<>("CIN"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname_g"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname_g"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailaddress_g"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phonenumber_g"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender_g"));
        languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));


        try {
            List<Guide> listGuides = guideServices.Read();
            ListGuides.getItems().addAll(listGuides);
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Failed to fetch guides from the database.");
        }

        ListGuides.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cinField.setText(String.valueOf(newSelection.getCIN()));
                firstNameField.setText(newSelection.getFirstname_g());
                lastNameField.setText(newSelection.getLastname_g());
                emailField.setText(newSelection.getEmailaddress_g());
                phoneNumberField.setText(newSelection.getPhonenumber_g());
                languageField.setValue(newSelection.getLanguage()); // Set the language in the ComboBox
                dobPicker.setValue(LocalDate.parse(newSelection.getDob()));
                priceField.setText(String.valueOf(newSelection.getPrice()));

            }


            languageField.getSelectionModel().selectFirst(); // Optionally set the first language as the default


        });
    }


    private boolean validateFields() {
        String cin = cinField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        LocalDate dob = dobPicker.getValue();

        // Check for empty fields
        if (cin.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || dob == null) {
            showAlert(AlertType.ERROR, "Validation Error", "All fields must be filled out.");
            return false;
        }

        // Validate CIN
        if (!Pattern.matches("\\d{8}", cin)) {
            showAlert(AlertType.ERROR, "Validation Error", "CIN must be 8 digits.");
            return false;
        }

        // Validate Email
        if (!Pattern.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}", email)) {
            showAlert(AlertType.ERROR, "Validation Error", "Invalid email format.");
            return false;
        }

        // Validate Phone Number
        if (!Pattern.matches("\\d{8}", phoneNumber)) {
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

        // If all checks pass
        return true;
    }


    @FXML
    public void clearGuide() {
        cinField.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneNumberField.clear();
        languageField.getSelectionModel().selectFirst(); // Optionally set the first language as the default
        dobPicker.setValue(null);
        priceField.clear();
        imageViewGuide.setImage(null); // Clear the ImageView
    }

    @FXML
    void AddGuide() {
        if (!validateFields()) {
            return;
        }

        int cin = Integer.parseInt(cinField.getText());
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        // Retrieve the selected gender RadioButton from the ToggleGroup
        RadioButton selectedGenderButton = (RadioButton) genderToggleGroup.getSelectedToggle();
        String gender = (selectedGenderButton != null) ? selectedGenderButton.getText() : "";
        String language = languageField.getValue(); // This will get the selected language from the ComboBox
        LocalDate dob = dobPicker.getValue();
        Double price = Double.parseDouble(priceField.getText());

        String imagePath = ""; // Default to an empty string if no image is selected

        // Assume you have an ImageView with fx:id="imageViewGuide"
        if (imageViewGuide.getImage() != null) {
            imagePath = imageViewGuide.getImage().getUrl(); // Get the URL of the image
        }


        Guide guide = new Guide(cin, firstName, lastName, email, phoneNumber, gender, language, dob.toString(),price, imagePath);

        guideServices.add(guide);
        ListGuides.getItems().add(guide);
        showAlert(AlertType.INFORMATION, "Success", "Guide added successfully!");
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
                ListGuides.getItems().remove(selectedGuide);
                showAlert(AlertType.INFORMATION, "Success", "Guide deleted successfully!");
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Database Error", "Error deleting guide from the database.");
            }
        } else {
            showAlert(AlertType.WARNING, "Selection Error", "Please select a guide to delete.");
        }
    }

    @FXML
    public void updateGuide() {
        Guide selectedGuide = ListGuides.getSelectionModel().getSelectedItem();
        if (selectedGuide != null) {
            try {
                int oldCIN = selectedGuide.getCIN();
                int CIN = Integer.parseInt(cinField.getText());
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();
                String phoneNumber = phoneNumberField.getText();
                // Retrieve the selected gender RadioButton from the ToggleGroup
                RadioButton selectedGenderButton = (RadioButton) genderToggleGroup.getSelectedToggle();
                String gender = (selectedGenderButton != null) ? selectedGenderButton.getText() : "";
                String language = languageField.getValue(); // This will get the selected language from the ComboBox
                LocalDate dob = dobPicker.getValue();
                Double price = Double.parseDouble(priceField.getText());
                String imagePath = ""; // Default to an empty string if no image is selected

                // Assume you have an ImageView with fx:id="imageViewGuide"
                if (imageViewGuide.getImage() != null) {
                    imagePath = imageViewGuide.getImage().getUrl(); // Get the URL of the image
                }

                if (!validateFields()) {
                    return;
                }

                selectedGuide.setCIN(CIN);
                selectedGuide.setFirstname_g(firstName);
                selectedGuide.setLastname_g(lastName);
                selectedGuide.setEmailaddress_g(email);
                selectedGuide.setPhonenumber_g(phoneNumber);
                selectedGuide.setGender_g(gender);
                selectedGuide.setLanguage(language);
                selectedGuide.setDob(dob.toString());
                selectedGuide.setPrice(price);
                selectedGuide.setImage(imagePath);


                guideServices.update(selectedGuide, oldCIN);
                ListGuides.refresh();
                showAlert(AlertType.INFORMATION, "Success", "Guide updated successfully!");
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Invalid input", "Please enter a valid CIN.");
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Database Error", "Error updating guide in the database.");
            }
        } else {
            showAlert(AlertType.WARNING, "Selection Error", "Please select a guide to update.");
        }
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
}
