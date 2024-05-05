package controllers;

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
import services.MessageService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ChatController {
    @FXML
    public VBox chatPanel;

    @FXML
    private ListView<Message> messageList;
    @FXML
    private TextField messageField;
    private MessageService messageService;
    private ScheduledService<Void> refreshService;
    private LocalDateTime lastUpdatedTime = LocalDateTime.now().minusDays(1); // Initialize to a time in the past

    @FXML
    public void initialize() {
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




    public void sendMessage() {
        String text = messageField.getText().trim();
        if (!text.isEmpty()) {
            try {
                Message message = new Message(text, LocalDateTime.now(), "Tourjoy", "admin");
                messageService.saveMessage(message);
                messageField.clear();
                updateMessages();
            } catch (SQLException e) {
                e.printStackTrace();  // Consider showing a user-friendly error message
            }
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
