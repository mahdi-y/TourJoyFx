package controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.categories;
import services.ServiceCategories;

import java.sql.SQLException;
import java.util.List;

public class CatController {

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

    private services.ServiceCategories ServiceCategories;



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

            if (name.trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a category name.");
                return;
            }

            categories newCategory = new categories(name);

            // Add category to database; the object newCategory will have its ID updated
            ServiceCategories.add(newCategory);

            // Add the newly added category to the ObservableList to update the TableView
            ObservableList<categories> currentItems = View.getItems();
            currentItems.add(newCategory);
            clearForm();
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





                // Update selected country object
                selectedCategories.setName(name);


                // Update country in the database
                ServiceCategories.update(selectedCategories);

                // Refresh TableView
                View.refresh();
                clearForm();
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

}
