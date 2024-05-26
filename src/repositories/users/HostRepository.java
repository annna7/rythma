package repositories.users;

import database.DatabaseConnector;
import models.users.Host;
import models.audio.collections.Podcast;
import models.audio.items.Episode;

import repositories.IRepository;
import repositories.audio.collections.PlaylistRepository;
import repositories.audio.collections.PodcastRepository;
import utils.JsonUtils;

import java.sql.*;
import java.util.*;

public class HostRepository implements IRepository<Host> {

    private static final String GET_ALL_HOSTS = "SELECT * FROM Host JOIN User ON Host.HostID = User.UserID";
    private static final String GET_HOST_BY_ID = "SELECT * FROM Host JOIN User ON Host.HostID = User.UserID WHERE HostID = ?";
    private static final String INSERT_HOST = "INSERT INTO User (Username, Password, FirstName, LastName) VALUES (?, ?, ?, ?);";
    private static final String INSERT_HOST_AFFILIATION = "INSERT INTO Host (HostID, Affiliation) VALUES (LAST_INSERT_ID(), ?);";
    private static final String UPDATE_HOST = "UPDATE User JOIN Host ON User.UserID = Host.HostID SET Username = ?, Password = ?, FirstName = ?, LastName = ?, Affiliation = ? WHERE HostID = ?";
    private static final String DELETE_HOST = "DELETE FROM User WHERE UserID = ?";  // Cascade delete should handle Host table

    private final PodcastRepository podcastRepository = new PodcastRepository();
    private final PlaylistRepository playlistRepository = new PlaylistRepository();

    public static Connection getDbConnection() {
        return DatabaseConnector.connect();
    }


    public List<Host> findAll() throws SQLException {
        List<Host> hosts = new ArrayList<>();
        Map<Integer, Host> hostMap = new HashMap<>();

        try (Connection conn = getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_HOSTS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int hostId = rs.getInt("HostID");
                if (!hostMap.containsKey(hostId)) {
                    Host host = new Host(rs.getString("Username"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Password"), rs.getString("Affiliation"));
                    host.setId(hostId);
                    hostMap.put(hostId, host);
                    hosts.add(host);
                }
            }
        }

        for (Host host : hosts) {
            host.getPodcasts().addAll(podcastRepository.findPodcastsByHostId(host.getId()));
            host.getPlaylists().addAll(playlistRepository.findAllPlaylistsByUserId(host.getId()));
        }

        return hosts;
    }

    public Optional<Host> findById(int hostId) throws SQLException {
        Host host = null;
        try (Connection conn = getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_HOST_BY_ID)) {
            stmt.setInt(1, hostId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    host = new Host(rs.getInt("UserID"), rs.getString("Username"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Password"), rs.getString("Affiliation"));
                    host.setId(rs.getInt("UserID"));
                }
            }
        }

        if (host != null) {
            host.getPodcasts().addAll(podcastRepository.findPodcastsByHostId(host.getId()));
            host.getPlaylists().addAll(playlistRepository.findAllPlaylistsByUserId(host.getId()));
            return Optional.of(host);
        }
        return Optional.empty();
    }

    public boolean create(Host host) throws SQLException {
        try (Connection conn = getDbConnection();
             PreparedStatement stmtUser = conn.prepareStatement(INSERT_HOST, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtHost = conn.prepareStatement(INSERT_HOST_AFFILIATION)) {
            conn.setAutoCommit(false);
            stmtUser.setString(1, host.getUsername());
            stmtUser.setString(2, host.getPassword());
            stmtUser.setString(3, host.getFirstName());
            stmtUser.setString(4, host.getLastName());
            int affectedRows = stmtUser.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            try (ResultSet generatedKeys = stmtUser.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    host.setId(generatedKeys.getInt(1));
                    stmtHost.setString(1, host.getAffiliation());
                    stmtHost.executeUpdate();
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
        }
    }

    public boolean update(Host host) throws SQLException {
        try (Connection conn = getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_HOST)) {
            stmt.setString(1, host.getUsername());
            stmt.setString(2, host.getPassword());
            stmt.setString(3, host.getFirstName());
            stmt.setString(4, host.getLastName());
            stmt.setString(5, host.getAffiliation());
            stmt.setInt(6, host.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int hostId) throws SQLException {
        try (Connection conn = getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_HOST)) {
            stmt.setInt(1, hostId);
            return stmt.executeUpdate() > 0;
        }
    }
}
