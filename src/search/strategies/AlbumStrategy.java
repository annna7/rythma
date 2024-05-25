package search.strategies;

import models.audio.collections.Album;
import search.SearchStrategy;
import services.MusicService;
import utils.SearchUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AlbumStrategy implements SearchStrategy<Album> {

    @Override
    public List<Album> search(Map<String, String> query) throws NoSuchFieldException, IllegalAccessException, SQLException {
        List<Album> albums = MusicService.getInstance().getAllAlbums();
        if (query.containsKey("name")) {
            albums = SearchUtils.searchByAttribute(albums, "name", query.get("name"));
        }
        if (query.containsKey("artist")) {
            albums = SearchUtils.searchByAttributeUsingFunction(albums, album ->
                    MusicService.getInstance().getArtistNameByAlbum(album), query.get("artist"));
        }
        if (query.containsKey("label")) {
            albums = SearchUtils.searchByAttribute(albums, "label", query.get("label"));
        }
        return albums;
    }
}
