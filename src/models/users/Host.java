package models.users;

import models.audio.collections.Podcast;

import java.util.ArrayList;
import java.util.List;

public class Host extends User {
    private final List<Podcast> podcasts = new ArrayList<>();
    private final String affiliation;

    public Host(String username, String firstName, String lastName, String password, String affiliation) {
        super(username, firstName, lastName, password);
        this.affiliation = affiliation;
    }

    public void addPodcast(Podcast podcast) {
        podcasts.add(podcast);
    }

    public void removePodcast(Podcast podcast) {
        podcasts.remove(podcast);
    }

    public String getAffiliation() {
        return affiliation;
    }

    public List<Podcast> getPodcasts() {
        return podcasts;
    }
}
