package services;

import models.claims;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceClaims implements IServices<claims> {

    private Connection con;
    private PreparedStatement pre;
    private ResultSet res;


    public ServiceClaims() {
        con = DBConnection.getInstance().getCnx();
    }



    @Override
    public void add(claims claims) throws SQLException {
        String query = "INSERT INTO claims ( title, description, createDate, state, reply) VALUES (?, ?, ?, ?, ?)";
        pre = con.prepareStatement(query);

        pre.setString(1, claims.getTitle());
        pre.setString(2, claims.getDescription());
        pre.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
        pre.setString(4, claims.getState());
        pre.setString(5, claims.getReply());
        pre.executeUpdate();

    }
    public List<claims> Read() throws SQLException {
        List<claims> claimsList = new ArrayList<>();
        String query = "SELECT * FROM claims";
        try (PreparedStatement pre = con.prepareStatement(query);
             ResultSet res = pre.executeQuery()) {
            while (res.next()) {
                int id = res.getInt("id");
                String title = res.getString("title");
                String description = res.getString("description");
                LocalDateTime createDate = res.getTimestamp("createDate").toLocalDateTime();
                String state = res.getString("state");
                String reply = res.getString("reply");
                claims claim = new claims(id, title, description, createDate, state, reply);
                claimsList.add(claim);
            }
        } catch (SQLException e) {
            throw new SQLException("Error reading claims from database", e);
        }
        return claimsList;
    }
    @Override
    public void update(claims claims)
            throws SQLException {
        String query = "UPDATE claims SET title=?, description=?, state=?, reply=? WHERE id=?";
        pre = con.prepareStatement(query);
        pre.setString(1, claims.getTitle());
        pre.setString(2, claims.getDescription());

        pre.setString(3, claims.getState());
        pre.setString(4, claims.getReply());
        pre.setInt(5, claims.getId()); // Ensure this is the last parameter according to the query
        pre.executeUpdate();
    }

    @Override
    public void delete(claims claims) throws SQLException {
        String deleteClaimsQuery = "DELETE FROM claims WHERE id=?";
        PreparedStatement deleteClaimsPst = null;
        try {
            deleteClaimsPst = con.prepareStatement(deleteClaimsQuery);
            deleteClaimsPst.setInt(1, claims.getId());
            int affectedRows = deleteClaimsPst.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting claim failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting claim from the database", e);
        } finally {
            if (deleteClaimsPst != null) {
                deleteClaimsPst.close(); // Close only the PreparedStatement
            }
            // Do not close the connection here if it's being reused
        }
    }





}


