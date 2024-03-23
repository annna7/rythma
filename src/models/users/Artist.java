package models.users;

import models.audio.collections.Album;
import observable.Observable;
import observable.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Artist extends User {
    private final Observable observable = new Observable();
    private final String biography;
    private final List<Album> albums = new ArrayList<>();
    private final Map<String, String> socialMediaLinks = new HashMap<>();

    public Artist(String username, String firstName, String lastName, String password, String biography) {
        super(username, firstName, lastName, password);
        this.biography = biography;
    }

    public void addAlbum(Album album) {
        albums.add(album);
        notifyObservers("New album added by " + this.getUsername() + ": " + album.getName());
    }

    public void removeAlbum(Album album) {
        albums.remove(album);
    }

    public void addSocialMediaLink(String platform, String link) {
        socialMediaLinks.put(platform, link);
    }

    public void removeSocialMediaLink(String platform) {
        if (socialMediaLinks.containsKey(platform)) {
            socialMediaLinks.remove(platform);
        } else {
            throw new IllegalArgumentException("Social media link not found");
        }
    }

    public String getBiography() {
        return biography;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public Map<String, String> getSocialMediaLinks() {
        return socialMediaLinks;
    }

    @Override
    public String toString() {
        return "Artist{" +
                super.toString() +
                ", biography='" + biography + '\'' +
                ", socialMediaLinks=" + socialMediaLinks +
                '}';
    }

    public void attach(Observer observer) {
        observable.attach(observer);
    }

    public void detach(Observer observer) {
        observable.detach(observer);
    }

    public void notifyObservers(String message) {
        observable.notifyObservers(message);
    }
}
