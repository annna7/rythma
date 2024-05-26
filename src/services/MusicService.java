package services;

import exceptions.IllegalOperationException;
import exceptions.NotFoundException;
import models.audio.collections.Album;
import models.audio.items.Song;
import models.users.Artist;
import repositories.audio.items.SongRepository;
import repositories.audio.collections.AlbumRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MusicService {
    private static MusicService instance = null;
    private static final List<Song> allSongs = new ArrayList<>();
    private static final List<Album> albums = new ArrayList<>();

    private final SongRepository songRepository = new SongRepository();
    private final AlbumRepository albumRepository = new AlbumRepository();

    private MusicService() {}

    public static synchronized MusicService getInstance() {
        if (instance == null) {
            instance = new MusicService();
        }
        return instance;
    }

    public Album getAlbumById(int albumId) {
        Optional<Album> albumOptional;

        albumOptional = albums.stream().filter(item -> item.getId() == albumId).findFirst();

        if (albumOptional.isEmpty()) {
            try {
                albumOptional = albumRepository.findById(albumId);
                albumOptional.ifPresent(albums::add);
            } catch (SQLException e) {
                System.out.printf("SQL error: %s", e.getMessage());
            }
        }

        if (albumOptional.isEmpty()) {
            throw new NotFoundException("Album with id " + albumId + " not found");
        }

        return albumOptional.get();
    }

    public Song getSongById(int songId) {
        Optional<Song> songOptional = allSongs.stream().filter(s -> s.getId() == songId).findFirst();
        try {
            if (songOptional.isEmpty()) {
                songOptional = songRepository.findById(songId);
                songOptional.ifPresent(allSongs::add);
            }
        } catch (SQLException e) {
            System.out.printf("SQL error: %s", e.getMessage());
        }
        if (songOptional.isPresent()) {
            return songOptional.get();
        }
        throw new NotFoundException("Song with id " + songId + " not found");
    }

    public List<Song> getAllSongs() {
        try {
            return songRepository.findAll();

        } catch (SQLException e) {
            System.out.printf("SQL error: %s", e.getMessage());
            return List.of();
        }
    }

    public List<Album> getCurrentAlbums() {
        Artist currentArtist = UserService.getInstance().getCurrentArtist();
        return getAllAlbums().stream().filter(album -> album.getOwnerId() == currentArtist.getId()).toList();
    }

    public void addAlbum(Album album) {
        Artist artist = UserService.getInstance().getCurrentArtist();
        artist.addAlbum(album);
        try {
            albumRepository.create(album);
        } catch (SQLException e) {
            System.out.printf("SQL error: %s", e.getMessage());
        }
        albums.add(album);
    }

    public void addSongToAlbum(Song song, int albumId) {
        Album album = getAlbumById(albumId);
        if (album == null) {
            throw new NotFoundException(String.format("Album with id %d not found!", albumId));
        }
        try {
            songRepository.create(song);
        } catch (SQLException e) {
            System.out.printf("SQL error: %s", e.getMessage());
        }
        album.addItem(song);
        allSongs.add(song);
    }


    public void removeAlbum(int albumId) {
        Album album = getAlbumById(albumId);
        Artist artist = UserService.getInstance().getCurrentArtist();
        if (album.getOwnerId() != artist.getId()) {
            throw new IllegalOperationException("You can't remove an album you don't own");
        }
        try {
            // TODO: check this deletes all songs as well
            albumRepository.delete(albumId);
        } catch (SQLException e) {
            System.out.printf("SQL error: %s", e.getMessage());
        }

        for (Song song : album.getItems()) {
            this.removeSongFromAlbum(albumId);
        }
        artist.removeAlbum(album);
        albums.remove(album);
    }

    public void removeSongFromAlbum(int songId) {
        Song song = getSongById(songId);
        Album album = getAlbumById(song.getCollectionId());

        if (album.getOwnerId() != UserService.getInstance().getCurrentUser().getId()) {
            throw new IllegalOperationException("You can't remove a song from an album you don't own");
        }
        try {
            songRepository.delete(songId);
        } catch (SQLException e) {
            System.out.printf("SQL error: %s", e.getMessage());
        }

        album.removeItem(song);
        allSongs.remove(song);
    }


    public List<Album> getAllAlbums() {
        try {
            return albumRepository.findAll();
        } catch (SQLException e) {
            System.out.printf("SQL error: %s", e.getMessage());
            return List.of();
        }
    }

    public String getArtistNameByAlbum(Album album) {
        return UserService.getInstance().getUserById(album.getOwnerId()).getDisplayName();
    }

    public Album getAlbumBySong(Song song) {
        return getAlbumById(song.getCollectionId());
    }

    public String getArtistNameBySong(Song song) {
        return UserService.getInstance().getUserById(getAlbumBySong(song).getOwnerId()).getDisplayName();
    }
}
