package controllers;

import com.example.tourjoy.HelloApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.User;
import Services.userService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;

public class usersList {
    public Button deleteButton;
    public Button frontoffice;
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> idColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
//    @FXML
//    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, Integer> phoneNumberColumn;
    @FXML
    private TableColumn<User, String> countryColumn;
    @FXML
    private TableColumn<User, String> creationDateColumn;
    @FXML
    private TableColumn<User, Boolean> isVerifiedColumn;
    @FXML
    private TableColumn<User, Boolean> isBannedColumn;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private Services.userService userService;

    @FXML
    public void initialize() {
        userService = new userService();
        setupTableColumns();
        loadUsers();

        deleteButton.setOnAction(event -> {
            User selectedUser = usersTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                try {
                    boolean deleted = userService.deleteUser(selectedUser);
                    if (deleted) {
                        showAlert(Alert.AlertType.INFORMATION, "User Deleted", "User successfully deleted.");
                        usersTable.getItems().remove(selectedUser);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Delete Failed", "Failed to delete user.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Delete Error", "An error occurred while deleting user.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "No User Selected", "Please select a user to delete.");
            }
        });
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
//        roleColumn.setCellValueFactory(cellData ->
//                new SimpleStringProperty(String.join(", ", cellData.getValue().getRoles())));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        creationDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCreatedAt().format(formatter)));
        isVerifiedColumn.setCellValueFactory(new PropertyValueFactory<>("verified"));
        isBannedColumn.setCellValueFactory(new PropertyValueFactory<>("banned"));

    }

    private void loadUsers() {
        try {
            List<User> users = userService.fetchAllUsers(); // Fetch all users
            ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
            usersTable.setItems(observableUsers);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Data Load Error", "Could not load user data from the database.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
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


    public void front(ActionEvent actionEvent) throws IOException {
        HelloApplication.loadFXML("/Home.fxml");
    }

    public void exportUsersToExcel() {
        String[] columns = {"ID", "Email", "First Name", "Last Name", "Phone Number", "Country", "Verified", "Banned"};
        try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream("F:/TJv2/TJv2/src/main/resources/image/Users.xsls")) {
            Sheet sheet = workbook.createSheet("Users");

            // Create a header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Fill data
            int rowNum = 1;
            for (User user : usersTable.getItems()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getEmail());
                row.createCell(2).setCellValue(user.getFirstName());
                row.createCell(3).setCellValue(user.getLastName());
                row.createCell(4).setCellValue(user.getPhoneNumber());
                row.createCell(5).setCellValue(user.getCountry());
                row.createCell(6).setCellValue(user.isVerified());
                row.createCell(7).setCellValue(user.isBanned());
            }

            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the output to a file
            workbook.write(fileOut);
            showAlert(Alert.AlertType.INFORMATION, "Export Successful", "Users data exported successfully to Users.xlsx");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Export Failed", "Failed to export users data.");
            e.printStackTrace();
        }
    }

    public void exportExcel(MouseEvent mouseEvent) {
        exportUsersToExcel();
    }

    @FXML
    public void ban(ActionEvent actionEvent) {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                userService.banUser(selectedUser); // Assume this method updates the banned status in the database
                selectedUser.setBanned(true);
                usersTable.refresh();
                showAlert(Alert.AlertType.INFORMATION, "User Banned", "The user has been successfully banned.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Banning Failed", "Failed to ban the user.");
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No User Selected", "Please select a user to ban.");
        }
    }
    @FXML
    public void unban(ActionEvent actionEvent) {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                userService.unBanUser(selectedUser); // Assume this method updates the banned status in the database
                selectedUser.setBanned(false);
                usersTable.refresh();
                showAlert(Alert.AlertType.INFORMATION, "User Unbanned", "The user has been successfully unbanned.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Unbanning Failed", "Failed to unban the user.");
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No User Selected", "Please select a user to ban.");
        }
    }

    @FXML
    public void makeAdmin(ActionEvent actionEvent) {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                String newRoles = selectedUser.getRoles() + ",ROLE_ADMIN";
                userService.updateUserRoles(selectedUser.getId(), newRoles);
                showAlert(Alert.AlertType.INFORMATION, "Update Successful", "User has been granted admin rights.");
                // Refresh user's display or roles view here if necessary
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update user roles.");
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No User Selected", "Please select a user to grant admin rights.");
        }
    }
}