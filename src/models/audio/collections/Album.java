package models.audio.collections;

import models.audio.items.Song;

public class Album extends AudioCollection<Song> {
    private final String label;
    public Album(String name, String label) {
        super(name);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }


}
