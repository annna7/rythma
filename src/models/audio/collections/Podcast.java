package models.audio.collections;

import models.audio.items.Episode;

import java.util.stream.Collectors;

import static utils.OutputUtils.getCollectionMessageWithCommas;

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
        return String.format("Podcast: %s (Description: %s)\nEpisodes: %s",
                name, description, getCollectionMessageWithCommas("episodes", items));
    }
}
