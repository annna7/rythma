package repositories;

import database.DatabaseConnector;
import models.users.Host;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HostRepository implements IRepository<Host> {

    private static final String GET_ALL_HOSTS = "SELECT * FROM Host JOIN User ON Host.HostID = User.UserID";
    private static final String GET_HOST_BY_ID = "SELECT * FROM Host JOIN User ON Host.HostID = User.UserID WHERE HostID = ?";
    private static final String INSERT_HOST = "INSERT INTO User (Username, Password, FirstName, LastName) VALUES (?, ?, ?, ?);";
    private static final String INSERT_HOST_AFFILIATION = "INSERT INTO Host (HostID, Affiliation) VALUES (LAST_INSERT_ID(), ?);";
    private static final String UPDATE_HOST = "UPDATE User JOIN Host ON User.UserID = Host.HostID SET Username = ?, Password = ?, FirstName = ?, LastName = ?, Affiliation = ? WHERE HostID = ?";
    private static final String DELETE_HOST = "DELETE FROM User WHERE UserID = ?";  // Cascade delete should handle Host table

    public static Connection getDbConnection() throws SQLException {
        return DatabaseConnector.connect();
    }

    public List<Host> findAll() throws SQLException {
        List<Host> hosts = new ArrayList<>();
        try (Connection conn = getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_HOSTS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                hosts.add(new Host(rs.getString("Username"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Password"), rs.getString("Affiliation")));
            }
        }
        return hosts;
    }

    public Optional<Host> findById(int hostId) throws SQLException {
        try (Connection conn = getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_HOST_BY_ID)) {
            stmt.setInt(1, hostId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Host(rs.getString("Username"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Password"), rs.getString("Affiliation")));
            }
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
