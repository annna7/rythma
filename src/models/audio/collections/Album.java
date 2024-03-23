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

    @Override
    public String toString() {
        String albumInfo = "Album{" +
                "id=" + getId() + ", " +
                "name='" + getName() + '\'' +
                ", label='" + label + '\'' +
                '}';
        StringBuilder songsInfo = new StringBuilder();
        for (Song song : getItems()) {
            songsInfo.append(song.toString()).append("\n");
        }
        return albumInfo + "\n" + songsInfo;
    }
}
