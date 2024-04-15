package services;

import java.sql.SQLException;
import java.util.List;

public interface IServices<T>{
    void registerUser(T t) throws SQLException;
    boolean updateUser(T t) throws SQLException;
    boolean deleteUser(T t) throws SQLException;
    List<T> ReadUser() throws SQLException;

}