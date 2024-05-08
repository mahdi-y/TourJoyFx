package Services;

import java.sql.*;
import java.util.List;


public interface Services<T> {
    void add(T t) throws SQLException;
    void update(T t) throws SQLException;
    void delete(T t) throws SQLException;
    void update(T t, int oldCIN) throws SQLException;
    List<T> Read() throws SQLException;

}
