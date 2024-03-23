package services;

import models.users.Artist;
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

    public void subscribeToArtist(int artistId) {
        User currentUser = UserService.getInstance().getCurrentUser();
        Artist artist = UserService.getInstance().getArtist(artistId);
        artist.attach(currentUser);
    }

    public void unsubscribeFromArtist(int artistId) {
        User currentUser = UserService.getInstance().getCurrentUser();
        Artist artist = UserService.getInstance().getArtist(artistId);
        artist.attach(currentUser);
    }
}
