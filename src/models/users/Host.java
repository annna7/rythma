package models.users;

import models.audio.collections.Podcast;
import observable.Observable;
import observable.Observer;

import java.util.ArrayList;
import java.util.List;

public class Host extends User {
    private final Observable observable = new Observable();
    private final List<Podcast> podcasts = new ArrayList<>();
    private String affiliation;

    public Host(String username, String firstName, String lastName, String password, String affiliation) {
        super(username, firstName, lastName, password);
        this.affiliation = affiliation;
    }

    public void addPodcast(Podcast podcast) {
        podcasts.add(podcast);
        notifyObservers("New podcast added by " + this.getUsername() + ": " + podcast.getName());
    }

    public void removePodcast(Podcast podcast) {
        podcasts.remove(podcast);
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public List<Podcast> getPodcasts() {
        return podcasts;
    }

    public void attach(Observer observable) {
        this.observable.attach(observable);
    }

    public void detach(Observer observable) {
        this.observable.detach(observable);
    }

    public void notifyObservers(String message) {
        this.observable.notifyObservers(message);
    }
}
