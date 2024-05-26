package models.audio.collections;

import models.audio.items.Song;

import java.util.List;

import static utils.OutputUtils.getCollectionMessageWithCommas;

public class Album extends AudioCollection<Song> {
    private final String label;
    public Album(String name, String label) {
        super(name);
        this.label = label;
    }

    public Album(int id, String name, String label, int ownerId) {
        super(id, ownerId, name);
        this.label = label;
    }

    public Album(int id, String name, String label, int ownerId, List<Song> songs) {
        this(id, name, label, ownerId);
        this.items = songs;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return String.format("Album: %d %s (Label: %s)\nSongs: %s",
                id, name, label, getCollectionMessageWithCommas("songs", items));
    }
}
