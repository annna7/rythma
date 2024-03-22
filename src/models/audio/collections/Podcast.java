package models.audio.collections;

import models.audio.items.Episode;

public class Podcast extends AudioCollection<Episode> {
    private String description;
    public Podcast(String name) {
        super(name);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
