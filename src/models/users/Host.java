package models.users;

import enums.NotificationTypeEnum;
import models.Notification;
import models.audio.collections.Podcast;
import observable.Observable;
import observable.Observer;

import java.util.ArrayList;
import java.util.List;

public class Host extends User implements Observable {
    private final List<Podcast> podcasts = new ArrayList<>();
    private String affiliation;
    private final List<Observer> observers = new ArrayList<>();

    public Host(String username, String firstName, String lastName, String password, String affiliation) {
        super(username, firstName, lastName, password);
        this.affiliation = affiliation;
    }


    public Host(int id, String username, String firstName, String lastName, String password, String affiliation) {
        this(username, firstName, lastName, password, affiliation);
        this.id = id;
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

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public List<Podcast> getPodcasts() {
        return podcasts;
    }

    @Override
    public String toString() {
        return String.format("Host [ID: %d, Username: '%s', Name: '%s %s', Affiliation: '%s', Podcasts: %d]",
                getId(),
                getUsername(),
                getFirstName(),
                getLastName(),
                affiliation,
                podcasts.size());
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
