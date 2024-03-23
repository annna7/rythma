package models.audio.collections;

import models.audio.items.Song;

public class Playlist extends AudioCollection<Song> {
    private final String description;
    public Playlist(String name, String description) {
        super(name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                super.toString() +
                "description='" + description + '\'' +
                '}';
    }
}
