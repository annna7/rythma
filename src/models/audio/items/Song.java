package models.audio.items;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Song extends PlayableItem {
    private final int albumId;
    private final List<String> genres = new ArrayList<>();
    public Song(String title, int length, int albumId, LocalDate release) {
        super(title, length, release);
        this.albumId = albumId;
    }

    public void addGenre(String genre) {
        genres.add(genre);
    }

    public List<String> getGenres() {
        return genres;
    }

    public int getAlbumId() {
        return albumId;
    }

    @Override
    public String toString() {
        return "Song{" + super.toString() +
                "albumId=" + albumId +
                ", genres=" + genres +
                '}';
    }
}
