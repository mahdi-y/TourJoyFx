package entities;

import java.time.LocalDate;

public class Booking {
    private int id;
    private int guide;
    private LocalDate date;

    public Booking(int id, int guide, LocalDate date) {
        this.id = id;
        this.guide = guide;
        this.date = date;
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

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", guide=" + guide +
                ", date=" + date +
                '}';
    }
}
