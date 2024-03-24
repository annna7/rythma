package search.strategies;

import models.audio.items.Song;
import search.SearchStrategy;
import services.MusicService;
import utils.SearchUtils;

import java.util.List;
import java.util.Map;


public class SongStrategy implements SearchStrategy<Song> {
    @Override
    public List<Song> search(Map<String, String> query) throws NoSuchFieldException, IllegalAccessException{
        List<Song> songs = MusicService.getInstance().getAllSongs();
        if (query.containsKey("title")) {
            songs = SearchUtils.searchByAttribute(songs, "title", query.get("title"));
        }
        if (query.containsKey("artist")) {
            songs = SearchUtils.searchByAttributeUsingFunction(songs, song ->
                    MusicService.getInstance().getArtistNameBySong(song), query.get("artist"));
        }
        if (query.containsKey("genre")) {
            songs = SearchUtils.searchInSetAttribute(songs, "genres", query.get("genre"));
        }
        if (query.containsKey("album")) {
            songs = SearchUtils.searchByAttributeUsingFunction(songs, song ->
                    MusicService.getInstance().getAlbumBySong(song).getName(), query.get("album"));
        }
        return songs;
    }
}
