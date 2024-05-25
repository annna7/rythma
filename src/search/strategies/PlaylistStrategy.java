package search.strategies;

import models.audio.collections.Playlist;
import search.SearchStrategy;
import services.PlaylistService;
import services.UserService;
import utils.SearchUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PlaylistStrategy implements SearchStrategy<Playlist> {
    @Override
    public List<Playlist> search(Map<String, String> query) throws NoSuchFieldException, IllegalAccessException, SQLException {
        List<Playlist> playlists = PlaylistService.getInstance().getAllPublicPlaylists();
        if (query.containsKey("name")) {
            playlists = SearchUtils.searchByAttribute(playlists, "name", query.get("name"));
        }
        if (query.containsKey("description")) {
            playlists = SearchUtils.searchByAttribute(playlists, "description", query.get("description"));
        }
        if (query.containsKey("owner")) {
            playlists = SearchUtils.searchByAttributeUsingFunction(playlists, playlist ->
                    UserService.getInstance().getUserById(playlist.getOwnerId()).getUsername(), query.get("owner"));
        }
        return playlists;
    }
}