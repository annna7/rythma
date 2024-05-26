package repositories.audio.collections;

import database.DatabaseConnector;
import models.audio.collections.Playlist;
import models.audio.items.Song;
import repositories.IRepository;
import repositories.audio.items.SongRepository;

import java.sql.*;
import java.util.*;

public class PlaylistRepository implements IRepository<Playlist> {
    private static final String INSERT_COLLECTION = "INSERT INTO AudioCollection (OwnerID, Name) VALUES (?, ?);";
    private static final String INSERT_PLAYLIST = "INSERT INTO Playlist (PlaylistID, Description, IsPublic) VALUES (LAST_INSERT_ID(), ?, ?);";
    private static final String UPDATE_PLAYLIST = "UPDATE AudioCollection ac JOIN Playlist p ON ac.CollectionID = p.PlaylistID SET ac.Name = ?, p.Description = ?, p.IsPublic = ? WHERE ac.CollectionID = ?;";
    private static final String DELETE_PLAYLIST = "DELETE FROM AudioCollection WHERE CollectionID = ?;";
    private static final String GET_ALL_PLAYLISTS = "SELECT ac.CollectionID, ac.Name, ac.OwnerID, p.Description, p.IsPublic, " +
            "s.ItemID, s.Title, s.Length, s.ReleaseDate, ss.Genres " +
            "FROM AudioCollection ac " +
            "JOIN Playlist p ON ac.CollectionID = p.PlaylistID " +
            "LEFT JOIN PlaylistSongs ps ON p.PlaylistID = ps.PlaylistID " +
            "LEFT JOIN PlayableItem s ON ps.SongID = s.ItemID" +
            " JOIN Song ss ON s.ItemID = ss.Genres ;";

    private static final String GET_PLAYLIST_BY_ID = "SELECT ac.CollectionID, ac.Name, ac.OwnerID, p.Description, p.IsPublic, " +
            "s.ItemID, s.Title, s.Length, s.ReleaseDate, ss.Genres " +
            "FROM AudioCollection ac " +
            "JOIN Playlist p ON ac.CollectionID = p.PlaylistID " +
            "LEFT JOIN PlaylistSongs ps ON p.PlaylistID = ps.PlaylistID " +
            "LEFT JOIN PlayableItem s ON ps.SongID = s.ItemID " +
            "LEFT JOIN Song ss ON s.ItemId = ss.songId" +
            " WHERE ac.CollectionID = ?;";

    private static final String GET_PLAYLISTS_BY_USER_ID = "SELECT ac.CollectionID, ac.Name, ac.OwnerID, p.Description, p.IsPublic, " +
            "s.ItemID, s.Title, s.Length, s.ReleaseDate, ss.Genres " +
            "FROM AudioCollection ac " +
            "JOIN Playlist p ON ac.CollectionID = p.PlaylistID " +
            "LEFT JOIN PlaylistSongs ps ON p.PlaylistID = ps.PlaylistID " +
            "LEFT JOIN PlayableItem s ON ps.SongID = s.ItemID " +
            "LEFT JOIN Song ss ON s.ItemId = ss.songId" +
            " WHERE ac.OwnerID = ?;";


    private static final String ADD_SONG_TO_PLAYLIST = "INSERT INTO PlaylistSongs (PlaylistID, SongID) VALUES (?, ?);";

    private final SongRepository songRepository = new SongRepository();

    public void addSongToPlaylist(int songId, int playlistId) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(ADD_SONG_TO_PLAYLIST)) {
            stmt.setInt(1, playlistId);
            stmt.setInt(2, songId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating song to playlist failed!");
            }
        }
    }

    private void processPlaylistsResultSet(ResultSet rs, List<Playlist> playlists, Map<Integer, List<Integer>> playlistSongsMap) throws SQLException {
        Playlist currentPlaylist = null;
        while (rs.next()) {
            int playlistId = rs.getInt("CollectionID");
            currentPlaylist = findPlaylistById(playlists, playlistId);
            if (currentPlaylist == null) {
                currentPlaylist = new Playlist(
                        rs.getInt("CollectionID"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getBoolean("IsPublic"),
                        rs.getInt("OwnerID")
                );
                playlists.add(currentPlaylist);
                playlistSongsMap.put(playlistId, new ArrayList<>());
            }
            int songId = rs.getInt("ItemID");
            if (songId != 0) {
                playlistSongsMap.get(playlistId).add(songId);
            }
        }
    }

    private void populatePlaylistsWithSongs(List<Playlist> playlists, Map<Integer, List<Integer>> playlistSongsMap) throws SQLException {
        for (Playlist playlist : playlists) {
            List<Integer> songIds = playlistSongsMap.get(playlist.getId());
            if (songIds != null) {
                for (Integer songId : songIds) {
                    Optional<Song> song = songRepository.findById(songId);
                    song.ifPresent(playlist::addItem);
                }
            }
        }
    }

    public List<Playlist> findAll() throws SQLException {
        List<Playlist> playlists = new ArrayList<>();
        Map<Integer, List<Integer>> playlistSongsMap = new HashMap<>();
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_PLAYLISTS);
             ResultSet rs = stmt.executeQuery()) {
            processPlaylistsResultSet(rs, playlists, playlistSongsMap);
        }
        populatePlaylistsWithSongs(playlists, playlistSongsMap);
        return playlists;
    }

    public List<Playlist> findAllPlaylistsByUserId(int userId) throws SQLException {
        List<Playlist> playlists = new ArrayList<>();
        Map<Integer, List<Integer>> playlistSongsMap = new HashMap<>();
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_PLAYLISTS_BY_USER_ID)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            processPlaylistsResultSet(rs, playlists, playlistSongsMap);
        }
        populatePlaylistsWithSongs(playlists, playlistSongsMap);
        return playlists;
    }

    public Optional<Playlist> findById(int id) throws SQLException {
        List<Playlist> playlists = new ArrayList<>();
        Map<Integer, List<Integer>> playlistSongsMap = new HashMap<>();
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_PLAYLIST_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            processPlaylistsResultSet(rs, playlists, playlistSongsMap);
        }
        populatePlaylistsWithSongs(playlists, playlistSongsMap);
        return playlists.stream().findFirst();
    }

    private Playlist findPlaylistById(List<Playlist> playlists, int playlistId) {
        return playlists.stream()
                .filter(p -> p.getId() == playlistId)
                .findFirst()
                .orElse(null);
    }
    public boolean create(Playlist playlist) throws SQLException {
        Connection conn = null;
        PreparedStatement stmtCollection = null;
        PreparedStatement stmtPlaylist = null;
        boolean result = false;
        try {
            conn = DatabaseConnector.connect();
            conn.setAutoCommit(false);

            stmtCollection = conn.prepareStatement(INSERT_COLLECTION, Statement.RETURN_GENERATED_KEYS);
            stmtCollection.setInt(1, playlist.getOwnerId());
            stmtCollection.setString(2, playlist.getName());
            int affectedRows = stmtCollection.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            ResultSet generatedKeys = stmtCollection.getGeneratedKeys();
            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                playlist.setId(newId);
                stmtPlaylist = conn.prepareStatement(INSERT_PLAYLIST);
                stmtPlaylist.setString(1, playlist.getDescription());
                stmtPlaylist.setBoolean(2, playlist.isPublic());
                stmtPlaylist.executeUpdate();
                conn.commit();
                result = true;
            } else {
                conn.rollback();
            }
        } catch (SQLException ex) {
            System.out.printf("SQL error: %s\n", ex.getMessage());
        } finally {
            if (stmtCollection != null) stmtCollection.close();
            if (stmtPlaylist != null) stmtPlaylist.close();
            if (conn != null) conn.setAutoCommit(true);
            if (conn != null) conn.close();
        }
        return result;
    }

    public boolean update(Playlist playlist) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PLAYLIST)) {
            stmt.setString(1, playlist.getName());
            stmt.setString(2, playlist.getDescription());
            stmt.setBoolean(3, playlist.isPublic());
            stmt.setInt(4, playlist.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(DELETE_PLAYLIST)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
