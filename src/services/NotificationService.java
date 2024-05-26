package services;

import enums.NotificationTypeEnum;
import exceptions.IllegalOperationException;
import models.Notification;
import models.Subscription;
import models.audio.collections.Playlist;
import models.users.User;
import repositories.notifications.NotificationRepository;
import repositories.notifications.*;

import java.sql.SQLException;
import java.util.List;

public class NotificationService {
    private static NotificationService instance;
    private final NotificationRepository notificationRepository = new NotificationRepository();
    private final SubscriptionRepository subscriptionRepository = new SubscriptionRepository();

    NotificationService() {}

    public static synchronized NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    public void subscribeOrUnsubscribe(int userId, int entityId, String entityType) {
        // check if already subscribed
        boolean isSubscribed = subscriptionRepository.checkSubscription(userId, entityId, entityType);
        if (isSubscribed) {
            subscriptionRepository.removeSubscription(userId, entityId, entityType);
        } else {
            subscriptionRepository.addSubscription(userId, entityId, entityType);
        }
    }

    public void subscribeOrUnsubscribeFromArtist(int artistId) {
        User currentUser = UserService.getInstance().getCurrentUser();
        subscribeOrUnsubscribe(currentUser.getId(), artistId, "ARTIST");
    }

    public void subscribeOrUnsubscribeFromHost(int hostId) {
        User currentUser = UserService.getInstance().getCurrentUser();
        subscribeOrUnsubscribe(currentUser.getId(), hostId, "HOST");
    }

    public void subscribeOrUnsubscribeFromPlaylist(int playlistId) {
        User currentUser = UserService.getInstance().getCurrentUser();
        Playlist playlist = PlaylistService.getInstance().getPlaylist(playlistId);
        if (!playlist.isPublic() && playlist.getOwnerId() != currentUser.getId()) {
            throw new IllegalOperationException("Cannot subscribe to private or own playlist.");
        }
        subscribeOrUnsubscribe(currentUser.getId(), playlistId, "PLAYLIST");
    }

    public void notifySubscribers(int entityId, String entityType, NotificationTypeEnum notificationType, String message) {
        try {
            List<Subscription> subs = subscriptionRepository.findSubscribersByEntity(entityId, entityType);
            for (Subscription sub : subs) {
                notificationRepository.createNotification(sub.getSubscriptionId(), message, notificationType);
            }
        } catch (SQLException e) {
            System.out.println("Error notifying subscribers: " + e.getMessage());
        }
    }

    public void viewNotifications(int userId) {
        try {
            var notifications = notificationRepository.getNotificationsByUserId(userId);
            notifications.forEach(notification -> {
                if (!notification.isRead()) {
                    try {
                        notificationRepository.markNotificationAsRead(notification.getId());
                    } catch (SQLException e) {
                        System.out.println("SQLException: " + e.getMessage());
                    }
                    System.out.println(notification);
                }
            });
        } catch (Exception e) {
            System.out.println("Error viewing notifications: " + e.getMessage());
        }
    }

    public void clearNotifications(int userId) {
        try {
            notificationRepository.clearNotifications(userId);
        } catch (Exception e) {
            System.out.println("Error clearing notifications: " + e.getMessage());
        }
    }
}
