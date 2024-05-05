package Entities;

import java.sql.Date;

public class Reservation {
    private int idR;
    private Date start_date;


    private Date end_date;
    private int name; // Foreign key to Accommodation
    private transient String accommodationName;
    public Reservation(int idR, Date start_date, Date end_date, int name) {
        this.idR = idR;
        this.start_date = start_date;
        this.end_date = end_date;
        this.name=name;
    }

    public Reservation(Date start_date, Date end_date, int name) {
        this.start_date = start_date;
        this.end_date = end_date;
        this.name = name;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getIdR() {
        return idR;
    }

    public void setIdR(int idR) {
        this.idR = idR;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date endDate) {
        this.end_date = endDate;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "idR=" + idR +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", name=" + name +
                '}';
    }
}
