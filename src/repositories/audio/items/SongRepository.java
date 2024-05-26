package repositories.audio.items;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.DatabaseConnector;
import models.audio.items.Song;
import repositories.IRepository;
import utils.JsonUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SongRepository implements IRepository<Song> {
    private static final String GET_ALL_SONGS = "SELECT pi.ItemID, pi.Title, pi.Length, pi.ReleaseDate, pi.CollectionID, s.Genres FROM PlayableItem pi JOIN Song s ON pi.ItemID = s.SongID";
    private static final String GET_SONG_BY_ID = "SELECT pi.ItemID, pi.Title, pi.Length, pi.ReleaseDate, pi.CollectionID, s.Genres FROM PlayableItem pi JOIN Song s ON pi.ItemID = s.SongID WHERE pi.ItemID = ?";
    private static final String INSERT_SONG = "INSERT INTO PlayableItem (Title, Length, ReleaseDate, CollectionID) VALUES (?, ?, ?, ?);";
    private static final String INSERT_SONG_GENRES = "INSERT INTO Song (SongID, Genres) VALUES (?, ?);";
    private static final String UPDATE_SONG = "UPDATE PlayableItem pi JOIN Song s ON pi.ItemID = s.SongID SET pi.Title = ?, pi.Length = ?, pi.ReleaseDate = ?, pi.CollectionID = ?, s.Genres = ? WHERE pi.ItemID = ?";
    private static final String DELETE_SONG = "DELETE FROM PlayableItem WHERE ItemID = ?";
    private static final String UPDATE_GENRES = "UPDATE Song SET Genres = JSON_MERGE_PRESERVE(Genres, ?) WHERE SongID = ?";

    public boolean updateGenres(int songId, List<String> newGenres) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_GENRES)) {
            stmt.setString(1, JsonUtils.toJson(newGenres));
            stmt.setInt(2, songId);
            return stmt.executeUpdate() > 0;
        }
    }


    @Override
    public List<Song> findAll() throws SQLException {
        List<Song> songs = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_SONGS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Song song = new Song(rs.getInt("ItemID"),
                        rs.getString("Title"),
                        rs.getInt("Length"),
                        rs.getInt("CollectionID"),
                        rs.getDate("ReleaseDate").toLocalDate(),
                        JsonUtils.fromJsonToList(rs.getString("Genres"))
                );
                songs.add(song);
            }
        }
        return songs;
    }

    @Override
    public Optional<Song> findById(int id) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_SONG_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Song(
                        rs.getInt("ItemID"),
                        rs.getString("Title"),
                        rs.getInt("Length"),
                        rs.getInt("CollectionID"),
                        rs.getDate("ReleaseDate").toLocalDate(),
                        JsonUtils.fromJsonToList(rs.getString("Genres"))
                ));
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean create(Song item) throws SQLException {
        Connection conn = null;
        PreparedStatement stmtPlayableItem = null;
        PreparedStatement stmtSong = null;
        boolean result = false;
        try {
            conn = DatabaseConnector.connect();
            conn.setAutoCommit(false);

            stmtPlayableItem = conn.prepareStatement(INSERT_SONG, Statement.RETURN_GENERATED_KEYS);
            stmtPlayableItem.setString(1, item.getTitle());
            stmtPlayableItem.setInt(2, item.getLength());
            stmtPlayableItem.setDate(3, Date.valueOf(item.getRelease()));
            stmtPlayableItem.setInt(4, item.getCollectionId());
            int affectedRows = stmtPlayableItem.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            ResultSet generatedKeys = stmtPlayableItem.getGeneratedKeys();
            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                item.setId(newId);
                stmtSong = conn.prepareStatement(INSERT_SONG_GENRES);
                stmtSong.setInt(1, newId);
                stmtSong.setString(2, JsonUtils.toJson(item.getGenres()));
                stmtSong.executeUpdate();
                conn.commit();
                result = true;
            } else {
                conn.rollback();
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        } finally {
            if (stmtPlayableItem != null) stmtPlayableItem.close();
            if (stmtSong != null) stmtSong.close();
            if (conn != null) conn.setAutoCommit(true);
            if (conn != null) conn.close();
        }
        return result;
    }

    @Override
    public boolean update(Song item) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SONG)) {
            stmt.setString(1, item.getTitle());
            stmt.setInt(2, item.getLength());
            stmt.setDate(3, Date.valueOf(item.getRelease()));
            stmt.setInt(4, item.getCollectionId());
            stmt.setString(5, JsonUtils.toJson(item.getGenres()));
            stmt.setInt(6, item.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SONG)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
