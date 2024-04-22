package Services;

import Entities.Accomodation;
import utils.MyDB;
import Entities.Reservation;
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


public List<Reservation> Read() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM Reservation";
        pre = con.prepareStatement(query);
        res = pre.executeQuery();
        while (res.next()) {
        int idR = res.getInt("idR");
        Date start_date = res.getDate("start_date");
        Date end_date = res.getDate("end_date");
                int name = res.getInt("name");


        Reservation reservation = new Reservation(idR, start_date, end_date, name);
        reservations.add(reservation);


        }
        return reservations;
        }


}

