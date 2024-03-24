package models.audio.collections;

import enums.NotificationTypeEnum;
import models.Notification;
import models.audio.items.Song;
import observable.Observable;
import observable.Observer;

import java.util.ArrayList;
import java.util.List;

public class Playlist extends AudioCollection<Song> implements Observable {
    private final String description;
    private final List<Observer> observers = new ArrayList<>();
    public Playlist(String name, String description, boolean isPublic) {
        super(name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void addItem(Song item) {
        super.addItem(item);
        notifyObservers(new Notification(NotificationTypeEnum.NEW_SONG_ADDED_TO_PLAYLIST, "New song added to playlist " + this.getName() + ": " + item.getTitle()));
    }

    @Override
    public String toString() {
        return "Playlist{" +
                super.toString() +
                "description='" + description + '\'' +
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
