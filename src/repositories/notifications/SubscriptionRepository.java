package repositories.notifications;

import database.DatabaseConnector;
import models.Subscription;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionRepository {

    private static final String CHECK_SUBSCRIPTION = "SELECT COUNT(*) FROM Subscriptions WHERE UserID = ? AND EntityID = ? AND EntityType = ?";
    private static final String ADD_SUBSCRIPTION = "INSERT INTO Subscriptions (UserID, EntityID, EntityType) VALUES (?, ?, ?)";
    private static final String REMOVE_SUBSCRIPTION = "DELETE FROM Subscriptions WHERE UserID = ? AND EntityID = ? AND EntityType = ?";
    private static final String FIND_SUBSCRIPTIONS_BY_ENTITY = "SELECT SubscriptionID, UserID FROM Subscriptions WHERE EntityID = ? AND EntityType = ?";

    public List<Subscription> findSubscribersByEntity(int entityId, String entityType) throws SQLException {
        List<Subscription> subscriptions = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(FIND_SUBSCRIPTIONS_BY_ENTITY)) {
            stmt.setInt(1, entityId);
            stmt.setString(2, entityType);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    subscriptions.add(new Subscription(rs.getInt("SubscriptionID"), rs.getInt("UserID"), entityId, entityType));
                }
            }
        }
        return subscriptions;
    }

    public boolean checkSubscription(int userId, int entityId, String entityType) {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(CHECK_SUBSCRIPTION)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, entityId);
            stmt.setString(3, entityType);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("SQL error in checkSubscription: " + e.getMessage());
        }
        return false;
    }

    public void addSubscription(int userId, int entityId, String entityType) {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(ADD_SUBSCRIPTION)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, entityId);
            stmt.setString(3, entityType);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL error in addSubscription: " + e.getMessage());
        }
    }

    public void removeSubscription(int userId, int entityId, String entityType) {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(REMOVE_SUBSCRIPTION)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, entityId);
            stmt.setString(3, entityType);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL error in removeSubscription: " + e.getMessage());
        }
    }
}
