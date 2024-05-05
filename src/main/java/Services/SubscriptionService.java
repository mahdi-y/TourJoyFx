package Services;

import Entities.Subscription;
import Entities.Transport;
import utils.MyDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscriptionService {
    private Map<String, Integer> typeToIdMap;
    public List<Subscription> getAllSubscriptions() throws SQLException {
        List<Subscription> subscriptions = new ArrayList<>();
        String query = "SELECT * FROM subscription";

        // Load all data into memory first
        List<Integer> ids = new ArrayList<>();
        List<String> plans = new ArrayList<>();
        List<Integer> durations = new ArrayList<>();
        List<Integer> typeTIds = new ArrayList<>();
        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
                plans.add(resultSet.getString("plan"));
                durations.add(resultSet.getInt("duration"));
                typeTIds.add(resultSet.getInt("type_t_id"));
            }
        }

        // Create Transport objects
        Map<Integer, Transport> transportMap = new HashMap<>();
        TransportService transportService = new TransportService();
        for (int typeTId : typeTIds) {
            transportMap.put(typeTId, transportService.getTransportById(typeTId));
        }

        // Create Subscription objects using the loaded data and Transport objects
        for (int i = 0; i < ids.size(); i++) {
            int id = ids.get(i);
            String plan = plans.get(i);
            int duration = durations.get(i);
            int typeTId = typeTIds.get(i);
            Transport typeT = transportMap.get(typeTId);
            subscriptions.add(new Subscription(id, plan, duration, typeT));
        }

        return subscriptions;
    }

    public List<Subscription> searchSubscriptionsByPlan(String plan) throws SQLException {
        List<Subscription> subscriptions = new ArrayList<>();
        String query = "SELECT * FROM subscription WHERE plan = ?";
        ResultSet resultSet = null;

        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, plan);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                int duration = resultSet.getInt("duration");
                int type_t_id = resultSet.getInt("type_t_id");
                TransportService transportService = new TransportService();
                Transport transport = transportService.getTransportById(type_t_id);
                Subscription subscription = new Subscription(id, plan, duration, transport);
                subscriptions.add(subscription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }

        return subscriptions;
    }





    public Subscription getSubscriptionById(int id) throws SQLException {
        String query = "SELECT * FROM subscription WHERE id = ?";

        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String plan = resultSet.getString("plan");
                int duration = resultSet.getInt("duration");
                int typeTId = resultSet.getInt("type_t_id");
                Transport typeT = new TransportService().getTransportById(typeTId);
                return new Subscription(id, plan, duration, typeT);
            }
        }

        return null;
    }

    public void addSubscription(Subscription subscription) throws SQLException {
        String query = "INSERT INTO subscription (plan, duration, type_t_id) VALUES (?, ?, ?)";

        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, subscription.getPlan());
            statement.setInt(2, subscription.getDuration());
            statement.setInt(3, subscription.getTypeT().getId());
            statement.executeUpdate();
        }
    }

    public void updateSubscription(Subscription subscription) throws SQLException {
        String query = "UPDATE subscription SET plan = ?, duration = ?, type_t_id = ? WHERE id = ?";

        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, subscription.getPlan());
            statement.setInt(2, subscription.getDuration());
            statement.setInt(3, subscription.getTypeT().getId());
            statement.setInt(4, subscription.getId().intValue());
            statement.executeUpdate();
        }
    }

    public void deleteSubscription(Long id) throws SQLException {
        String query = "DELETE FROM subscription WHERE id = ?";
        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }
    public boolean checkIfSubscriptionExists(String plan, String type) throws SQLException {
        String query = "SELECT * FROM subscription WHERE plan =? AND type_t_id = (SELECT id FROM transport WHERE type_t =?)";

        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, plan);
            statement.setString(2, type);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        }
    }


}