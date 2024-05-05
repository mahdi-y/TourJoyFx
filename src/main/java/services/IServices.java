package Services;

import Entities.Guide;
import models.User;
import models.claims;

import java.sql.SQLException;
import java.util.List;

public interface IServices<T>{
    void registerUser(T t) throws SQLException;
    void updateProfile(T t, String currentEmail) throws SQLException;

    void updateProfileAfetrCompletion(User user, String currentEmail) throws SQLException;
    void add(T t) throws SQLException;

    boolean emailExists(String email) throws SQLException;

    boolean deleteUser(T t) throws SQLException;
    void update(T t) throws SQLException;

    void update(claims claims, Integer fkUser) throws SQLException;

    void update(Guide guide, int oldCIN) throws SQLException;

    void delete(T t) throws SQLException;
    List<T> Read(int fkUser) throws SQLException;

    boolean phoneNumberExists(int phone) throws SQLException;

    List<T> ReadUser() throws SQLException;



    List<User> fetchAllUsers() throws SQLException;
}