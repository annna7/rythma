package models.audio.items;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Song extends PlayableItem {
    private final List<String> genres = new ArrayList<>();
    public Song(String title, int length, int albumId, LocalDate release) {
        super(title, length, release, albumId);
    }

    public Song(int id, String title, int length, int albumId, LocalDate release, List<String> genres) {
        this(title, length, albumId, release);
        this.id = id;
        this.genres.addAll(genres);
    }

    public void addGenre(String genre) {
        genres.add(genre);
    }

    public List<String> getGenres() {
        return genres;
    }

    @Override
    public String toString() {
        String genreList = genres.isEmpty() ? "No genres" : String.join(", ", genres);
        return String.format("Song: %s (Length: %d mins, Released: %s, Album ID: %d, Genres: %s)",
                title, length, release, getCollectionId(), genreList);
    }

}
