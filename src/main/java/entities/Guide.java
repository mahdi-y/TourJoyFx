package entities;

import services.ServiceCountry;

public class Guide {
    private int CIN; // Assuming CIN is the primary key and not auto-incremented in the database
    private String firstname_g;
    private String lastname_g;
    private String emailaddress_g;
    private String phonenumber_g;
    private String gender_g;
    private String language;
    private String dob;
    private double price; // Price attribute moved before image
    private String image; // Image path or URL
    private int country_id; // Foreign key to Country entity


    public Guide(int CIN, String firstname_g, String lastname_g, String emailaddress_g,
                 String phonenumber_g, String gender_g, String language, String dob, double price, String image, int country_id) {
        this.CIN = CIN;
        this.firstname_g = firstname_g;
        this.lastname_g = lastname_g;
        this.emailaddress_g = emailaddress_g;
        this.phonenumber_g = phonenumber_g;
        this.gender_g = gender_g;
        this.language = language;
        this.dob = dob;
        this.price = price;
        this.image = image;
        this.country_id = country_id;
    }


    public Guide(String firstname_g, String lastname_g, String emailaddress_g,
                 String phonenumber_g, String gender_g, String language, String dob, double price, String image, Country country_id) {
        this.firstname_g = firstname_g;
        this.lastname_g = lastname_g;
        this.emailaddress_g = emailaddress_g;
        this.phonenumber_g = phonenumber_g;
        this.gender_g = gender_g;
        this.language = language;
        this.dob = dob;
        this.price = price; // Initialize price before image
        this.image = image;
        this.country_id = country_id.getId();
    }

    public Guide(  String firstName, String lastName, String email, String phoneNumber, String gender, String language, String dob, double price, String imagePath, int country_id) {
    }

    // Getters and Setters

    public int getCIN() {
        return CIN;
    }

    public void setCIN(int CIN) {
        this.CIN = CIN;
    }

    public String getFirstname_g() {
        return firstname_g;
    }

    public void setFirstname_g(String firstname_g) {
        this.firstname_g = firstname_g;
    }

    public String getLastname_g() {
        return lastname_g;
    }

    public void setLastname_g(String lastname_g) {
        this.lastname_g = lastname_g;
    }

    public String getEmailaddress_g() {
        return emailaddress_g;
    }

    public void setEmailaddress_g(String emailaddress_g) {
        this.emailaddress_g = emailaddress_g;
    }

    public String getPhonenumber_g() {
        return phonenumber_g;
    }

    public void setPhonenumber_g(String phonenumber_g) {
        this.phonenumber_g = phonenumber_g;
    }

    public String getGender_g() {
        return gender_g;
    }

    public void setGender_g(String gender_g) {
        this.gender_g = gender_g;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCountry() {
        return country_id;
    }

    public void setCountry(int country_id) {
        this.country_id = country_id;
    }
    public String getCountryName() {
        String countryName = null;
        try {
            // Implement logic to retrieve the country name based on the country_id
            ServiceCountry countryService = new ServiceCountry(); // Assuming you have a CountryService class
            countryName = countryService.getCountryNameById(country_id);
        } catch (Exception e) {
            System.out.println("Error getting country name: " + e.getMessage());
            e.printStackTrace();
        }
        return countryName;
    }

    @Override
    public String toString() {
        return "Guide{" +
                "CIN=" + CIN +
                ", firstname_g='" + firstname_g + '\'' +
                ", lastname_g='" + lastname_g + '\'' +
                ", emailaddress_g='" + emailaddress_g + '\'' +
                ", phonenumber_g='" + phonenumber_g + '\'' +
                ", gender_g='" + gender_g + '\'' +
                ", language='" + language + '\'' +
                ", dob='" + dob + '\'' +
                ", price=" + price +  // Price displayed before image in the toString method
                ", image='" + image + '\'' +
                ", country_id=" + country_id + // Include country in the toString method
                '}';
    }
}
