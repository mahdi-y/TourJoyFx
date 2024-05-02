package Services;

import Entities.Accomodation;
import utils.MyDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.sql.*;

public class ServiceAccomodation implements IServices<Accomodation> {

    private Connection con;
    private PreparedStatement pre;
    private ResultSet res;

    public ServiceAccomodation() {
        con = MyDB.getInstance().getConnection();
    }

    @Override
    public void add(Accomodation accomodation) throws SQLException {

        String query = "INSERT INTO Accomodation (name, type, nb_rooms ,location, price, image_name ) VALUES (?, ?, ?, ?, ?, ?)";
        pre = con.prepareStatement(query);
        pre.setString(1, accomodation.getName());
        pre.setString(2, accomodation.getType());
        pre.setInt(3, accomodation.getNb_rooms());
        pre.setString(4, accomodation.getLocation());
        pre.setFloat(5, accomodation.getPrice());
        pre.setString(6, accomodation.getImage_name());




        pre.executeUpdate();
    }
    public List<String> readNames() throws SQLException {
        List<String> accommodationNames = new ArrayList<>();
        String query = "SELECT name FROM Accomodation";
        try (PreparedStatement pre = con.prepareStatement(query);
             ResultSet res = pre.executeQuery()) {
            while (res.next()) {
                String name = res.getString("name");
                accommodationNames.add(name);
            }
        }
        return accommodationNames;
    }

    @Override
    public void update(Accomodation accomodation) throws SQLException {

        String query = "UPDATE Accomodation SET name=?, type=?, nb_rooms=?, price=?, location=? WHERE refA=?";
        pre = con.prepareStatement(query);
        pre.setString(1, accomodation.getName());
        pre.setString(2, accomodation.getType());
        pre.setInt(3, accomodation.getNb_rooms());
        pre.setFloat(4,accomodation.getPrice());
        pre.setString(5, accomodation.getLocation());
        pre.setInt(6, accomodation.getRefA());

        pre.executeUpdate();
    }

    @Override
    public void delete(Accomodation accomodation) throws SQLException {
        String query = "DELETE FROM Accomodation WHERE refA=?";
        pre = con.prepareStatement(query);
        pre.setInt(1, accomodation.getRefA());
        pre.executeUpdate();
    }
    public Map<String, Integer> getAccommodationNameIdMap() {
        Map<String, Integer> accommodationMap = new HashMap<>();
        String query = "SELECT refA, name FROM Accomodation";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                accommodationMap.put(rs.getString("name"), rs.getInt("refA"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // This will print the error log
        }
        return accommodationMap;
    }


    public List<Accomodation> Read() throws SQLException {
        List<Accomodation> accomodations = new ArrayList<>();
        String query = "SELECT * FROM Accomodation";
        pre = con.prepareStatement(query);
        res = pre.executeQuery();
        while (res.next()) {
            int refA=res.getInt("refA");

            String name = res.getString("name");
            String type = res.getString("type");
            int nb_rooms=res.getInt("nb_rooms");
            String location = res.getString("location");
            float price = res.getFloat("price");
            String image_name = res.getString("image_name");

            Accomodation accomodation = new Accomodation(refA,name, type, location, price, nb_rooms, image_name);
            accomodations.add(accomodation);
        }
        return accomodations;
    }
}
