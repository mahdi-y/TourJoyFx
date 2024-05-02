package entities;

public class Monument {
    private int id;
    private Country country;
    private String name;
    private String type;
    private int entryPrice;
    private String status;
    private String description;
    private double latitude;  // Changed from int to double
    private double longitude; // Changed from int to double
    private String imagePath;
    private Integer convertedEntryPrice; // Additional field for the converted price


    // Default constructor
    public Monument() {
    }

    // Full constructor including ID
    public Monument(int id, Country country, String name, String type, int entryPrice,
                    String status, String description, double latitude, double longitude, String imagePath) {
        this.id = id;
        this.country = country;
        this.name = name;
        this.type = type;
        this.entryPrice = entryPrice;
        this.status = status;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imagePath = imagePath;
    }

    // Constructor without the id field
    public Monument(Country country, String name, String type, int entryPrice,
                    String status, String description, double latitude, double longitude, String imagePath) {
        this.country = country;
        this.name = name;
        this.type = type;
        this.entryPrice = entryPrice;
        this.status = status;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imagePath = imagePath;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
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

    public int getEntryPrice() {
        // If convertedEntryPrice is not null, return it. Otherwise, return the original entryPrice.
        return (convertedEntryPrice != null) ? convertedEntryPrice : entryPrice;
    }
    public void setEntryPrice(int entryPrice) {
        this.entryPrice = entryPrice;
    }
    public Integer getConvertedEntryPrice() {
        return convertedEntryPrice;
    }

    public void setConvertedEntryPrice(Integer convertedEntryPrice) {
        this.convertedEntryPrice = convertedEntryPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Monument{" +
                "id=" + id +
                ", country=" + country +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", entryPrice=" + entryPrice +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
