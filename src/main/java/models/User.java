package models;
//import javax.persistence;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class User {
    private String[] roles;

    private int id;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private Integer phoneNumber;

    private String country;

    private String profilePicture;

    private LocalDateTime createdAt;

    private String googleAuthenticatorSecret;

    private LocalDateTime modifiedAt;

    private boolean isVerified;

    public User(String email, String[] roles, String password, Integer phoneNumber, LocalDateTime createdAt) {
        this.email = email;
        setRoles(roles);
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
    }

    public User(String email, String[] roles, String password, String firstName, String lastName, Integer phoneNumber, LocalDateTime createdAt) {
        this.email = email;
        setRoles(roles);
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
    }



    private boolean isBanned;

    private String googleId;

    public User(String email, String[] roles, String password, LocalDateTime createdAt) {
        this.email = email;
        setRoles(roles);
        this.password = password;
        this.createdAt = createdAt;
    }

    public User(int id, String email, String[] roles, String password, String firstName, String lastName, Integer phoneNumber, String country, String profilePicture, String googleAuthenticatorSecret, boolean isVerified, boolean isBanned, String googleId) {
        this.id = id;
        this.email = email;
        setRoles(roles);
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.profilePicture = profilePicture;
        this.googleAuthenticatorSecret = googleAuthenticatorSecret;
        this.isVerified = isVerified;
        this.isBanned = isBanned;
        this.googleId = googleId;
    }

    public User(int id, String email, String firstName, String lastName, String country, String profilePicture, int phoneNumber) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.profilePicture = profilePicture;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(int id, String email, String[] roles, String password, String firstName, String lastName, Integer phoneNumber, String country, String profilePicture, LocalDateTime modifiedAt) {
        this.id = id;
        this.email = email;
        setRoles(roles);
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.profilePicture = profilePicture;
        this.modifiedAt = modifiedAt;
    }

    public User(int id, String email, String[] roles, String password, String firstName, String lastName, String country, String profilePicture, int phoneNumber) {
        this.id = id;
        this.email = email;
        setRoles(roles);
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.profilePicture = profilePicture;
        this.phoneNumber = phoneNumber;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public Set<String> getRoles() {
//        return roles;
//    }


    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getGoogleAuthenticatorSecret() {
        return googleAuthenticatorSecret;
    }

    public void setGoogleAuthenticatorSecret(String googleAuthenticatorSecret) {
        this.googleAuthenticatorSecret = googleAuthenticatorSecret;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public User() {

    }

    public User(int id, String email, String[] roles, String password, String firstName, String lastName, Integer phoneNumber, String country, String profilePicture, LocalDateTime createdAt, String googleAuthenticatorSecret, LocalDateTime modifiedAt, boolean isVerified, boolean isBanned, String googleId) {
        this.id = id;
        this.email = email;
        setRoles(roles);
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.profilePicture = profilePicture;
        this.createdAt = createdAt;
        this.googleAuthenticatorSecret = googleAuthenticatorSecret;
        this.modifiedAt = modifiedAt;
        this.isVerified = isVerified;
        this.isBanned = isBanned;
        this.googleId = googleId;
    }

    public User(int id, String email, String[] roles, String firstName, String lastName, Integer phoneNumber, String country, LocalDateTime createdAt, boolean isVerified, boolean isBanned) {
        this.id = id;
        this.email = email;
        setRoles(roles);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.createdAt = createdAt;
        this.isVerified = isVerified;
        this.isBanned = isBanned;
    }

    public User(String email, String password, String firstName, String lastName, Integer phoneNumber, LocalDateTime createdAt) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
    }

    public User(int id, String email, String firstName, String lastName, Integer phoneNumber, String country, LocalDateTime createdAt, boolean isVerified, boolean isBanned) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.createdAt = createdAt;
        this.isVerified = isVerified;
        this.isBanned = isBanned;
    }

//    User updatedUser = new User(user.getId(), existingUser.getLastName(), existingUser.getFirstName(), existingUser.getEmail(), existingUser.getPhoneNumber(), newPassword, existingUser.getProfilePicture());

    public User(int id, String lastName, String firstName, String email, int phoneNumber, String password, String profilePicture) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
    }

    public User(int id, String email, String password, String firstName, String lastName, String profilePicture) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
    }




    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", country='" + country + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", createdAt=" + createdAt +
                ", googleAuthenticatorSecret='" + googleAuthenticatorSecret + '\'' +
                ", modifiedAt=" + modifiedAt +
                ", isVerified=" + isVerified +
                ", isBanned=" + isBanned +
                ", googleId='" + googleId + '\'' +
                '}';
    }
}
