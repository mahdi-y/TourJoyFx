package services;

import models.categories;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategories implements IServices<categories> {

    private Connection con;
    private PreparedStatement pre;
    private ResultSet res;


    public ServiceCategories() {
        con = DBConnection.getInstance().getCnx();
    }

    @Override
    public void add(categories categories) throws SQLException {
        String query = "INSERT INTO categories (name) VALUES (?)";
        // Use the class-level connection 'con' which is already open
        try (PreparedStatement pre = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pre.setString(1, categories.getName());
            pre.executeUpdate();

            // Retrieve the generated keys
            try (ResultSet generatedKeys = pre.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Set the id of the category from the generated key
                    categories.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }




    public List<categories> Read() throws SQLException {
        List<categories>categoriesList = new ArrayList<>();
        String query = "SELECT * FROM categories";
        try (PreparedStatement pre = con.prepareStatement(query);
             ResultSet res = pre.executeQuery()) {
            while (res.next()) {
                int id = res.getInt("id");
                String name = res.getString("name");

                categories category = new categories(id, name);
                categoriesList.add(category);
            }
        } catch (SQLException e) {
            throw new SQLException("Error reading claims from database", e);
        }
        return categoriesList;
    }
    @Override
    public void update(categories categories)
            throws SQLException {
        String query = "UPDATE categories SET name=? WHERE id=?";
        pre = con.prepareStatement(query);
        pre.setString(1, categories.getName());

        pre.setInt(2, categories.getId()); // Ensure this is the last parameter according to the query
        pre.executeUpdate();
    }
    public boolean isNameUnique(String name, Integer id) throws SQLException {
        String query = "SELECT COUNT(*) FROM categories WHERE name = ? AND id != ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, name);
            pst.setInt(2, (id == null) ? -1 : id);  // Use -1 if id is null to ensure no match
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;  // Returns true if no duplicates found
            }
        }
        return false;  // Default to false to handle unexpected cases safely
    }

    @Override
    public void delete(categories categories) throws SQLException {
        String deleteCategoryQuery = "DELETE FROM categories WHERE id=?";
        PreparedStatement deleteCategoryPst = null;
        try {
            // Assuming 'con' is a class member that holds an open connection
            deleteCategoryPst = con.prepareStatement(deleteCategoryQuery);
            deleteCategoryPst.setInt(1, categories.getId());
            int affectedRows = deleteCategoryPst.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting category failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting category from the database", e);
        } finally {
            if (deleteCategoryPst != null) {
                deleteCategoryPst.close(); // Close only the PreparedStatement
            }
            // Do not close the connection here if it's being reused
        }
    }





}


