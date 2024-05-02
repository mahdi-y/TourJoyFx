package Entities;
import java.util.ArrayList;
import java.util.List;
public class Accomodation {
    private Integer refA;
    private String name;
    private String type;
    private String location;
    private float price;
    private Integer nb_rooms;

    private String image_name;

    private List<String> photoNames;
    public Accomodation(Integer refA, String name, String type, String location, float price, Integer nb_rooms, String image_name) {
        this.refA = refA;
        this.name = name;
        this.type = type;
        this.location = location;
        this.price = price;
        this.nb_rooms = nb_rooms;
        this.image_name = image_name;
    }
//constructor without refA
    public Accomodation(String name, String type, String location, float price, int nb_rooms, String image_name) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.price = price;
        this.nb_rooms = nb_rooms;
        this.image_name= image_name;
    }

    public int getRefA() {
        return refA;
    }

    public void setRefA(int refA) {
        this.refA = refA;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getNb_rooms() {
        return nb_rooms;
    }

    public void setNb_rooms(int nb_rooms) {
        this.nb_rooms = nb_rooms;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    @Override
    public String toString() {
        return "Accomodation{" +
                "refA=" + refA +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", price=" + price +
                ", nb_rooms=" + nb_rooms +
                ", image_name='" + image_name + '\'' +
                '}';
    }
}
