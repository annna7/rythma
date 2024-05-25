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
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_USERS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(rs.getString("username"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("password")));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
        return users;
    }

    public Optional<User> findById(int userId) {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_USER_BY_ID)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new User(rs.getString("username"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("password")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<User> findByUsername(String username) {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_USER_BY_USERNAME)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new User(rs.getString("username"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("password")));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean create(User user) {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(User user) {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setInt(5, user.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
}
