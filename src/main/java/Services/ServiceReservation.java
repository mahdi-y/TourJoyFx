package Services;

import Entities.Accomodation;
import Entities.Reservation;
import utils.MyDB;

import java.sql.*;
import java.time.LocalDate;
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
        String query = "INSERT INTO Reservation (start_date, end_date, name) VALUES ( ?, ?, ?)";
        try (PreparedStatement pre = con.prepareStatement(query)) {
            pre.setDate(1, reservation.getStart_date());
            pre.setDate(2, reservation.getEnd_date());
            pre.setInt(3, reservation.getName());
            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // This will print the error log
            throw e; // Rethrow the exception to handle it in the calling method
        }
    }

    public List<LocalDate> getBookedDates(int accommodationId) throws SQLException {
        List<LocalDate> bookedDates = new ArrayList<>();
        String query = "SELECT start_date, end_date FROM Reservation WHERE name = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, accommodationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate startDate = rs.getDate("start_date").toLocalDate();
                    LocalDate endDate = rs.getDate("end_date").toLocalDate();
                    while (!startDate.isAfter(endDate)) {
                        bookedDates.add(startDate);
                        startDate = startDate.plusDays(1);
                    }
                }
            }
        }
        return bookedDates;
    }




}

