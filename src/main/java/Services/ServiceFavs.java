package Services;

import Entities.Accomodation;
import Entities.Favs;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFavs {


    private Connection con;
    private PreparedStatement pre;
    private ResultSet res;

    public ServiceFavs() {
        con = MyDB.getInstance().getConnection();
    }

    public void addFavorite(Favs fav) throws SQLException {
        String sql = "INSERT INTO favs (user, acc) VALUES (?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, fav.getUser());
            pstmt.setInt(2, fav.getAcc());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    private Connection connect() throws SQLException {
        // Returns a connection to your database
        return DriverManager.getConnection("jdbc:sqlite:path_to_your_db.db");
    }

    public List<Accomodation> getAllFavorites() throws SQLException {
        List<Accomodation> favorites = new ArrayList<>();
        String sql = "SELECT accomodation.* FROM accomodation JOIN favs ON accomodation.refA = favs.acc WHERE favs.user = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, 1); // Assuming user ID is 1
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int refA = rs.getInt("refA");
                String name = rs.getString("name");
                String type = rs.getString("type");
                String location = rs.getString("location");
                float price = rs.getFloat("price");
                int nb_rooms = rs.getInt("nb_rooms");
                String image_name = rs.getString("image_name");
                favorites.add(new Accomodation(refA, name, type, location, price, nb_rooms, image_name));
            }
        }
        return favorites;
    }

    public void deleteFavorite(int accRef, int userId) throws SQLException {
        String sql = "DELETE FROM favs WHERE acc = ? AND user = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, accRef);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        }
    }
    public void deleteAllFavorites() throws SQLException {
        String sql = "DELETE FROM favs";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.executeUpdate();
        }
    }

}
