package Services;

import Entities.Accomodation;
import Entities.Guide;
import Entities.Images;
import models.User;
import models.claims;
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

    public void registerUser(Accomodation accomodation) throws SQLException {

    }

    public void updateProfile(Accomodation accomodation, String currentEmail) throws SQLException {

    }

    public void updateProfileAfetrCompletion(User user, String currentEmail) throws SQLException {

    }

    public void add(Accomodation accomodation) throws SQLException {
        String query = "INSERT INTO Accomodation (name, type, nb_rooms, location, price, image_name) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pre = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pre.setString(1, accomodation.getName());
            pre.setString(2, accomodation.getType());
            pre.setInt(3, accomodation.getNb_rooms());
            pre.setString(4, accomodation.getLocation());
            pre.setFloat(5, accomodation.getPrice());
            pre.setString(6, accomodation.getImage_name());
            pre.executeUpdate();

            try (ResultSet generatedKeys = pre.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    accomodation.setRefA(generatedKeys.getInt(1));  // Assuming `refA` is the first column
                } else {
                    throw new SQLException("Creating accommodation failed, no ID obtained.");
                }
            }
        }
    }

    public boolean emailExists(String email) throws SQLException {
        return false;
    }


    public boolean deleteUser(Accomodation accomodation) throws SQLException {
        return false;
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

        String query = "UPDATE Accomodation SET name=?, type=?, nb_rooms=?, price=?, location=?, WHERE refA=?";
        pre = con.prepareStatement(query);
        pre.setString(1, accomodation.getName());
        pre.setString(2, accomodation.getType());
        pre.setInt(3, accomodation.getNb_rooms());
        pre.setFloat(4,accomodation.getPrice());
        pre.setString(5, accomodation.getLocation());
        pre.setInt(6, accomodation.getRefA());


        pre.executeUpdate();
    }

    public void update(claims claims, Integer fkUser) throws SQLException {

    }

    public void update(Guide guide, int oldCIN) throws SQLException {

    }

    @Override
    public void delete(Accomodation accomodation) throws SQLException {
        String query = "DELETE FROM Accomodation WHERE refA=?";
        PreparedStatement pre = null;
        try {
            pre = con.prepareStatement(query);
            pre.setInt(1, accomodation.getRefA());
            int affectedRows = pre.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting accommodation failed, no rows affected.");
            }
        } catch (SQLException e) {
            System.err.println("SQL error on delete operation: " + e.getMessage());
            throw e; // Rethrow the exception to handle it further up in the call stack
        } finally {
            if (pre != null) {
                try {
                    pre.close();
                } catch (SQLException ex) {
                    System.err.println("Error closing PreparedStatement: " + ex.getMessage());
                }
            }
        }
    }

    @Override
    public void update(Accomodation accomodation, int oldCIN) throws SQLException {

    }

    public List<Accomodation> Read(int fkUser) throws SQLException {
        return null;
    }


    public boolean phoneNumberExists(int phone) throws SQLException {
        return false;
    }

    public List<Accomodation> ReadUser() throws SQLException {
        return null;
    }

    public List<User> fetchAllUsers() throws SQLException {
        return null;
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

    public void addImage(Images image) throws SQLException {
        String query = "INSERT INTO Images (accommodation, name) VALUES (?, ?)";
        try (PreparedStatement pre = con.prepareStatement(query)) {
            pre.setInt(1, image.getAccommodation());
            pre.setString(2, image.getName());
            int result = pre.executeUpdate();
            System.out.println("Insert result: " + result);
        } catch (SQLException e) {
            System.err.println("Error adding image to database: " + e.getMessage());
            throw e;  // Propagate the error
        }
    }




    public List<Images> getImagesByAccommodationId(int accommodationId) throws SQLException {
        List<Images> images = new ArrayList<>();
        String query = "SELECT * FROM Images WHERE accommodation = ?";
        try (PreparedStatement pre = con.prepareStatement(query)) {
            pre.setInt(1, accommodationId);
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                images.add(new Images(res.getInt("id"), res.getInt("accommodation"), res.getString("name")));
            }
        }
        return images;
    }


}
