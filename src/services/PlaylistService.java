package services;

import exceptions.IllegalOperationException;
import exceptions.NotFoundException;
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
        Playlist playlist = getPlaylist(playlistId);
        playlist.addItem(song);
    }

    public void removePlaylist(int playlistId) {
        User currentUser = UserService.getInstance().getCurrentUser();
        Playlist playlist = getPlaylist(playlistId);
        playlists.remove(playlist);
        currentUser.removePlaylist(playlist);
    }

    public void removeSongFromPlaylist(int songId, int playlistId) {
        Song song = MusicService.getInstance().getSong(songId);
        Playlist playlist = getPlaylist(playlistId);
        playlist.removeItem(song);
    }

    public List<Playlist> getPlaylistsForCurrentUser() {
        User currentUser = UserService.getInstance().getCurrentUser();
        return currentUser.getPlaylists();
    }

    public Playlist getPlaylist(int playlistId) {
        User currentUser = UserService.getInstance().getCurrentUser();
        return currentUser.getPlaylists().stream().filter(p -> p.getId() == playlistId).findFirst().orElseThrow(() -> new NotFoundException("Playlist"));
    }

    public void togglePlaylistVisibility(int playlistId) {
        Playlist playlist = getPlaylist(playlistId);
        User currentUser = UserService.getInstance().getCurrentUser();
        if (playlist.getOwnerId() != currentUser.getId()) {
            throw new IllegalOperationException("You can only toggle the visibility of your own playlists");
        }
        playlist.setPublic(!playlist.isPublic());
    }

    public List<Playlist> getAllPublicPlaylists() {
        return playlists.stream().filter(Playlist::isPublic).toList();
    }
}
