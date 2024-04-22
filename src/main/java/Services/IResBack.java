package Services;

import java.sql.*;
import java.util.List;


public interface IResBack<T> {
    void delete(T t) throws SQLException;
    List<T> Read() throws SQLException;
}
