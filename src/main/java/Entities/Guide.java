package Entities;

public class Guide {
    private int CIN; // Assuming CIN is the primary key and not auto-incremented in the database
    private String firstname_g;
    private String lastname_g;
    private String emailaddress_g;
    private String phonenumber_g;
    private String gender_g;
    private String language;
    private String dob;

    // Constructors
    public Guide() {}

    public Guide(int CIN, String firstname_g, String lastname_g, String emailaddress_g,
                 String phonenumber_g, String gender_g, String language, String dob) {
        this.CIN = CIN;
        this.firstname_g = firstname_g;
        this.lastname_g = lastname_g;
        this.emailaddress_g = emailaddress_g;
        this.phonenumber_g = phonenumber_g;
        this.gender_g = gender_g;
        this.language = language;
        this.dob = dob;
    }

    public Guide(String firstname_g, String lastname_g, String emailaddress_g,
                 String phonenumber_g, String gender_g, String language, String dob) {
        this.firstname_g = firstname_g;
        this.lastname_g = lastname_g;
        this.emailaddress_g = emailaddress_g;
        this.phonenumber_g = phonenumber_g;
        this.gender_g = gender_g;
        this.language = language;
        this.dob = dob;
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
                '}';
    }
}
