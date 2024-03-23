package services;

import models.audio.collections.Album;
import models.audio.items.Song;
import models.users.Artist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MusicService {
    private static MusicService instance = null;
    private static final List<Song> allSongs = new ArrayList<>();
    private static final List<Album> albums = new ArrayList<>();
    private MusicService() {}

    public static MusicService getInstance() {
        if (instance == null) {
            instance = new MusicService();
        }
        return instance;
    }

    public Album getAlbum(int albumId) {
        try {
            return albums.stream().filter(a -> a.getId() == albumId).findFirst().get();
        } catch (Exception e) {
            return null;
        }
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public void addSongToAlbum(Song song, int albumId) {
        Album album = getAlbum(albumId);
        allSongs.add(song);
    }

    public void removeAlbum(int albumId) {
        Artist currentUser = (Artist) UserService.getInstance().getCurrentUser();
        if (currentUser == null) {
            throw new IllegalArgumentException("Current user is not an artist");
        }
        Album album = albums.stream().filter(a -> a.getId() == albumId).findFirst().orElse(null);
        if (album != null) {
            albums.remove(album);
            currentUser.removeAlbum(album);
        }
    }
}
