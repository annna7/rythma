package repositories.audio.collections;

import database.DatabaseConnector;
import models.audio.collections.Podcast;
import repositories.IRepository;
import utils.JsonUtils;
import models.audio.items.Episode;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PodcastRepository implements IRepository<Podcast> {
    private static final String INSERT_COLLECTION = "INSERT INTO AudioCollection (OwnerID, Name) VALUES (?, ?);";
    private static final String INSERT_PODCAST = "INSERT INTO Podcast (PodcastID, Description) VALUES (LAST_INSERT_ID(), ?);";
    private static final String UPDATE_PODCAST = "UPDATE AudioCollection ac JOIN Podcast p ON ac.CollectionID = p.PodcastID SET ac.Name = ?, p.Description = ? WHERE ac.CollectionID = ?;";
    private static final String DELETE_PODCAST = "DELETE FROM AudioCollection WHERE CollectionID = ?;";

    private static final String GET_ALL_PODCASTS = "SELECT ac.CollectionID, ac.Name, ac.OwnerID, p.Description, " +
            "pi.ItemID, pi.Title, pi.Length, pi.ReleaseDate, e.EpisodeNumber, e.ShowNotes, e.Guests " +
            "FROM AudioCollection ac " +
            "JOIN Podcast p ON ac.CollectionID = p.PodcastID " +
            "LEFT JOIN PlayableItem pi ON ac.CollectionID = pi.CollectionID " +
            "LEFT JOIN Episode e ON pi.ItemID = e.EpisodeID";

    private static final String GET_PODCAST_BY_ID = "SELECT ac.CollectionID, ac.Name, ac.OwnerID, p.Description, " +
            "pi.ItemID, pi.Title, pi.Length, pi.ReleaseDate, e.EpisodeNumber, e.ShowNotes, e.Guests " +
            "FROM AudioCollection ac " +
            "JOIN Podcast p ON ac.CollectionID = p.PodcastID " +
            "LEFT JOIN PlayableItem pi ON ac.CollectionID = pi.CollectionID " +
            "LEFT JOIN Episode e ON pi.ItemID = e.EpisodeID " +
            "WHERE ac.CollectionID = ?";

    private static final String GET_PODCASTS_BY_HOST_ID = "SELECT ac.CollectionID, ac.Name, ac.OwnerID, p.Description, " +
            "pi.ItemID, pi.Title, pi.Length, pi.ReleaseDate, e.EpisodeNumber, e.ShowNotes, e.Guests " +
            "FROM AudioCollection ac " +
            "JOIN Podcast p ON ac.CollectionID = p.PodcastID " +
            "LEFT JOIN PlayableItem pi ON ac.CollectionID = pi.CollectionID " +
            "LEFT JOIN Episode e ON pi.ItemID = e.EpisodeID " +
            "WHERE ac.OwnerID = ?";

    public List<Podcast> findPodcastsByHostId(int hostId) throws SQLException {
        List<Podcast> podcasts = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_PODCASTS_BY_HOST_ID)) {
            stmt.setInt(1, hostId);
            ResultSet rs = stmt.executeQuery();
            Podcast currentPodcast = null;
            while (rs.next()) {
                int podcastId = rs.getInt("CollectionID");
                if (currentPodcast == null || currentPodcast.getId() != podcastId) {
                    currentPodcast = new Podcast(
                            podcastId,
                            rs.getString("Name"),
                            rs.getString("Description"),
                            rs.getInt("OwnerID"),
                            new ArrayList<>()
                    );
                    podcasts.add(currentPodcast);
                }
                if (rs.getInt("ItemID") != 0) {
                    currentPodcast.getItems().add(new Episode(
                            rs.getInt("ItemID"),
                            rs.getString("Title"),
                            rs.getInt("Length"),
                            rs.getDate("ReleaseDate").toLocalDate(),
                            podcastId,
                            rs.getInt("EpisodeNumber"),
                            rs.getString("ShowNotes"),
                            JsonUtils.fromJsonToList(rs.getString("Guests"))
                    ));
                }
            }
        }
        return podcasts;
    }

    @Override
    public List<Podcast> findAll() throws SQLException {
        List<Podcast> podcasts = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_PODCASTS);
             ResultSet rs = stmt.executeQuery()) {
            Podcast currentPodcast = null;
            int lastPodcastId = -1;
            while (rs.next()) {
                int podcastId = rs.getInt("CollectionID");
                if (currentPodcast == null || podcastId != lastPodcastId) {
                    if (currentPodcast != null) {
                        podcasts.add(currentPodcast);
                    }
                    currentPodcast = new Podcast(
                            podcastId,
                            rs.getString("Name"),
                            rs.getString("Description"),
                            rs.getInt("OwnerID")
                    );
                    lastPodcastId = podcastId;
                }
                if (rs.getInt("ItemID") != 0) {
                    currentPodcast.getItems().add(new Episode(
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
            if (currentPodcast != null) {
                podcasts.add(currentPodcast);
            }
        }
        return podcasts;
    }

    @Override
    public Optional<Podcast> findById(int id) throws SQLException {
        Podcast podcast = null;
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_PODCAST_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (podcast == null) {
                    podcast = new Podcast(
                            rs.getInt("CollectionID"),
                            rs.getString("Name"),
                            rs.getString("Description"),
                            rs.getInt("OwnerID")
                    );
                }
                if (rs.getInt("ItemID") != 0) {
                    podcast.getItems().add(new Episode(
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
        }
        return Optional.ofNullable(podcast);
    }

    @Override
    public boolean create(Podcast podcast) throws SQLException {
        Connection conn = null;
        PreparedStatement stmtCollection = null;
        PreparedStatement stmtPodcast = null;
        boolean result = false;
        try {
            conn = DatabaseConnector.connect();
            conn.setAutoCommit(false);

            stmtCollection = conn.prepareStatement(INSERT_COLLECTION, Statement.RETURN_GENERATED_KEYS);
            stmtCollection.setInt(1, podcast.getOwnerId());
            stmtCollection.setString(2, podcast.getName());
            int affectedRows = stmtCollection.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            ResultSet generatedKeys = stmtCollection.getGeneratedKeys();
            if (generatedKeys.next()) {
                podcast.setId(generatedKeys.getInt(1));
                stmtPodcast = conn.prepareStatement(INSERT_PODCAST);
                stmtPodcast.setString(1, podcast.getDescription());
                stmtPodcast.executeUpdate();
                conn.commit();
                result = true;
            } else {
                conn.rollback();
            }
        } catch (SQLException ex) {
            System.out.printf("SQL error: %s\n", ex.getMessage());
        } finally {
            if (stmtCollection != null) stmtCollection.close();
            if (stmtPodcast != null) stmtPodcast.close();
            if (conn != null) conn.setAutoCommit(true);
            if (conn != null) conn.close();
        }
        return result;
    }

    @Override
    public boolean update(Podcast podcast) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PODCAST)) {
            stmt.setString(1, podcast.getName());
            stmt.setString(2, podcast.getDescription());
            stmt.setInt(3, podcast.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(DELETE_PODCAST)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
