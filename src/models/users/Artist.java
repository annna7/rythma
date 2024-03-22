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

    public Artist(String username, String firstName, String lastName, String biography) {
        super(username, firstName, lastName);
        this.biography = biography;
    }
}
