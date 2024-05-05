package Entities;

import java.time.LocalDate;

public class Booking {
    private int id;
    private int guide;
    private int user_id; // Foreign key to User

    private LocalDate date;
    private String status; // Added status attribute

    public Booking(int id, int guide, LocalDate date, String status) {
        this.id = id;
        this.guide = guide;
        this.date = date;
        this.status = status;
    }

    public Booking(int id, int guide, int user_id, LocalDate date, String status) {
        this.id = id;
        this.guide = guide;
        this.user_id = user_id;
        this.date = date;
        this.status = status;
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuide() {
        return guide;
    }

    public void setGuide(int guide) {
        this.guide = guide;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", guide=" + guide +
                ", date=" + date +
                ", status='" + status + '\'' + // Include status in toString
                '}';
    }
}