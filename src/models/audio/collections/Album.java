package models.audio.collections;

import models.audio.items.Song;

import static utils.OutputUtils.getCollectionMessageWithCommas;

public class Album extends AudioCollection<Song> {
    private final String label;
    public Album(String name, String label) {
        super(name);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return String.format("Album: %s (Label: %s)\nSongs: %s",
                name, label, getCollectionMessageWithCommas("songs", items));
    }
}
