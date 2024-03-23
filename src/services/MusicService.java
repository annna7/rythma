package services;

import exceptions.NotFoundException;
import models.audio.collections.Album;
import models.audio.items.Song;
import models.users.Artist;

import java.util.ArrayList;
import java.util.List;

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
        return albums.stream().filter(a -> a.getId() == albumId).findFirst().orElseThrow(() -> new NotFoundException("Album"));
    }

    public Song getSong(int songId) {
        return allSongs.stream().filter(s -> s.getId() == songId).findFirst().orElseThrow(() -> new NotFoundException("Song"));
    }

    public List<Album> getCurrentAlbums() {
        return UserService.getInstance().getCurrentArtist().getAlbums();
    }

    public void addAlbum(Album album) {
        Artist artist = UserService.getInstance().getCurrentArtist();
        artist.addAlbum(album);
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
        Album album = getAlbum(albumId);
        Artist artist = UserService.getInstance().getCurrentArtist();
        if (album.getOwnerId() != artist.getId()) {
            throw new IllegalArgumentException("You can't remove an album you don't own");
        }
        artist.removeAlbum(album);
        albums.remove(album);
    }

    public void removeSongFromAlbum(int songId) {
        Song song = getSong(songId);
        Album album = getAlbum(song.getAlbumId());

        if (album.getOwnerId() != UserService.getInstance().getCurrentUser().getId()) {
            throw new IllegalArgumentException("You can't remove a song from an album you don't own");
        }

        album.removeItem(song);
        allSongs.remove(song);
    }




}
