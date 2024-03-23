package services;

import models.audio.collections.Playlist;
import models.audio.items.Song;
import models.users.User;

import java.util.*;

public class PlaylistService {
    private static PlaylistService instance = null;
    private final List<Playlist> playlists = new ArrayList<>();
    private PlaylistService() {}

    public static synchronized PlaylistService getInstance() {
        if (instance == null) {
            instance = new PlaylistService();
        }
        return instance;
    }

    public void addPlaylist(Playlist playlist) {
        User currentUser = UserService.getInstance().getCurrentUser();
        playlists.add(playlist);
        currentUser.addPlaylist(playlist);
    }

    public void addSongToPlaylist(int songId, int playlistId) {
        Song song = MusicService.getInstance().getSong(songId);
        if (song == null) {
            throw new IllegalArgumentException("Song not found");
        }
        Playlist playlist = getPlaylist(playlistId);
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist not found");
        }
        playlist.addItem(song);
    }

    public void removePlaylist(int playlistId) {
        User currentUser = UserService.getInstance().getCurrentUser();
        Playlist playlist = getPlaylist(playlistId);
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist not found");
        }
        playlists.remove(playlist);
        currentUser.removePlaylist(playlist);
    }

    public void removeSongFromPlaylist(int songId, int playlistId) {
        Song song = MusicService.getInstance().getSong(songId);
        if (song == null) {
            throw new IllegalArgumentException("Song not found");
        }
        Playlist playlist = getPlaylist(playlistId);
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist not found");
        }
        playlist.removeItem(song);
    }

    public ArrayList<Playlist> getPlaylistsForCurrentUser() {
        User currentUser = UserService.getInstance().getCurrentUser();
        return (ArrayList<Playlist>) currentUser.getPlaylists();
    }

    private Playlist getPlaylist(int playlistId) {
        User currentUser = UserService.getInstance().getCurrentUser();
        return currentUser.getPlaylists().stream().filter(p -> p.getId() == playlistId).findFirst().orElse(null);
    }
}
