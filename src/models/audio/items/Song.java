package models.audio.items;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Song extends PlayableItem {
    private final int albumId;
    private final List<String> genres = new ArrayList<>();
    private String lyrics;
    public Song(String title, int length, int albumId, LocalDate release) {
        super(title, length, release);
        this.albumId = albumId;
    }

    public void addGenre(String genre) {
        genres.add(genre);
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getLyrics() {
        return lyrics;
    }

    public List<String> getGenres() {
        return genres;
    }

    public int getAlbumId() {
        return albumId;
    }

    public String toString() {
        return title + " by " + albumId;
    }
}
