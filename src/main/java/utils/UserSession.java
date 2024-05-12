package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.User;

import javax.mail.Authenticator;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public final class UserSession {

    private static UserSession instance;

    private int id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String[] rolesPHP;

    private String country;
    private String picture;
    private String phonenumber;

    private String[] roles;
    private String rolesJson;

    public String[] getRoles() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Deserialize the JSON string back into an array of strings
            return mapper.readValue(rolesJson, String[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0]; // Return empty array on error
        }
    }




    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }
    public String getPicture() { return picture; }

    public String getCountry() {return country;}
    public String getPhonenumber() { return phonenumber; }

private static final ObjectMapper objectMapper = new ObjectMapper();


   /* public String getRoles() {
        return this.rolesJson;
    }

    public void setRoles(String rolesJson) {
        this.rolesJson = rolesJson;
    }*/

    private UserSession() { }

    public UserSession(int id, String email, String password, String firstname, String lastname, String country, String picture, String phonenumber, String[] roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.country = country;
        this.picture = picture;
        this.phonenumber = phonenumber;
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.rolesJson = mapper.writeValueAsString(roles); // Convert and store roles as JSON
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            this.rolesJson = "[]";  // In case of error, initialize with an empty array
        }
    }

//    public UserSession(int id, String email, String password, String firstname, String lastname, String country, String picture, String phonenumber, String[] role, String[] rolesPHP) {
//        this.id = id;
//        this.email = email;
//        this.password = password;
//        this.firstname = firstname;
//        this.lastname = lastname;
//        this.country = country;
//        this.picture = picture;
//        this.phonenumber = phonenumber;
//        this.roles = role;
//        this.rolesPHP = rolesPHP;
//    }


        public void setUser(User user) {

            ObjectMapper mapper = new ObjectMapper();
            this.id = user.getId();
            this.email = user.getEmail();
            this.firstname = user.getFirstName();
            this.lastname = user.getLastName();
            this.country = user.getCountry();
            this.picture = user.getProfilePicture();
            this.phonenumber = user.getPhoneNumber().toString();
            this.roles = user.getRoles();


            System.out.println("Roles from the getter in user session: " + Arrays.toString(user.getRoles()));
            System.out.println("Roles in UserSession: " + Arrays.toString(this.roles));

        }

    public static UserSession getInstance(int id, String email, String password, String firstname, String lastname
            , String country,String picture, String phonenumber, String[] roles, String[] rolesPHP) {
        if(instance == null) {
            instance = new UserSession(id, email, password, firstname, lastname, country, picture, phonenumber, roles);
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
        roles = null;
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
                ", role='" + roles + '\'' +
                '}';
    }
}
