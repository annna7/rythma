package models.users;

import models.audio.collections.Playlist;
import observable.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User implements Observer {
    private static int idCounter = 0;
    private final int id = idCounter++;
    protected final String username;
    protected final String firstName;
    protected final String lastName;
    protected final String password;
    private final List<Playlist> playlists = new ArrayList<>();
    private final List<String> notifications = new ArrayList<>();

    public User(String username, String firstName, String lastName, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
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

    public String getPassword() { return password; }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }

    public void removePlaylist(Playlist playlist) {
        if (!playlists.contains(playlist)) {
            throw new IllegalArgumentException("Playlist not found");
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
    public void update(String message) {
        notifications.add(message);
    }

    public void getNotifications() {
        for (String notification : notifications) {
            System.out.println(notification);
        }
    }
}
