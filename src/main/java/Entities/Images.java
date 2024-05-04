package Entities;

public class Images {

    private int id;
    private int accommodation;
    private String name;

    public Images(int accommodation) {
        this.accommodation = accommodation;
    }

    public Images(String name) {
        this.name = name;
    }

    public Images(int id, int accommodation, String name) {
        this.id = id;
        this.accommodation = accommodation;
        this.name = name;
    }
//constructor without id
    public Images(int accommodation, String name) {
        this.accommodation = accommodation;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(int accommodation) {
        this.accommodation = accommodation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Images{" +
                "id=" + id +
                ", accommodation=" + accommodation +
                ", name='" + name + '\'' +
                '}';
    }
}
