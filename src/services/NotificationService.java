package services;

import models.users.Artist;
import models.users.Host;
import models.users.User;
import observable.Observable;

public class NotificationService {
    private static NotificationService instance;
    private NotificationService() {}

    public static synchronized NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    public void subscribeOrUnsubscribeFromArtist(int artistId) {
        User currentUser = UserService.getInstance().getCurrentUser();
        Artist artist = UserService.getInstance().getArtist(artistId);
        if (currentUser.getSubscriptions().contains(artist)) {
            artist.detach(currentUser);
        } else {
            artist.attach(currentUser);
        }
    }

    public void subscribeOrUnsubscribeFromHost(int hostId) {
        User currentUser = UserService.getInstance().getCurrentUser();
        Host host = UserService.getInstance().getHost(hostId);
        if (currentUser.getSubscriptions().contains(host)) {
            host.detach(currentUser);
        } else {
            host.attach(currentUser);
        }
    }

    public void subscribeOrUnsubscribeFromPlaylist(int playlistId) {
        User currentUser = UserService.getInstance().getCurrentUser();
        Observable playlist = PlaylistService.getInstance().getPlaylist(playlistId);
        if (currentUser.getSubscriptions().contains(playlist)) {
            playlist.detach(currentUser);
        } else {
            playlist.attach(currentUser);
        }
    }

    public void viewNotifications() {
        User currentUser = UserService.getInstance().getCurrentUser();
        currentUser.getNotifications().forEach(notification -> {
            if (!notification.isRead()) {
                notification.setRead(true);
            }
            System.out.println(notification);
        });
    }

    public void clearNotifications() {
        User currentUser = UserService.getInstance().getCurrentUser();
        currentUser.getNotifications().clear();
    }
}
