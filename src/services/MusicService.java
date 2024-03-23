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

    public Song getSong(int songId) {
        try {
            return allSongs.stream().filter(s -> s.getId() == songId).findFirst().get();
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<Album> getCurrentAlbums() {
        Artist currentUser = (Artist) UserService.getInstance().getCurrentUser();
        if (currentUser == null) {
            throw new IllegalArgumentException("Current user is not an artist");
        }
        return (ArrayList<Album>) currentUser.getAlbums();
    }

    public void addAlbum(Album album) {
        Artist currentUser = (Artist) UserService.getInstance().getCurrentUser();
        if (currentUser == null) {
            throw new IllegalArgumentException("Current user is not an artist");
        }
        currentUser.addAlbum(album);
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

    public void addSocialMediaLinkToArtist(String platform, String link) {
        Artist currentUser = (Artist) UserService.getInstance().getCurrentUser();
        if (currentUser == null) {
            throw new IllegalArgumentException("Current user is not an artist");
        }
        currentUser.addSocialMediaLink(platform, link);
    }

    public void removeSocialMediaLinkFromArtist(String platform) {
        Artist currentUser = (Artist) UserService.getInstance().getCurrentUser();
        if (currentUser == null) {
            throw new IllegalArgumentException("Current user is not an artist");
        }
        currentUser.removeSocialMediaLink(platform);
    }

    public void removeAlbum(int albumId) {
        Artist currentUser = (Artist) UserService.getInstance().getCurrentUser();
        if (currentUser == null) {
            throw new IllegalArgumentException("Current user is not an artist");
        }
        Album album = getAlbum(albumId);
        if (album != null) {
            albums.remove(album);
            currentUser.removeAlbum(album);
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
