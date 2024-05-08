package Services;

import Entities.Accomodation;
import utils.MyDB;
import Entities.Reservation;
import utils.UserSession;

import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class ServiceResBack implements IResBack<Reservation>{

private Connection con;
private PreparedStatement pre;
private ResultSet res;

    public ServiceResBack() {
        con = MyDB.getInstance().getConnection();
    }



@Override
public void delete(Reservation reservation) throws SQLException {
        String query = "DELETE FROM Reservation WHERE idR=?";
        pre = con.prepareStatement(query);
        pre.setInt(1, reservation.getIdR());
        pre.executeUpdate();
        }

        @Override
        public List<Reservation> Read() throws SQLException {
                return null;
        }


        public List<Reservation> Read(int userId) throws SQLException {
                List<Reservation> reservations = new ArrayList<>();
                String query = "SELECT Reservation.idR, Reservation.start_date, Reservation.end_date, Reservation.name AS refA, Accomodation.name AS accommodationName " +
                        "FROM Reservation " +
                        "JOIN Accomodation ON Reservation.name = Accomodation.refA " +
                        "WHERE Reservation.fkuser_id = ?";  // Assuming 'user_id' is the field in 'Reservation' that links to the user
                PreparedStatement pre = con.prepareStatement(query);
                pre.setInt(1, userId);  // Set the user ID parameter
                ResultSet res = pre.executeQuery();
                while (res.next()) {
                        int idR = res.getInt("idR");
                        Date start_date = res.getDate("start_date");
                        Date end_date = res.getDate("end_date");
                        int refA = res.getInt("refA");
                        String accommodationName = res.getString("accommodationName");

                        Reservation reservation = new Reservation(idR, start_date, end_date, refA);
                        reservation.setAccommodationName(accommodationName);
                        reservations.add(reservation);
                }
                return reservations;
    }
    }



