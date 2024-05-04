package Services;

import Entities.Country;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCountry implements IServices<Country> {

    private Connection con;
    private PreparedStatement pre;
    private ResultSet res;

    public ServiceCountry() {
        con = MyDB.getInstance().getConnection();
    }

    @Override
    public void add(Country country) throws SQLException {
        String query = "INSERT INTO Country (name, region, visa_required) VALUES (?, ?, ?)";
        pre = con.prepareStatement(query);
        pre.setString(1, country.getName());
        pre.setString(2, country.getRegion());
        pre.setBoolean(3, country.isVisa_required());
        pre.executeUpdate();
    }

    @Override
    public void update(Country country) throws SQLException {
        String query = "UPDATE Country SET name=?, region=?, visa_required=? WHERE name=?";
        pre = con.prepareStatement(query);
        pre.setString(1, country.getName());
        pre.setString(2, country.getRegion());
        pre.setBoolean(3, country.isVisa_required());
        pre.setString(4, country.getName());
        pre.executeUpdate();
    }

    @Override
    public void delete(Country country) throws SQLException {
        String deleteMonumentsQuery = "DELETE FROM Monument WHERE fkcountry_id=?";
        String deleteCountryQuery = "DELETE FROM Country WHERE name=?";

        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement deleteMonumentsPst = connection.prepareStatement(deleteMonumentsQuery);
             PreparedStatement deleteCountryPst = connection.prepareStatement(deleteCountryQuery)) {

            // Delete monuments associated with the country
            deleteMonumentsPst.setString(1, country.getName());
            deleteMonumentsPst.executeUpdate();

            // Delete the country
            deleteCountryPst.setString(1, country.getName());
            deleteCountryPst.executeUpdate();
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }

    public List<Country> Read() throws SQLException {
        List<Country> countries = new ArrayList<>();
        String query = "SELECT * FROM Country";
        pre = con.prepareStatement(query);
        res = pre.executeQuery();
        while (res.next()) {
            String name = res.getString("name");
            String region = res.getString("region");
            boolean visa_required = res.getBoolean("visa_required");
            Country country = new Country(name, region, visa_required);
            countries.add(country);
        }
        return countries;
    }

    @Override
    public void update(Country country, int oldCIN) throws SQLException {

    }

    public Country getCountryByName(String countryName) {
        String query = "SELECT id, name, region, visa_required FROM country WHERE name = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, countryName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Country country = new Country();
                    country.setId(rs.getInt("id"));
                    country.setName(rs.getString("name"));
                    country.setRegion(rs.getString("region"));
                    country.setVisa_required(rs.getBoolean("visa_required"));
                    return country;
                }
            }
        } catch (SQLException e) {
            // Handle exception (logging, rethrowing, etc.)
        }
        return null; // or consider throwing an exception
    }

    public String getCountryNameById(int countryId) {
        String countryName = null;
        String query = "SELECT name FROM Country WHERE id = ?";
        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, countryId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    countryName = rs.getString("name");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting country name by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return countryName;
    }

    public List<String> getAllCountryNames() {
        List<String> countryNames = new ArrayList<>();
        String query = "SELECT name FROM Country"; // Corrected table name

        try {
            // Use the existing connection from the constructor
            pre = con.prepareStatement(query);
            res = pre.executeQuery();

            while (res.next()) {
                String countryName = res.getString("name");
                countryNames.add(countryName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions
        }

        return countryNames;
    }

    public int getCountryIdByName(String countryName) throws SQLException {
        String query = "SELECT id FROM Country WHERE name = ?";
        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, countryName);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        // Return -1 if the country name is not found
        return -1;
    }
}