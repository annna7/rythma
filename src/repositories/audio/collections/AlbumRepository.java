package repositories.audio.collections;

import database.DatabaseConnector;
import models.audio.collections.Album;
import repositories.IRepository;
import utils.JsonUtils;
import models.audio.items.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlbumRepository implements IRepository<Album> {
    private static final String INSERT_COLLECTION = "INSERT INTO AudioCollection (OwnerID, Name) VALUES (?, ?);";
    private static final String INSERT_ALBUM = "INSERT INTO Album (AlbumID, Label) VALUES (LAST_INSERT_ID(), ?);";
//    private static final String GET_ALBUM_BY_ID = "SELECT ac.CollectionID, ac.Name, ac.OwnerID, a.Label FROM Album a JOIN AudioCollection ac ON a.AlbumID = ac.CollectionID WHERE a.AlbumID = ?";
//    private static final String GET_ALL_ALBUMS = "SELECT ac.CollectionID, ac.Name, ac.OwnerID, a.Label FROM Album a JOIN AudioCollection ac ON a.AlbumID = ac.CollectionID;";
    private static final String UPDATE_ALBUM = "UPDATE AudioCollection ac JOIN Album a ON ac.CollectionID = a.AlbumID SET ac.Name = ?, a.Label = ? WHERE ac.CollectionID = ?;";
    private static final String DELETE_ALBUM = "DELETE FROM AudioCollection WHERE CollectionID = ?;";
    private static final String GET_ALL_ALBUMS = "SELECT ac.CollectionID, ac.Name, ac.OwnerID, al.Label, " +
            "pi.ItemID, pi.Title, pi.Length, pi.ReleaseDate, s.Genres " +
            "FROM AudioCollection ac " +
            "JOIN Album al ON ac.CollectionID = al.AlbumID " +
            "LEFT JOIN PlayableItem pi ON ac.CollectionID = pi.CollectionID " +
            "LEFT JOIN Song s ON pi.ItemID = s.SongID";

    private static final String GET_ALBUM_BY_ID = "SELECT ac.CollectionID, ac.Name, ac.OwnerID, al.Label, " +
            "pi.ItemID, pi.Title, pi.Length, pi.ReleaseDate, s.Genres " +
            "FROM AudioCollection ac " +
            "JOIN Album al ON ac.CollectionID = al.AlbumID " +
            "LEFT JOIN PlayableItem pi ON ac.CollectionID = pi.CollectionID " +
            "LEFT JOIN Song s ON pi.ItemID = s.SongID " +
            "WHERE ac.CollectionID = ?";

    private static final String GET_ALBUMS_BY_ARTIST_ID = "SELECT ac.CollectionID, ac.Name, ac.OwnerID, al.Label, " +
            "pi.ItemID, pi.Title, pi.Length, pi.ReleaseDate, s.Genres " +
            "FROM AudioCollection ac " +
            "JOIN Album al ON ac.CollectionID = al.AlbumID " +
            "LEFT JOIN PlayableItem pi ON ac.CollectionID = pi.CollectionID " +
            "LEFT JOIN Song s ON pi.ItemID = s.SongID " +
            "WHERE ac.OwnerID = ?";

    public List<Album> findAlbumsByArtistId(int artistId) throws SQLException {
        List<Album> albums = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_ALBUMS_BY_ARTIST_ID)) {
            stmt.setInt(1, artistId);
            ResultSet rs = stmt.executeQuery();
            Album currentAlbum = null;
            while (rs.next()) {
                if (currentAlbum == null || currentAlbum.getId() != rs.getInt("CollectionID")) {
                    currentAlbum = new Album(
                            rs.getInt("CollectionID"), rs.getString("Name"), rs.getString("Label"), artistId, new ArrayList<>()
                    );
                    albums.add(currentAlbum);
                }
                if (rs.getInt("ItemID") != 0) {
                    currentAlbum.getItems().add(new Song(
                            rs.getInt("ItemID"), rs.getString("Title"), rs.getInt("Length"),
                            rs.getInt("CollectionID"), rs.getDate("ReleaseDate").toLocalDate(),
                            JsonUtils.fromJsonToList(rs.getString("Genres"))
                    ));
                }
            }
        }
        return albums;
    }

    @Override
    public List<Album> findAll() throws SQLException {
        List<Album> albums = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_ALBUMS);
             ResultSet rs = stmt.executeQuery()) {
            Album currentAlbum = null;
            int lastAlbumId = -1;
            while (rs.next()) {
                int albumId = rs.getInt("CollectionID");
                if (currentAlbum == null || albumId != lastAlbumId) {
                    if (currentAlbum != null) {
                        albums.add(currentAlbum);
                    }
                    currentAlbum = new Album(
                            albumId,
                            rs.getString("Name"),
                            rs.getString("Label"),
                            rs.getInt("OwnerID")
                    );
                    lastAlbumId = albumId;
                }
                if (rs.getInt("ItemID") != 0) {
                    currentAlbum.getItems().add(new Song(
                            rs.getInt("ItemID"),
                            rs.getString("Title"),
                            rs.getInt("Length"),
                            rs.getInt("CollectionID"),
                            rs.getDate("ReleaseDate").toLocalDate(),
                            JsonUtils.fromJsonToList(rs.getString("Genres"))
                    ));
                }
            }
            if (currentAlbum != null) {
                albums.add(currentAlbum);
            }
        }
        return albums;
    }

    @Override
    public Optional<Album> findById(int id) throws SQLException {
        Album album = null;
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_ALBUM_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (album == null) {
                    album = new Album(
                            rs.getInt("CollectionID"),
                            rs.getString("Name"),
                            rs.getString("Label"),
                            rs.getInt("OwnerID")
                    );
                }
                if (rs.getInt("ItemID") != 0) {
                    album.getItems().add(new Song(
                            rs.getInt("ItemID"),
                            rs.getString("Title"),
                            rs.getInt("Length"),
                            rs.getInt("CollectionID"),
                            rs.getDate("ReleaseDate").toLocalDate(),
                            JsonUtils.fromJsonToList(rs.getString("Genres"))
                    ));
                }
            }
        }
        return Optional.ofNullable(album);
    }
    @Override
    public boolean create(Album album) throws SQLException {
        Connection conn = null;
        PreparedStatement stmtCollection = null;
        PreparedStatement stmtAlbum = null;
        boolean result = false;
        try {
            conn = DatabaseConnector.connect();
            conn.setAutoCommit(false);

            stmtCollection = conn.prepareStatement(INSERT_COLLECTION, Statement.RETURN_GENERATED_KEYS);
            stmtCollection.setInt(1, album.getOwnerId());
            stmtCollection.setString(2, album.getName());
            int affectedRows = stmtCollection.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            ResultSet generatedKeys = stmtCollection.getGeneratedKeys();
            if (generatedKeys.next()) {
                album.setId(generatedKeys.getInt(1));
                stmtAlbum = conn.prepareStatement(INSERT_ALBUM);
                stmtAlbum.setString(1, album.getLabel());
                stmtAlbum.executeUpdate();
                conn.commit();
                result = true;
            } else {
                conn.rollback();
            }
        } catch (SQLException ex) {
            System.out.printf("SQL error: %s\n", ex.getMessage());
        } finally {
            if (stmtCollection != null) stmtCollection.close();
            if (stmtAlbum != null) stmtAlbum.close();
            if (conn != null) conn.setAutoCommit(true);
            if (conn != null) conn.close();
        }
        return result;
    }

    @Override
    public boolean update(Album album) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_ALBUM)) {
            stmt.setString(1, album.getName());
            stmt.setString(2, album.getLabel());
            stmt.setInt(3, album.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(DELETE_ALBUM)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
