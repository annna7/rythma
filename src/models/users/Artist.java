package models.users;

import models.audio.collections.Album;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Artist extends User {
    private String biography;
    private List<Album> discography = new ArrayList<>();
    private Map<String, String> socialMediaLinks = new HashMap<>();

    public Artist(String username, String firstName, String lastName, String password, String biography) {
        super(username, firstName, lastName, password);
        this.biography = biography;
    }

    public void addAlbum(Album album) {
        discography.add(album);
    }

    public void removeAlbum(Album album) {
        discography.remove(album);
    }

    public void addSocialMediaLink(String platform, String link) {
        socialMediaLinks.put(platform, link);
    }

    public String getBiography() {
        return biography;
    }

    public List<Album> getDiscography() {
        return discography;
    }

    public Map<String, String> getSocialMediaLinks() {
        return socialMediaLinks;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
