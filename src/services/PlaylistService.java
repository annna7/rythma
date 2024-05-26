package services;

import exceptions.IllegalOperationException;
import exceptions.NotFoundException;
import models.audio.collections.Playlist;
import models.audio.items.Song;
import models.users.User;
import repositories.audio.collections.PlaylistRepository;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class PlaylistService {
    private static PlaylistService instance = null;
    private final List<Playlist> playlists = new ArrayList<>();
    private PlaylistService() {}
    private final PlaylistRepository playlistRepository = new PlaylistRepository();

    public static synchronized PlaylistService getInstance() {
        if (instance == null) {
            instance = new PlaylistService();
        }
        return instance;
    }

    public void addPlaylist(Playlist playlist) {
        User currentUser = UserService.getInstance().getCurrentUser();
        try {
            playlistRepository.create(playlist);
        } catch (SQLException e) {
            System.out.printf("SQL error: %s", e.getMessage());
        }
        playlists.add(playlist);
        currentUser.addPlaylist(playlist);
    }

    public void addSongToPlaylist(int songId, int playlistId) {
        Song song = MusicService.getInstance().getSongById(songId);
        Playlist playlist = getPlaylist(playlistId);
        try {
            playlistRepository.addSongToPlaylist(songId, playlistId);
        } catch (SQLException e) {
            System.out.printf("SQL error: %s", e.getMessage());
        }
        playlist.addItem(song);
    }

    public void removePlaylist(int playlistId) {
        User currentUser = UserService.getInstance().getCurrentUser();
        Playlist playlist = getPlaylist(playlistId);
        try {
            playlistRepository.delete(playlistId);
        } catch (SQLException e) {
            System.out.printf("SQL error: %s", e.getMessage());
        }
        playlists.remove(playlist);
        currentUser.removePlaylist(playlist);
    }

    public void removeSongFromPlaylist(int songId, int playlistId) {
        Song song = MusicService.getInstance().getSongById(songId);
        Playlist playlist = getPlaylist(playlistId);
        playlist.removeItem(song);
    }

    public List<Playlist> getPlaylistsForCurrentUser() {
        User currentUser = UserService.getInstance().getCurrentUser();
        return currentUser.getPlaylists();
    }

    public Playlist getPlaylist(int playlistId) {
        Optional<Playlist> playlistOptional;

        playlistOptional = playlists.stream().filter(playlist -> playlist.getId() == playlistId).findFirst();

        if (playlistOptional.isEmpty()) {
            try {
                    playlistOptional = playlistRepository.findById(playlistId);
                    playlistOptional.ifPresent(playlists::add);
                }
            catch (SQLException ex) {
                System.out.printf("SQL error: %s", ex.getMessage());
            }
        }

        if (playlistOptional.isEmpty()) {
            throw new NotFoundException("Playlist with id " + playlistId + " not found");
        }

        return playlistOptional.get();
//        User currentUser = UserService.getInstance().getCurrentUser();
//        return currentUser.getPlaylists().stream().filter(p -> p.getId() == playlistId).findFirst().orElseThrow(() -> new NotFoundException("Playlist"));
    }

    public void togglePlaylistVisibility(int playlistId) {
        Playlist playlist = getPlaylist(playlistId);
        User currentUser = UserService.getInstance().getCurrentUser();
        if (playlist.getOwnerId() != currentUser.getId()) {
            throw new IllegalOperationException("You can only toggle the visibility of your own playlists");
        }
        playlist.setPublic(!playlist.isPublic());
        try {
            playlistRepository.update(playlist);
        } catch (SQLException e) {
            System.out.printf("SQL error: %s", e.getMessage());
        }
    }

    public List<Playlist> getPlaylists() {
        try {
            return playlistRepository.findAll();
        } catch (SQLException e) {
            System.out.printf("SQL error: %s", e.getMessage());
            return List.of();
        }
    }

    public List<Playlist> getAllPublicPlaylists() {
        return getPlaylists().stream().filter(Playlist::isPublic).toList();
    }
}
