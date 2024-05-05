package models;

import java.time.LocalDateTime;

public class Message {

        private int id;
        private String text;
        private LocalDateTime timestamp;
        private String sender;
        private String role;
        private int fkUser;

    public int getFkUser() {
        return fkUser;
    }

    public void setFkUser(int fkUser) {
        this.fkUser = fkUser;
    }

    public Message(int id, String text, LocalDateTime timestamp, String sender, String role) {
            this.id = id;
            this.text = text;
            this.timestamp = timestamp;
            this.sender = sender;
            this.role = role;

        }

    public Message(int id, String text, LocalDateTime timestamp, String sender, String role, int fkUser) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.sender = sender;
        this.role = role;
        this.fkUser = fkUser;
    }

    public Message(String text, LocalDateTime timestamp, String sender, String role, int fkUser) {
//        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.sender = sender;
        this.role = role;
        this.fkUser = fkUser;
    }
    public Message() {
    }

    public Message(String text, LocalDateTime timestamp, String sender, String role) {
        this.text = text;
        this.timestamp = timestamp;
        this.sender = sender;
        this.role = role;

    }

    public int getId() {
        return id;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getText() {
        return text;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getSender() {
        return sender;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


    @Override
    public String toString() {
        return
                sender +

                text
                +
                        timestamp ;
    }
}



