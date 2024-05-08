package Entities;

public class Country {
    private int id;
    private String name;
    private String region;
    private boolean visa_required;

    public Country( String name, String region, boolean visa_required) {
        // this.id = id;
        this.name = name;
        this.region = region;
        this.visa_required = visa_required;
    }

    public Country() {

    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public boolean isVisa_required() {
        return visa_required;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setVisa_required(boolean visa_required) {
        this.visa_required = visa_required;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", visa_required=" + visa_required +
                '}';
    }
}