package models.users;

import exceptions.NotFoundException;
import models.Notification;
import models.audio.collections.Playlist;
import observable.Observer;
import observable.Observable;

import java.util.*;

public class User implements Observer {
    private static int idCounter = 0;
    private final int id = idCounter++;
    protected final String displayName;
    protected final String username;
    protected final String firstName;
    protected final String lastName;
    protected final String password;
    private final List<Playlist> playlists = new ArrayList<>();
    // Sorted Container - Notifications are sorted by priority, timestamp, and whether they have been read
    private final Set<Notification> notifications = new TreeSet<>();
    public final Set<Observable> subscriptions = new HashSet<>();

    public User(String username, String firstName, String lastName, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.displayName = firstName + " " + lastName;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDisplayName() {
        return displayName;
    }
    public String getPassword() { return password; }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }

    public void removePlaylist(Playlist playlist) {
        if (!playlists.contains(playlist)) {
            throw new NotFoundException("Playlist");
        }
        playlists.remove(playlist);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", playlists=" + playlists +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User user = (User) obj;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public void update(Notification notification) {
        notifications.add(notification);
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public Set<Observable> getSubscriptions() {
        return subscriptions;
    }

    // TODO: Why is this unused?
    public void subscribe(Observable observable) {
        subscriptions.add(observable);
        observable.attach(this);
    }
}
