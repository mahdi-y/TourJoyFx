package services;

import java.sql.SQLException;
import java.util.List;
public interface IServices <T> {

    void add(T t);
    void delete(T t) throws SQLException;
    void update(T t, int oldCIN) throws SQLException;
    List<T> Read() throws SQLException;

}
