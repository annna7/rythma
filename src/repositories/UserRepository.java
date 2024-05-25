package repositories;

import database.DatabaseConnector;
import models.users.User;

import java.util.Optional;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IRepository<User> {
    private static final String GET_ALL_USERS = "SELECT * FROM User";
    private static final String GET_USER_BY_ID = "SELECT * FROM User WHERE id = ?";
    private static final String GET_USER_BY_USERNAME = "SELECT * FROM User WHERE username = ?";
    private static final String INSERT_USER = "INSERT INTO User (username, password, firstName, lastName) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE User SET username = ?, password = ?, firstName = ?, lastName = ? WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM User WHERE id = ?";

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_USERS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(rs.getString("username"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("password")));
            }
            return users;
        }
    }

    public Optional<User> findById(int userId) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_USER_BY_ID)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new User(rs.getString("username"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("password")));
            }
            return Optional.empty();
        }
    }

    public Optional<User> findByUsername(String username) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_USER_BY_USERNAME)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new User(rs.getString("username"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("password")));
            }
            return Optional.empty();
        }
    }

    @Override
    public boolean create(User user) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            return stmt.executeUpdate() > 0;
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
