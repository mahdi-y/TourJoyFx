package utils;

import models.User;

import java.util.Arrays;

public final class UserSession {

    private static UserSession instance;

    private int id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;

    private String country;
    private String picture;
    private String phonenumber;

    private String role;

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }
    public String getPicture() { return picture; }

    public String getCountry() {return country;}
    public String getPhonenumber() { return phonenumber; }
    public String getRole() { return role; }

    private UserSession() { }

    public UserSession(int id, String email, String password, String firstname, String lastname, String country, String picture, String phonenumber, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.country = country;
        this.picture = picture;
        this.phonenumber = phonenumber;
        this.role = role;
    }


    public void setUser(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstname = user.getFirstName();
        this.lastname = user.getLastName();
        this.country = user.getCountry();
        this.picture = user.getProfilePicture();
        this.phonenumber = user.getPhoneNumber().toString();
        this.role = Arrays.toString(user.getRoles());
    }

    public static UserSession getInstance(int id, String email, String password, String firstname, String lastname
            , String country,String picture, String phonenumber, String role) {
        if(instance == null) {
            instance = new UserSession(id, email, password, firstname, lastname, country, picture, phonenumber, role);
        }
        return instance;
    }

    public static UserSession getInstance() {
        if (instance == null) {
            throw new IllegalStateException("UserSession has not been initialized.");
        }
        return instance;
    }


    public void cleanUserSession() {
        id = 0;

        email = null;
        password = null;
        firstname = null;
        lastname = null;
        country = null;

        picture = null;
        phonenumber = null;

        role = null;

    }

    @Override
    public String toString() {
        return "UserSession{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", country'" + country + '\'' +
                ", picture='" + picture + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
