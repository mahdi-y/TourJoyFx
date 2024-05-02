package Services;

import Entities.Monument;
import Entities.Country;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceMonument implements IServices<Monument> {

    private Connection con;
    private PreparedStatement pre;
    private ResultSet res;

    public ServiceMonument() {
        con = MyDB.getInstance().getConnection();
    }

    @Override
    public void add(Monument monument) throws SQLException {
        String query = "INSERT INTO Monument (fkcountry_id, name_m, type, entryprice, status, description, latitude, longitude, image_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        pre = con.prepareStatement(query);
        pre.setObject(1, monument.getCountry() != null ? monument.getCountry().getId() : null);
        pre.setString(2, monument.getName());
        pre.setString(3, monument.getType());
        pre.setInt(4, monument.getEntryPrice());
        pre.setString(5, monument.getStatus());
        pre.setString(6, monument.getDescription());
        pre.setDouble(7, monument.getLatitude());  // Changed to setDouble
        pre.setDouble(8, monument.getLongitude());  // Changed to setDouble
        pre.setString(9, monument.getImagePath());
        pre.executeUpdate();
    }

    @Override
    public void delete(Monument monument) throws SQLException {
        String query = "DELETE FROM Monument WHERE ref=?";
        pre = con.prepareStatement(query);
        pre.setInt(1, monument.getId());
        pre.executeUpdate();
    }

    @Override
    public void update(Monument monument) throws SQLException {
        String query = "UPDATE Monument SET fkcountry_id=?, name_m=?, type=?, entryprice=?, status=?, description=?, latitude=?, longitude=?, image_name=? WHERE ref=?";
        pre = con.prepareStatement(query);
        pre.setInt(1, monument.getCountry().getId());
        pre.setString(2, monument.getName());
        pre.setString(3, monument.getType());
        pre.setInt(4, monument.getEntryPrice());
        pre.setString(5, monument.getStatus());
        pre.setString(6, monument.getDescription());
        pre.setDouble(7, monument.getLatitude());  // Changed to setDouble
        pre.setDouble(8, monument.getLongitude());  // Changed to setDouble
        pre.setString(9, monument.getImagePath());
        pre.setInt(10, monument.getId());
        pre.executeUpdate();
    }

    public List<Monument> Read() throws SQLException {
        List<Monument> monuments = new ArrayList<>();
        String query = "SELECT m.*, c.name AS country_name FROM Monument m JOIN Country c ON m.fkcountry_id = c.id";
        pre = con.prepareStatement(query);
        res = pre.executeQuery();
        while (res.next()) {
            int id = res.getInt("ref");
            int fkCountryId = res.getInt("fkcountry_id");
            String name = res.getString("name_m");
            String type = res.getString("type");
            int entryPrice = res.getInt("entryprice");
            String status = res.getString("status");
            String description = res.getString("description");
            double latitude = res.getDouble("latitude");  // Changed to getDouble
            double longitude = res.getDouble("longitude");  // Changed to getDouble
            String countryName = res.getString("country_name");
            String imagePath = res.getString("image_name");

            Country country = new Country();
            country.setId(fkCountryId);
            country.setName(countryName);

            Monument monument = new Monument(id, country, name, type, entryPrice, status, description, latitude, longitude, imagePath);
            monuments.add(monument);
        }
        return monuments;
    }
    public Map<String, Integer> getMonumentCountByCountry() throws SQLException {
        Map<String, Integer> monumentCount = new HashMap<>();
        String query = "SELECT c.name, COUNT(m.ref) as monument_count FROM Country c LEFT JOIN Monument m ON c.id = m.fkcountry_id GROUP BY c.name";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                monumentCount.put(rs.getString("name"), rs.getInt("monument_count"));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            throw e;  // Throw the exception to be handled/displayed in the controller
        }
        return monumentCount;
    }

}