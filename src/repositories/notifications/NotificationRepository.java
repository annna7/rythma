package repositories.notifications;

import database.DatabaseConnector;
import models.Notification;
import enums.NotificationTypeEnum;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationRepository {
    private static final String INSERT_NOTIFICATION = "INSERT INTO Notifications (SubscriptionID, Message, NotificationType) VALUES (?, ?, ?)";
    private static final String GET_NOTIFICATIONS_BY_USER_ID = "SELECT n.* FROM Notifications n JOIN Subscriptions s ON n.SubscriptionID = s.SubscriptionID WHERE s.UserID = ? ORDER BY n.IsRead, n.Timestamp DESC";
    private static final String UPDATE_NOTIFICATION_READ = "UPDATE Notifications SET IsRead = TRUE WHERE NotificationID = ?";
    private static final String CLEAR_NOTIFICATIONS = "DELETE n FROM Notifications n JOIN Subscriptions s ON n.SubscriptionID = s.SubscriptionID WHERE s.UserID = ?";

    public void createNotification(int subscriptionId, String message, NotificationTypeEnum notificationType) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(INSERT_NOTIFICATION)) {
            stmt.setInt(1, subscriptionId);
            stmt.setString(2, message);
            stmt.setString(3, notificationType.toString());
            stmt.executeUpdate();
        }
    }

    public List<Notification> getNotificationsByUserId(int userId) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_NOTIFICATIONS_BY_USER_ID)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Notification notification = new Notification(
                        rs.getInt("SubscriptionID"),
                        NotificationTypeEnum.valueOf(rs.getString("NotificationType")),
                        rs.getString("Message")
                );
                notification.setRead(rs.getBoolean("IsRead"));
                notifications.add(notification);
            }
        }
        return notifications;
    }

    public void markNotificationAsRead(int notificationId) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_NOTIFICATION_READ)) {
            stmt.setInt(1, notificationId);
            stmt.executeUpdate();
        }
    }

    public void clearNotifications(int userId) {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(CLEAR_NOTIFICATIONS)) {
            stmt.setInt(1, userId);
            int affectedRows = stmt.executeUpdate();
            System.out.println("Cleared " + affectedRows + " notifications for user ID: " + userId);
        } catch (SQLException e) {
            System.out.println("SQL error in clearReadNotifications: " + e.getMessage());
        }
    }
}
