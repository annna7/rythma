package models.audio.collections;

import models.audio.items.Song;

public class Playlist extends AudioCollection<Song> {
    private String description;
    public Playlist(String name) {
        super(name);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
