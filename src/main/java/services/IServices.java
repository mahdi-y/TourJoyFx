package services;

import models.User;

import java.sql.SQLException;
import java.util.List;

public interface IServices<T>{
    void registerUser(T t) throws SQLException;
    void updateProfile(T t, String currentEmail) throws SQLException;

    void updateProfileAfetrCompletion(User user, String currentEmail) throws SQLException;

    boolean emailExists(String email) throws SQLException;

    boolean deleteUser(T t) throws SQLException;

    boolean phoneNumberExists(int phone) throws SQLException;

    List<T> ReadUser() throws SQLException;



    List<User> fetchAllUsers() throws SQLException;
}