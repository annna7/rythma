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

    public void addPodcast(Podcast podcast) {
        podcasts.add(podcast);
        notifyObservers(new Notification(NotificationTypeEnum.NEW_EPISODE,"New podcast added by " + this.getUsername() + ": " + podcast.getName()));
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
        return "Host{" +
                super.toString() +
                "affiliation='" + affiliation + '\'' +
                '}';
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
