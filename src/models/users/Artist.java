package models.users;

import enums.NotificationTypeEnum;
import exceptions.NotFoundException;
import models.Notification;
import models.audio.collections.Album;
import observable.Observable;
import observable.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Artist extends User implements Observable {
    private final String biography;
    private final List<Album> albums = new ArrayList<>();
    private final Map<String, String> socialMediaLinks = new HashMap<>();
    private final List<Observer> observers = new ArrayList<>();

    public Artist(String username, String firstName, String lastName, String password, String biography) {
        super(username, firstName, lastName, password);
        this.biography = biography;
    }

    public Artist(int id, String username, String firstName, String lastName, String password, String biography) {
        this(username, firstName, lastName, password, biography);
        this.id = id;
    }

    public void addAlbum(Album album) {
        albums.add(album);
        notifyObservers(new Notification(NotificationTypeEnum.NEW_SONG_RELEASE, "New album added by " + this.getUsername() + ": " + album.getName()));
    }

    public void removeAlbum(Album album) {
        albums.remove(album);
    }

    public void setSocialMediaLinks(Map<String, String> socialMediaLinks) {
        this.socialMediaLinks.putAll(socialMediaLinks);
    }

    public void addSocialMediaLink(String platform, String link) {
        socialMediaLinks.put(platform, link);
    }

    public void removeSocialMediaLink(String platform) {
        if (socialMediaLinks.containsKey(platform)) {
            socialMediaLinks.remove(platform);
        } else {
            throw new NotFoundException("Social media link");
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
        return String.format("Artist [ID: %d, Username: '%s', Name: '%s %s', Biography: '%s', Albums: %d, Social Media Links: %s]",
                getId(),
                getUsername(),
                getFirstName(),
                getLastName(),
                biography,
                albums.size(),
                socialMediaLinks);
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Notification notification) {
        observers.forEach(observer -> observer.update(notification));
    }
}
