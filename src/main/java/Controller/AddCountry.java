package Controller;

import Entities.Country;
import Services.ServiceCountry;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class AddCountry {

    public Button btn_workbench;
    public Button btn_workbench211;
    public AnchorPane side_ankerpane;
    public Pane inner_pane;
    public Pane most_inner_pane;
    public Button btn_workbench2;
    public Button btn_workbench21;
    public Button btn_workbench1;
    public Button btn_workbench11;
    public Button clearButton1;
    @FXML
    private TableView<Country> DisplayCountries;

    @FXML
    private TableColumn<Country, Integer> idColumn;

    @FXML
    private TableColumn<Country, String> nameColumn;

    @FXML
    private TableColumn<Country, String> regionColumn;

    @FXML
    private TableColumn<Country, Boolean> visaRequiredColumn;

//    @FXML
  //  private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> regionComboBox;

    @FXML
    private CheckBox visaRequiredCheckbox;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    private ServiceCountry serviceCountry;

    @FXML
    void initialize() {

        serviceCountry = new ServiceCountry();
       // idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        regionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRegion()));

        // Modify visaRequiredColumn's CellValueFactory to display "Yes" or "No"
        // Modify visaRequiredColumn's CellValueFactory to display "Yes" or "No"
        visaRequiredColumn.setCellValueFactory(cellData -> {
            boolean visaRequired = cellData.getValue().isVisa_required();
            return new SimpleBooleanProperty(visaRequired);
        });

        // Optionally, you can set a custom cell factory to style the cell if you want
        visaRequiredColumn.setCellFactory(column -> new TableCell<Country, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item ? "Yes" : "No");
                    // Optionally, you can add styles to the cell based on the value
                }
            }
        });

        addButton.setOnAction(event -> addCountry());
        deleteButton.setOnAction(event -> deleteCountry());
        updateButton.setOnAction(event -> updateCountry());

        // Populate region ComboBox with continents
        regionComboBox.getItems().addAll(
                "Africa", "Antarctica", "Asia", "Europe", "North America", "Oceania", "South America"
        );

        // Load countries into the table
        try {
            List<Country> listCountries = serviceCountry.Read();
            DisplayCountries.getItems().addAll(listCountries);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch countries from the database.");
        }

        // Add listener to handle row selection
        DisplayCountries.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Populate fields with selected country's data
               // idField.setText(String.valueOf(newSelection.getId()));
                nameField.setText(newSelection.getName());
                regionComboBox.setValue(newSelection.getRegion());
                visaRequiredCheckbox.setSelected(newSelection.isVisa_required());
            }
        });
    }
    private boolean validateCountryName(String name) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://restcountries.com/v3.1/name/" + name + "?fullText=true");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @FXML
    void addCountry() {
        try {
            String name = nameField.getText();
            String region = regionComboBox.getValue();
            boolean visaRequired = visaRequiredCheckbox.isSelected();

            // Validate fields
            if (name.isEmpty() || region == null) {
                showAlert(Alert.AlertType.ERROR, "Required fields", "Please fill in all mandatory fields.");
                return;
            }

            // Validate country name (no numbers)
            if (name.matches(".*\\d+.*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid name", "Country name should not contain numbers.");
                return;
            }

            // Validate against a list of real countries
            if (!validateCountryName(name)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Country", "The country name is not recognized.");
                return;
            }

            // Create country object
            Country country = new Country(name, region, visaRequired);

            // Add country to database
            serviceCountry.add(country);

            // Update TableView
            DisplayCountries.getItems().add(country);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Country added successfully!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid ID.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding country to the database.");
        }
    }
    @FXML
    void deleteCountry() {
        Country selectedCountry = DisplayCountries.getSelectionModel().getSelectedItem();
        if (selectedCountry != null) {
            try {
                serviceCountry.delete(selectedCountry);
                DisplayCountries.getItems().remove(selectedCountry);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Country deleted successfully!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error deleting country from the database.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a country to delete.");
        }
    }
    @FXML
    void updateCountry() {
        Country selectedCountry = DisplayCountries.getSelectionModel().getSelectedItem();
        if (selectedCountry != null) {
            try {
              //  int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String region = regionComboBox.getValue();
                boolean visaRequired = visaRequiredCheckbox.isSelected();

                // Validate fields
                if (name.isEmpty() || region == null) {
                    showAlert(Alert.AlertType.ERROR, "Required fields", "Please fill in all mandatory fields.");
                    return;
                }

                // Validate country name (no numbers)
                if (name.matches(".*\\d+.*")) {
                    showAlert(Alert.AlertType.ERROR, "Invalid name", "Country name should not contain numbers.");
                    return;
                }

                // Update selected country object
              //  selectedCountry.setId(id);
                selectedCountry.setName(name);
                selectedCountry.setRegion(region);
                selectedCountry.setVisa_required(visaRequired);

                // Update country in the database
                serviceCountry.update(selectedCountry);

                // Refresh TableView
                DisplayCountries.refresh();

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Country updated successfully!");

            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating country in the database.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a country to update.");
        }
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
                // Get the current scene and set the root
                Scene scene = addButton.getScene(); // You can use any control present in the scene
                scene.setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    @FXML
    public void goToCountry(ActionEvent event) {
        URL url = getClass().getResource("/com/test/tjv2/Country.fxml");
        if (url == null) {
            System.err.println("Cannot find Country.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                // Get the current scene and set the root
                Scene scene = addButton.getScene(); // You can use any control present in the scene
                scene.setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }
    private void clearFields() {
        nameField.clear();
        visaRequiredCheckbox.setSelected(false);
    }
    public void Clearc(ActionEvent actionEvent) {
        clearFields();;
    }
}