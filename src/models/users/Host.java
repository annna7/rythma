package models.users;

import models.audio.collections.Podcast;

import java.util.ArrayList;
import java.util.List;

public class Host extends User {
    private final List<Podcast> podcasts = new ArrayList<>();
    private String affiliation;

    public Host(String username, String firstName, String lastName, String affiliation) {
        super(username, firstName, lastName);
        this.affiliation = affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }
}
