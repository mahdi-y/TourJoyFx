package Services;

import Entities.Transport;
import utils.MyDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransportService {
    public List<Transport> getAllTransports() throws SQLException {
        List<Transport> transports = new ArrayList<>();
        String query = "SELECT * FROM transport";

        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type_t = resultSet.getString("type_t");
                transports.add(new Transport(id, type_t));
            }
        }

        return transports;
    }
    public List<Transport> searchTransportsByType(String keyword) throws SQLException {
        List<Transport> searchResult = new ArrayList<>();
        String query = "SELECT * FROM transport WHERE type_t LIKE ?";
        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "%" + keyword + "%"); // Use % as wildcard for SQL LIKE clause
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type_t = resultSet.getString("type_t");
                searchResult.add(new Transport(id, type_t));
            }
        }
        return searchResult;
    }

    public Transport getTransportById(int id) throws SQLException {
        String query = "SELECT * FROM transport WHERE id = ?";

        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String type_t = resultSet.getString("type_t");
                return new Transport(id, type_t);
            }
        }

        return null;
    }

    public void addTransport(Transport transport) throws SQLException {
        String query = "INSERT INTO transport (type_t) VALUES (?)";

        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, transport.getType_t());
            statement.executeUpdate();
        }
    }

    public void updateTransport(Transport transport) throws SQLException {
        String query = "UPDATE transport SET type_t = ? WHERE id = ?";

        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, transport.getType_t());
            statement.setInt(2, transport.getId());
            statement.executeUpdate();
        }
    }

    public void deleteTransport(int id) throws SQLException {
        String query = "DELETE FROM transport WHERE id = ?";

        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
    // Method to check if a transport type already exists
    public boolean isTransportTypeExists(String type_t) throws SQLException {
        String query = "SELECT COUNT(*) FROM transport WHERE type_t = ?";

        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, type_t);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }

        return false;
    }
}