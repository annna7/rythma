package models.audio.items;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Episode extends PlayableItem {
    private final int episodeNumber;
    private final String showNotes;
    private final List<String> guests = new ArrayList<>();
    public Episode(String title, int length, LocalDate release, int hostId, int episodeNumber, String showNotes) {
        super(title, length, release, hostId);
        this.episodeNumber = episodeNumber;
        this.showNotes = showNotes;
    }

    public void addGuest(String guest) {
        guests.add(guest);
    }

    public String getShowNotes() {
        return showNotes;
    }

    public List<String> getGuests() {
        return guests;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    @Override
    public String toString() {
        return "Episode{" +
                super.toString() +
                "episodeNumber=" + episodeNumber +
                ", showNotes='" + showNotes + '\'' +
                ", guests=" + guests +
                '}';
    }
}
