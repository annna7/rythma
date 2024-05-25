package repositories;

import database.DatabaseConnector;
import models.users.Artist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArtistRepository implements IRepository<Artist> {

    private static final String GET_ALL_ARTISTS = "SELECT * FROM Artist JOIN User ON Artist.ArtistID = User.UserID";
    private static final String GET_ARTIST_BY_ID = "SELECT * FROM Artist JOIN User ON Artist.ArtistID = User.UserID WHERE ArtistID = ?";
    private static final String INSERT_ARTIST = "INSERT INTO User (Username, Password, FirstName, LastName) VALUES (?, ?, ?, ?);";
    private static final String INSERT_ARTIST_BIO = "INSERT INTO Artist (ArtistID, Biography) VALUES (LAST_INSERT_ID(), ?);";
    private static final String UPDATE_ARTIST = "UPDATE User JOIN Artist ON User.UserID = Artist.ArtistID SET Username = ?, Password = ?, FirstName = ?, LastName = ?, Biography = ? WHERE ArtistID = ?";
    private static final String DELETE_ARTIST = "DELETE FROM User WHERE UserID = ?";  // Cascade delete should handle Artist table

    public static Connection getDbConnection() {
        return DatabaseConnector.connect();
    }

    public List<Artist> findAll() throws SQLException {
        List<Artist> artists = new ArrayList<>();
        try (Connection conn = getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_ARTISTS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                artists.add(new Artist(rs.getString("Username"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Password"), rs.getString("Biography")));
            }
        }
        return artists;
    }

    public Optional<Artist> findById(int artistId) throws SQLException {
        try (Connection conn = getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ARTIST_BY_ID)) {
            stmt.setInt(1, artistId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Artist(rs.getString("Username"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Password"), rs.getString("Biography")));
            }
        }
        return Optional.empty();
    }

    public boolean create(Artist artist) throws SQLException {
        try (Connection conn = getDbConnection();
             PreparedStatement stmtUser = conn.prepareStatement(INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtArtist = conn.prepareStatement(INSERT_ARTIST_BIO)) {
            conn.setAutoCommit(false);
            stmtUser.setString(1, artist.getUsername());
            stmtUser.setString(2, artist.getPassword());
            stmtUser.setString(3, artist.getFirstName());
            stmtUser.setString(4, artist.getLastName());
            int affectedRows = stmtUser.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            try (ResultSet generatedKeys = stmtUser.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    stmtArtist.setString(1, artist.getBiography());
                    stmtArtist.executeUpdate();
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
        }
    }

    public boolean update(Artist artist) throws SQLException {
        try (Connection conn = getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_ARTIST)) {
            stmt.setString(1, artist.getUsername());
            stmt.setString(2, artist.getPassword());
            stmt.setString(3, artist.getFirstName());
            stmt.setString(4, artist.getLastName());
            stmt.setString(5, artist.getBiography());
            stmt.setInt(6, artist.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int artistId) throws SQLException {
        try (Connection conn = getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_ARTIST)) {
            stmt.setInt(1, artistId);
            return stmt.executeUpdate() > 0;
        }
    }
}
