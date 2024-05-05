package controllers;


import Filter.ProfanityFilter;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import models.Message;
import Services.MessageService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import utils.UserSession;

public class chatClientController {
        @FXML
        public VBox chatPanel;

        @FXML
        private ListView<Message> messageList;
        @FXML
        private TextField messageField;
        private MessageService messageService;
        UserSession session = UserSession.getInstance();

    private ScheduledService<Void> refreshService;
        @FXML
        public void initialize() {
            System.out.println(session.getFirstname() + " - " + session.getId());
            try {
                messageService = new MessageService();
                updateMessages();
                setupMessageDisplay();
                startScheduledService();
            } catch (SQLException e) {
                e.printStackTrace();  // Consider better error handling or logging
            }
        }



    private void startScheduledService() {
        refreshService = new ScheduledService<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        updateMessages();
                        return null;
                    }
                };
            }
        };
        refreshService.setPeriod(Duration.seconds(5)); // refresh every 5 seconds
        refreshService.start();
    }
    public void stopService() {
        if (refreshService != null && refreshService.isRunning()) {
            refreshService.cancel();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void sendMessage() {
        String text = messageField.getText().trim();
        String mail = session.getEmail();
        if (!text.isEmpty()) {
            ProfanityFilter.containsProfanity(text).thenAccept(containsProfanity -> {
                Platform.runLater(() -> {
                    if (containsProfanity) {
                        showAlert("Please enter user-friendly words");
                    } else {
                        try {
                            // Proceed to send the message if no profanity is detected
                            Message message = new Message(text, LocalDateTime.now(), "Client" + mail, "client");
                            messageService.saveMessage(message, session.getId());
                            messageField.clear();
                            updateMessages();
                        } catch (Exception e) {
                            e.printStackTrace();
                            showAlert("Failed to send message. Please try again.");
                        }
                    }
                });
            }).exceptionally(ex -> {
                Platform.runLater(() -> showAlert("Failed to check message content. Please try again."));
                return null;
            });
        }
    }


    private void updateMessages() {
            try {
                List<Message> messages = messageService.getAllMessages();
                messageList.getItems().setAll(messages);
            } catch (SQLException e) {
                e.printStackTrace();  // Consider better error handling or displaying a user-friendly message
            }
        }

        private void setupMessageDisplay() {
            messageList.setCellFactory(lv -> new ListCell<Message>() {
                @Override
                protected void updateItem(Message message, boolean empty) {
                    super.updateItem(message, empty);
                    if (empty || message == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        String displayText = String.format(" %s :  %s       %s",  message.getSender(), message.getText(), message.getTimestamp());
                        setText(displayText);
                        if ("admin".equals(message.getRole())) {
                            setStyle("-fx-background-color: #add8e6;"); // Light blue for admin messages
                        } else {
                            setStyle("-fx-background-color: #90ee90;"); // Light green for client messages
                        }
                    }
                }
            });
        }
    }
