package Services;
import Entities.Booking;

import java.sql.SQLException;
import java.util.List;
public interface CRUDBooking {
    void addBooking(Booking booking) throws SQLException;

}
