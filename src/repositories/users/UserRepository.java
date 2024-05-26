package repositories.users;

import database.DatabaseConnector;
import models.users.User;
import repositories.IRepository;
import repositories.audio.collections.PlaylistRepository;

import java.util.Optional;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IRepository<User> {
    private static final String GET_ALL_USERS = "SELECT * FROM User";
    private static final String GET_USER_BY_ID = "SELECT * FROM User WHERE UserID = ?";
    private static final String GET_USER_BY_USERNAME = "SELECT * FROM User WHERE username = ?";
    private static final String INSERT_USER = "INSERT INTO User (username, password, firstName, lastName) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE User SET username = ?, password = ?, firstName = ?, lastName = ? WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM User WHERE id = ?";

    private static final PlaylistRepository playlistRepository = new PlaylistRepository();

    public void addPlaylistsToUser(User user) throws SQLException {
        user.getPlaylists().addAll(playlistRepository.findAllPlaylistsByUserId(user.getId()));
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_USERS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User(rs.getInt("UserID"), rs.getString("username"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("password"));
                addPlaylistsToUser(user);
                users.add(user);
            }
            return users;
        }
    }

    public Optional<User> findById(int userId) throws SQLException {
        User user = null;
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_USER_BY_ID)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(rs.getInt("UserID"), rs.getString("username"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("password"));
                }
            }
        }
        if (user != null) {
            addPlaylistsToUser(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public Optional<User> findByUsername(String username) throws SQLException {
        User user = null;
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_USER_BY_USERNAME)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(rs.getInt("UserID"), rs.getString("username"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("password"));
                }
            }
        }

        if (user != null) {
            addPlaylistsToUser(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }


    public boolean create(User user) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));  // Set the ID back on the user object
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
    @Override
    public boolean update(User user) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setInt(5, user.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
