package models.audio.collections;

import models.audio.items.Episode;

public class Podcast extends AudioCollection<Episode> {
    private final String description;
    public Podcast(String name, String description) {
        super(name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Podcast{" +
                super.toString() +
                "description='" + description + '\'' +
                '}';
    }
}
