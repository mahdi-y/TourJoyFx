package Services;

import java.sql.*;
import java.util.List;


public interface IReservation<T> {
    void add(T t) throws SQLException;

}
