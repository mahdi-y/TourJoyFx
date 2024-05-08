package Controller;
//import com.sun.javafx.tk.quantum.PaintRenderJob;
import javafx.scene.image.Image;

import java.awt.Robot;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import Entities.QRCodeGenerator;
import Entities.PDFGenerator;
import Entities.Subscription;
import Entities.Transport;
import Services.SubscriptionService;
import Services.TransportService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.example.javafx.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;


import com.google.zxing.WriterException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class SubscriptionController<PdfDocument> implements Initializable {

    private TransportService transportService;
    private SubscriptionService subscriptionService;
    private Map<String, Integer> typeToIdMap;
    private final String defaultLanguage = "en"; // Default language
    @FXML
    private Button gotoGuideManagement;
    @FXML
    private Button gotoMonumentManagement;
    @FXML
    private Button frontoffice;

    @FXML
    private Button user;

    public SubscriptionController() {
        transportService = new TransportService();
        subscriptionService = new SubscriptionService();
    }

    @FXML
    private Button generateQRCodeButton;
    @FXML
    private ImageView qrCodeImageView;


    @FXML
    private TableView<Subscription> subscriptionTable;

    @FXML
    private TableColumn<Subscription, Long> idColumn;


    @FXML
    private TableColumn<Subscription, String> planColumn;

    @FXML
    private TableColumn<Subscription, Integer> durationColumn;

    @FXML
    private TableColumn<Subscription, String> typeColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button deleteButton;

    @FXML
    private TextField planTextField;

    @FXML
    private TextField durationTextField;

    @FXML
    private ChoiceBox<String> typeChoiceBox;
    @FXML
    private ChoiceBox<String> languageChoiceBox;
    @FXML
    private Button TransportButton;
    @FXML
    private TextField searchSubscriptionTextField;
    @FXML
    private Button ttsButton;




    private void initializeDataTable() {
        try {
            List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
            subscriptionTable.getItems().setAll(subscriptions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleRefreshButtonAction(ActionEvent event) {
        initializeDataTable();
    }

    public void handleAddButtonAction(ActionEvent event) {
        String plan = planTextField.getText();
        String durationText = durationTextField.getText();
        String type = typeChoiceBox.getValue();

        if (plan.isEmpty() || durationText.isEmpty() || type == null) {
            // Handle validation errors
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        if (plan.matches(".*\\d.*")) {
            // Handle validation errors
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Plan must be a string.");
            alert.showAndWait();
            return;
        }

        try {
            int duration = Integer.parseInt(durationText);
            if (duration <= 0) {
                // Handle validation errors
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Duration must be a positive number.");
                alert.showAndWait();
                return;
            }

            int typeId = typeToIdMap.get(type);
            Transport transport = new Transport(typeId, type);
            Subscription subscription = new Subscription(0L, plan, duration, transport);

            if (subscriptionService.checkIfSubscriptionExists(plan, type)) {
                // Handle subscription already exists error
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Subscription already exists.");
                alert.showAndWait();
                return;
            }

            subscriptionService.addSubscription(subscription);
            initializeDataTable(); // Reload the subscriptions from the database

            planTextField.clear();
            durationTextField.clear();
            typeChoiceBox.setValue(null);
        } catch (NumberFormatException e) {
            // Handle validation errors
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Duration must be a number.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleUpdateButtonAction(ActionEvent event) {
        Subscription selectedSubscription = subscriptionTable.getSelectionModel().getSelectedItem();
        if (selectedSubscription != null) {
            String newPlan = planTextField.getText();
            int newDuration = Integer.parseInt(durationTextField.getText());
            String newType = typeChoiceBox.getValue();

            if (newPlan.isEmpty() || newDuration <= 0 || newType == null) {
                // Handle validation errors
                return;
            }

            try {
                int typeId = typeToIdMap.get(newType);
                Transport transport = new Transport(typeId, newType);

                selectedSubscription.setPlan(newPlan);
                selectedSubscription.setDuration(newDuration);
                selectedSubscription.setTypeT(transport);

                subscriptionService.updateSubscription(selectedSubscription);
                initializeDataTable(); // Reload the subscriptions from the database

                planTextField.clear();
                durationTextField.clear();
                typeChoiceBox.setValue(null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleDeleteButtonAction(ActionEvent event) {
        Subscription selectedSubscription = subscriptionTable.getSelectionModel().getSelectedItem();
        if (selectedSubscription != null) {
            try {
                subscriptionService.deleteSubscription(selectedSubscription.getId());
                subscriptionTable.getItems().remove(selectedSubscription);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    //initializeTTS();
        List<Transport> transports;
        try {
            transports = transportService.getAllTransports();
            if (transports != null) {
                ObservableList<String> typeList = FXCollections.observableArrayList();
                typeToIdMap = new HashMap<>();
                for (Transport transport : transports) {
                    typeList.add(transport.getType_t());
                    typeToIdMap.put(transport.getType_t(), transport.getId());
                }
                typeChoiceBox.setItems(typeList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        planColumn.setCellValueFactory(new PropertyValueFactory<>("plan"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        typeColumn.setCellValueFactory(cellData -> {
            Transport transport = cellData.getValue().getTypeT();
            return transport == null ? null : new SimpleStringProperty(transport.getType_t());
        });

        initializeDataTable();
        // Add listener to handle selection changes
        subscriptionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                planTextField.setText(newSelection.getPlan());
                durationTextField.setText(String.valueOf(newSelection.getDuration()));
                typeChoiceBox.setValue(newSelection.getTypeT().getType_t());
            }
        });

    }
    @FXML
    private void goToTransport(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafx/Transport.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) TransportButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSearchButtonAction(ActionEvent actionEvent) {
        String keyword = searchSubscriptionTextField.getText();
        try {
            List<Subscription> searchResult = subscriptionService.searchSubscriptionsByPlan(keyword);
            // Clear the table and add the search results
            subscriptionTable.getItems().clear();
            subscriptionTable.getItems().addAll(searchResult);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void genererpdf(ActionEvent event) throws SQLException {

        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        String filePath = "susbcriptions.pdf";
        String imagePath = "F:\\TJv2\\TJv2\\src\\main\\resources\\com\\example\\javafx\\images\\tourjoy.png";
        try {
            PDFGenerator.generatePDF(subscriptions, filePath,imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void generateStatistics() throws SQLException {
        // Retrieve the list of subscriptions from your data source
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions(); // Your data retrieval logic here

        int countLessThan50 = 0;
        int countGreaterThan50 = 0;

        // Calculate the number of subscriptions falling into each category
        for (Subscription subscription : subscriptions) {
            if (subscription.getDuration() < 50) {
                countLessThan50++;
            } else {
                countGreaterThan50++;
            }
        }

        // Create a custom dialog
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Subscription Statistics");

        // Create a pie chart
        PieChart pieChart = new PieChart();
        pieChart.getData().add(new PieChart.Data("Duration < 50", countLessThan50));
        pieChart.getData().add(new PieChart.Data("Duration > 50", countGreaterThan50));

        // Create a VBox to hold the pie chart and labels
        VBox vBox = new VBox();
        vBox.getChildren().addAll(new Label("Subscription Statistics"), pieChart);

        // Set the dialog content
        dialog.getDialogPane().setContent(vBox);

        // Add an OK button to the dialog
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        // Show the dialog
        dialog.showAndWait();
    }


    @FXML
    private void generateQRCode(ActionEvent event) {
        Subscription selectedSubscription = subscriptionTable.getSelectionModel().getSelectedItem();
        if (selectedSubscription != null) {
            try {
                byte[] qrCodeBytes = QRCodeGenerator.generateQRCodeImage(selectedSubscription, 300, 300); // Adjust width and height as needed
                Image qrCodeImage = convertToJavaFXImage(qrCodeBytes);

                // Create an alert dialog
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("QR Code");
                alert.setHeaderText(null);
                alert.setContentText("QR Code for the selected subscription:");

                // Create an image view to display the QR code
                ImageView imageView = new ImageView(qrCodeImage);
                imageView.setFitWidth(200); // Adjust width as needed
                imageView.setFitHeight(200); // Adjust height as needed
                alert.getDialogPane().setContent(imageView);

                // Add OK button to the alert
                alert.getButtonTypes().setAll(ButtonType.OK);

                // Show the alert
                alert.showAndWait();
            } catch (WriterException | IOException e) {
                e.printStackTrace();
                // Handle exception
            }
        }
    }
    private Image convertToJavaFXImage(byte[] bytes) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        return new Image(inputStream);
    }
    /*private void initializeTTS() {
        System.out.println("Initializing TTS...");

        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice[] voices = voiceManager.getVoices();

        System.out.println("Available voices:");
        for (Voice voice : voices) {
            System.out.println("- " + voice.getName());
        }

        Voice voice = voiceManager.getVoice("alan");
        if (voice == null) {
            System.err.println("Cannot find a voice named alan.");
            return;
        }

        voice.allocate();
        System.out.println("TTS initialized successfully.");
    }
    @FXML
    private void handleTTSButtonAction(ActionEvent event) {
        Subscription selectedSubscription = subscriptionTable.getSelectionModel().getSelectedItem();
        if (selectedSubscription != null) {
            String textToSpeak = "Subscription details: " +
                    "Plan: " + selectedSubscription.getPlan() +
                    ", Duration: " + selectedSubscription.getDuration() +
                    ", Type: " + selectedSubscription.getTypeT().getType_t();
            System.out.println("Text to speak: " + textToSpeak);
            speak(textToSpeak);
        } else {
            System.err.println("No subscription selected.");
        }
    }
    private void speak(String text) {
        Voice voice = VoiceManager.getInstance().getVoice("alan");
        if (voice != null) {
            System.out.println("Speaking: " + text);
            voice.speak(text);
            System.out.println("Speech completed.");
        } else {
            System.err.println("Cannot find a voice named alan.");
        }
    }*/
    public static void prepareTextField() {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            int centerX = screenRect.width / 2;
            int centerY = screenRect.height / 2;

            // Move mouse to the center of the screen and click to focus
            robot.mouseMove(centerX, centerY);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(500);  // Adjust this delay as necessary

            // Press Ctrl+A to select all text
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_A);
            robot.keyRelease(KeyEvent.VK_A);
            robot.keyRelease(KeyEvent.VK_CONTROL);

            // Press Backspace to delete all selected text
            robot.keyPress(KeyEvent.VK_BACK_SPACE);
            robot.keyRelease(KeyEvent.VK_BACK_SPACE);

            Thread.sleep(500);  // Allow some time for the application to react

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void typeString(String text) {
        try {
            Robot robot = new Robot();

            for (char c : text.toCharArray()) {
                int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
                if (Character.isUpperCase(c)) {
                    robot.keyPress(KeyEvent.VK_SHIFT);
                }
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);

                if (Character.isUpperCase(c)) {
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                }

                robot.delay(50); // Adding a small delay to mimic human typing speed
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void speakSubscriptionDetails(String details) throws IOException {
        try {
            String espeakPath = "C:\\Program Files (x86)\\eSpeak\\TTSApp.exe";

            // Start the external application
            Process process = Runtime.getRuntime().exec(espeakPath);

            // Wait a bit to ensure the application is open and ready
            Thread.sleep(2000); // Adjust the timing as necessary

            // Prepare the text field by clicking, selecting all, and pressing escape
            prepareTextField();

            // Type the details into the text field
            typeString(details);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleTTSButtonAction(ActionEvent event) throws IOException {
        // Get the selected subscription
        Subscription selectedSubscription = subscriptionTable.getSelectionModel().getSelectedItem();

        if (selectedSubscription != null) {
            // Compose speech text from subscription details
            String speechText = "Subscription details: Plan: " + selectedSubscription.getPlan() +
                    ", Duration: " + selectedSubscription.getDuration() + " Days" +
                    ", Transport Type: " + selectedSubscription.getTypeT().getType_t();

            // Call the speech service to speak the details
            System.out.println(speechText);
            speakSubscriptionDetails(speechText);
        }
    }

    private Stage getPrimaryStage() {
        return HelloApplication.getPrimaryStage();
    }
    public void minimizeWindow(ActionEvent actionEvent) {
        getPrimaryStage().setIconified(true);
    }

    public void expandWindow(ActionEvent actionEvent) {
        Stage stage = getPrimaryStage();
        stage.setMaximized(!stage.isMaximized());
    }
    public void closeWindow(ActionEvent actionEvent) {
        getPrimaryStage().close();
    }

    public void front(ActionEvent actionEvent) throws IOException {
        HelloApplication.loadFXML("/com/test/tjv2/Home.fxml");
    }

    public void gotoGuideManagement(javafx.event.ActionEvent actionEvent) {
        URL url = getClass().getResource("/AddGuide.fxml");
        if (url == null) {
            System.err.println("Cannot find Guides.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                gotoGuideManagement.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }
    public void gotoMonumentManagement(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/test/tjv2/Monument.fxml");
        if (url == null) {
            System.err.println("Cannot find Monument.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                gotoMonumentManagement.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }
    public void gotoUserManagment(ActionEvent actionEvent) {
        URL url = getClass().getResource("/usersList.fxml");
        if (url == null) {
            System.err.println("Cannot find Monument.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                user.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }

    public void goToClaimsManagement(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/example/javafx/back.fxml");
        if (url == null) {
            System.err.println("Cannot find back.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                frontoffice.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }




    public void goToAccManagement(ActionEvent actionEvent) {
        URL url = getClass().getResource("/com/example/javafx/Accomodation.fxml");
        if (url == null) {
            System.err.println("Cannot find back.fxml");
        } else {
            try {
                Parent root = FXMLLoader.load(url);
                frontoffice.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace(); // This prints the stack trace to help diagnose the issue
            }
        }
    }
}
