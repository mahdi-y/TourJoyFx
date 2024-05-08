package controllers;

import com.example.javafx.HelloApplication;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.categories;
import Services.ServiceCategories;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CatController {

    public Button RecB;
    @FXML
    private TableView<categories> View;

    @FXML
    private TableColumn<categories, Integer> idView;

    @FXML
    private Button nameAdd;

    @FXML
    private Button nameDelete;

    @FXML
    private TextField nameField;

    @FXML
    private Button nameUpdate;

    @FXML
    private TableColumn<categories, String> nameView;

    private Services.ServiceCategories ServiceCategories;



    @FXML
    void initialize() {

        ServiceCategories=new ServiceCategories();
        nameAdd.setOnAction(event -> {
            addCategories();

        });

        nameUpdate.setOnAction(event -> updateCategories());
        nameDelete.setOnAction(event -> deleteCategories());
        loadCategoriesData();
        setupTableColumns();
        // Add listener to handle row selection
        View.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Populate fields with selected country's data

                nameField.setText(newSelection.getName());

            }
        });
    }
    @FXML
    void addCategories() {
        try {
            String name = nameField.getText();


            if (name.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a category name.");
                return;
            }

            if (name.length() < 3 || name.length() > 16) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Category name must be between 3 and 16 characters.");
                return;
            }

            if (!name.matches("[\\w\\s-]+")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Category name contains invalid characters.");
                return;
            }
            if (!ServiceCategories.isNameUnique(name, null)) {
                showAlert(Alert.AlertType.ERROR, "Duplicate Name", "A category with this name already exists.");
                return;
            }
            categories newCategory = new categories(name);

            // Add category to database; the object newCategory will have its ID updated
            ServiceCategories.add(newCategory);

            // Add the newly added category to the ObservableList to update the TableView
            ObservableList<categories> currentItems = View.getItems();
            currentItems.add(newCategory);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Category added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();  // Print stack trace for detailed error information
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding category to the database.");
        }
    }

    private void setupTableColumns() {
        idView.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nameView.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

    }
    private void loadCategoriesData() {
        try {
            List<categories> categoriesList = ServiceCategories.Read();
            ObservableList<categories> observableCategories = FXCollections.observableArrayList(categoriesList);
            View.setItems(observableCategories);
        } catch (SQLException e) {
            e.printStackTrace();  // Handle exceptions, log them, and maybe show an error message to the user
        }
    }

    private void clearForm() {
        nameField.setText("");

    }
    @FXML
    void deleteCategories() {
        categories selectedCategory = View.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            try {
                ServiceCategories.delete(selectedCategory);
                View.getItems().remove(selectedCategory);
                clearForm();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Category deleted successfully!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error deleting category from the database.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a claim to delete.");
        }
    }

    @FXML
    void updateCategories() {
        categories selectedCategories = View.getSelectionModel().getSelectedItem();
        if (selectedCategories != null) {
            try {
                String name = nameField.getText();

                if (name.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a category name.");
                    return;
                }

                if (name.length() < 3 || name.length() > 16) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Category name must be between 3 and 16 characters.");
                    return;
                }

                if (!name.matches("[\\w\\s-]+")) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Category name contains invalid characters.");
                    return;
                }


                if (!ServiceCategories.isNameUnique(name, selectedCategories.getId())) {
                    showAlert(Alert.AlertType.ERROR, "Duplicate Name", "A category with this name already exists.");
                    return;
                }

                // Update selected country object
                selectedCategories.setName(name);


                // Update country in the database
                ServiceCategories.update(selectedCategories);

                // Refresh TableView
                View.refresh();

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "category updated successfully!");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid ID.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating category in the database.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a category to update.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleRecV() {
        try {
            // Load the second FXML file
            // Ensure that the FXMLLoader uses the correct class to find the resource relative to its location in the classpath.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafx/back.fxml"));
            // Change 'loader' to 'fxmlLoader' to match the initialized FXMLLoader object.
            Parent root = fxmlLoader.load();

            // Create a new stage or use an existing one
            Stage stage = new Stage();
            stage.setTitle("Claims List");  // Updated title to reflect the function
            stage.setScene(new Scene(root));
            stage.show();

            // Optionally hide the current stage if it's a full screen change
            // ((Stage) openSecondaryViewButton.getScene().getWindow()).hide();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
            // Consider showing an error alert to the user here as well
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
