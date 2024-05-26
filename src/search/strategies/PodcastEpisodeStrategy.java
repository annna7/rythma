package search.strategies;

import models.audio.items.Episode;
import search.SearchStrategy;
import services.PodcastService;
import utils.SearchUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PodcastEpisodeStrategy implements SearchStrategy<Episode> {
    @Override
    public List<Episode> search(Map<String, String> query) throws NoSuchFieldException, IllegalAccessException, SQLException {
        List<Episode> episodes = PodcastService.getInstance().getAllEpisodes();
        if (query.containsKey("title")) {
            episodes = SearchUtils.searchByAttribute(episodes, "title", query.get("title"));
        }
        if (query.containsKey("podcast")) {
            episodes = SearchUtils.searchByAttributeUsingFunction(episodes, episode ->
                    PodcastService.getInstance().getPodcastNameByEpisode(episode), query.get("podcast"));
        }
        if (query.containsKey("number")) {
            episodes = SearchUtils.searchByAttributeEquality(episodes, "episodeNumber", Integer.parseInt(query.get("number")));
        }
        if (query.containsKey("host")) {
            episodes = SearchUtils.searchByAttributeUsingFunction(episodes, episode ->
                    PodcastService.getInstance().getHostNamesByEpisode(episode), query.get("host"));
        }
        if (query.containsKey("guest")) {
            episodes = SearchUtils.searchInSetAttribute(episodes, "guests", query.get("guest"));
        }
        return episodes;
    }
}
