package services;

import models.audio.collections.Album;
import models.audio.collections.Playlist;
import models.audio.collections.Podcast;
import models.audio.items.Episode;
import models.audio.items.Song;
import models.users.Artist;
import models.users.Host;
import models.users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static utils.RoleValidator.validateArtist;

public class MusicService {
    private static MusicService instance = null;
    private static final List<Song> allSongs = new ArrayList<>();
    private static final List<Album> albums = new ArrayList<>();
    private MusicService() {}

    public static synchronized MusicService getInstance() {
        if (instance == null) {
            instance = new MusicService();
        }
        return instance;
    }

    public Album getAlbum(int albumId) {
        return albums.stream().filter(a -> a.getId() == albumId).findFirst().get();
    }

    public Song getSong(int songId) {
        return allSongs.stream().filter(s -> s.getId() == songId).findFirst().get();
    }

    public ArrayList<Album> getCurrentAlbums() {
        User currentUser = UserService.getInstance().getCurrentUser();
        validateArtist(currentUser);
        return (ArrayList<Album>) ((Artist) currentUser).getAlbums();
    }

    public void addAlbum(Album album) {
        User currentUser = UserService.getInstance().getCurrentUser();
        validateArtist(currentUser);
        ((Artist) currentUser).addAlbum(album);
        albums.add(album);
    }

    public void addSongToAlbum(Song song, int albumId) {
        Album album = getAlbum(albumId);
        if (album == null) {
            throw new IllegalArgumentException("Album not found");
        }
        album.addItem(song);
        allSongs.add(song);
    }


    public void removeAlbum(int albumId) {
        User currentUser = UserService.getInstance().getCurrentUser();
        validateArtist(currentUser);
        Album album = getAlbum(albumId);
        if (album != null) {
            albums.remove(album);
            ((Artist) currentUser).removeAlbum(album);
        }
    }

    public void removeSongFromAlbum(int songId) {
        Song song = getSong(songId);
        if (song == null) {
            throw new IllegalArgumentException("Song not found");
        }
        Album album = getAlbum(song.getAlbumId());
        if (album == null) {
            throw new IllegalArgumentException("Album not found");
        }

        if (album.getOwnerId() != UserService.getInstance().getCurrentUser().getId()) {
            throw new IllegalArgumentException("You can't remove a song from an album you don't own");
        }

        album.removeItem(song);
        allSongs.remove(song);
    }




}
