package services;

import exceptions.IllegalOperationException;
import exceptions.UnauthorizedAccessException;
import models.audio.collections.Playlist;
import models.users.Artist;
import models.users.Host;
import models.users.User;

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
        Artist artist = UserService.getInstance().getArtistById(artistId);
        if (currentUser.getSubscriptions().contains(artist)) {
            currentUser.unsubscribe(artist);
        } else {
            currentUser.subscribe(artist);
        }
    }

    public void subscribeOrUnsubscribeFromHost(int hostId) {
        User currentUser = UserService.getInstance().getCurrentUser();
        Host host = UserService.getInstance().getHostById(hostId);
        if (currentUser.getSubscriptions().contains(host)) {
            currentUser.unsubscribe(host);
        } else {
            currentUser.subscribe(host);
        }
    }

    public void subscribeOrUnsubscribeFromPlaylist(int playlistId) {
        User currentUser = UserService.getInstance().getCurrentUser();
        Playlist playlist = PlaylistService.getInstance().getPlaylist(playlistId);
        if (currentUser.getSubscriptions().contains(playlist)) {
            currentUser.unsubscribe(playlist);
        } else {
            if (!playlist.isPublic()) {
                throw new IllegalOperationException("You cannot subscribe to a private playlist");
            } else if (playlist.getOwnerId() == currentUser.getId()) {
                throw new IllegalOperationException("You cannot subscribe to your own playlist");
            }
            currentUser.subscribe(playlist);
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
