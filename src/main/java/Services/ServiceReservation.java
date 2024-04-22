package Services;

import Entities.Accomodation;
import Entities.Reservation;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservation implements IReservation<Reservation>{
    private Connection con;
    private PreparedStatement pre;
    private ResultSet res;
    public ServiceReservation() {
        con = MyDB.getInstance().getConnection();
    }

    @Override
    public void add(Reservation reservation) throws SQLException {
        String query = "INSERT INTO Reservation (idR, start_date, end_date, name) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pre = con.prepareStatement(query)) {
            pre.setInt(1, reservation.getIdR());
            pre.setDate(2, reservation.getStart_date());
            pre.setDate(3, reservation.getEnd_date());
            pre.setInt(4, reservation.getName());
            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // This will print the error log
            throw e; // Rethrow the exception to handle it in the calling method
        }
    }



}

