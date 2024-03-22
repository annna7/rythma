package models.audio.items;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Episode extends PlayableItem {
    int episodeNumber;
    private String showNotes;
    private final List<String> guests = new ArrayList<>();
    public Episode(String title, int length, int episodeNumber, LocalDate release) {
        super(title, length, release);
        this.episodeNumber = episodeNumber;
    }

    public void addGuest(String guest) {
        guests.add(guest);
    }

    public void setShowNotes(String showNotes) {
        this.showNotes = showNotes;
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
}
