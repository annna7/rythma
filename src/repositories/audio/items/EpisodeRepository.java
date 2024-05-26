package repositories.audio.items;

import database.DatabaseConnector;
import models.audio.items.Episode;
import repositories.IRepository;
import utils.JsonUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EpisodeRepository implements IRepository<Episode> {
    private static final String GET_ALL_EPISODES = "SELECT pi.ItemID, pi.Title, pi.Length, pi.ReleaseDate, pi.CollectionID, e.EpisodeNumber, e.ShowNotes, e.Guests FROM PlayableItem pi JOIN Episode e ON pi.ItemID = e.EpisodeID";
    private static final String GET_EPISODE_BY_ID = "SELECT pi.ItemID, pi.Title, pi.Length, pi.ReleaseDate, pi.CollectionID, e.EpisodeNumber, e.ShowNotes, e.Guests FROM PlayableItem pi JOIN Episode e ON pi.ItemID = e.EpisodeID WHERE pi.ItemID = ?";
    private static final String INSERT_EPISODE = "INSERT INTO PlayableItem (Title, Length, ReleaseDate, CollectionID) VALUES (?, ?, ?, ?);";
    private static final String INSERT_EPISODE_DETAILS = "INSERT INTO Episode (EpisodeID, EpisodeNumber, ShowNotes, Guests) VALUES (?, ?, ?, ?);";
    private static final String UPDATE_EPISODE = "UPDATE PlayableItem pi JOIN Episode e ON pi.ItemID = e.EpisodeID SET pi.Title = ?, pi.Length = ?, pi.ReleaseDate = ?, pi.CollectionID = ?, e.EpisodeNumber = ?, e.ShowNotes = ?, e.Guests = ? WHERE pi.ItemID = ?";
    private static final String DELETE_EPISODE = "DELETE FROM PlayableItem WHERE ItemID = ?";

    @Override
    public List<Episode> findAll() throws SQLException {
        List<Episode> episodes = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_EPISODES);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                episodes.add(new Episode(
                        rs.getInt("ItemID"),
                        rs.getString("Title"),
                        rs.getInt("Length"),
                        rs.getDate("ReleaseDate").toLocalDate(),
                        rs.getInt("CollectionID"),
                        rs.getInt("EpisodeNumber"),
                        rs.getString("ShowNotes"),
                        JsonUtils.fromJsonToList(rs.getString("Guests"))
                ));
            }
            return episodes;
        }
    }

    @Override
    public Optional<Episode> findById(int id) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_EPISODE_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Episode(
                        rs.getInt("ItemID"),
                        rs.getString("Title"),
                        rs.getInt("Length"),
                        rs.getDate("ReleaseDate").toLocalDate(),
                        rs.getInt("CollectionID"),
                        rs.getInt("EpisodeNumber"),
                        rs.getString("ShowNotes"),
                        JsonUtils.fromJsonToList(rs.getString("Guests"))
                ));
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean create(Episode item) throws SQLException {
        Connection conn = null;
        PreparedStatement stmtPlayableItem = null;
        PreparedStatement stmtEpisode = null;
        boolean result = false;
        try {
            conn = DatabaseConnector.connect();
            conn.setAutoCommit(false);

            stmtPlayableItem = conn.prepareStatement(INSERT_EPISODE, Statement.RETURN_GENERATED_KEYS);
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
                item.setId(generatedKeys.getInt(1));
                stmtEpisode = conn.prepareStatement(INSERT_EPISODE_DETAILS);
                stmtEpisode.setInt(1, generatedKeys.getInt(1));
                stmtEpisode.setInt(2, item.getEpisodeNumber());
                stmtEpisode.setString(3, item.getShowNotes());
                stmtEpisode.setString(4, JsonUtils.toJson(item.getGuests()));
                stmtEpisode.executeUpdate();
                conn.commit();
                result = true;
            } else {
                conn.rollback();
            }
        } catch (SQLException ex) {
            System.out.printf("SQL error: %s\n", ex.getMessage());
        } finally {
            if (stmtPlayableItem != null) stmtPlayableItem.close();
            if (stmtEpisode != null) stmtEpisode.close();
            if (conn != null) conn.setAutoCommit(true);
            if (conn != null) conn.close();
        }
        return result;
    }

    @Override
    public boolean update(Episode item) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_EPISODE)) {
            stmt.setString(1, item.getTitle());
            stmt.setInt(2, item.getLength());
            stmt.setDate(3, Date.valueOf(item.getRelease()));
            stmt.setInt(4, item.getCollectionId());
            stmt.setInt(5, item.getEpisodeNumber());
            stmt.setString(6, item.getShowNotes());
            stmt.setString(7, JsonUtils.toJson(item.getGuests()));
            stmt.setInt(8, item.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(DELETE_EPISODE)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
