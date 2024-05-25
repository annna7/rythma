package search.strategies;

import models.users.Artist;
import search.SearchStrategy;
import services.UserService;
import utils.SearchUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ArtistStrategy implements SearchStrategy<Artist> {
    @Override
    public List<Artist> search(Map<String, String> query) throws NoSuchFieldException, IllegalAccessException, SQLException {
        List<Artist> artists = UserService.getInstance().getAllArtists();
        if (query.containsKey("name")) {
            artists = SearchUtils.searchByAttribute(artists, "displayName", query.get("name"));
        }
        if (query.containsKey("biography")) {
            artists = SearchUtils.searchByAttribute(artists, "biography", query.get("biography"));
        }
        return artists;
    }
}
