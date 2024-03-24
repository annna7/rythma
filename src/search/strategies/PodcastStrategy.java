package search.strategies;

import models.audio.collections.Podcast;
import search.SearchStrategy;
import services.PodcastService;
import utils.SearchUtils;

import java.util.List;
import java.util.Map;

public class PodcastStrategy implements SearchStrategy<Podcast> {
    @Override
    public List<Podcast> search(Map<String, String> query) throws NoSuchFieldException, IllegalAccessException {
        List<Podcast> podcasts = PodcastService.getInstance().getAllPodcasts();
        if (query.containsKey("title")) {
            podcasts = SearchUtils.searchByAttribute(podcasts, "name", query.get("title"));
        }
        if (query.containsKey("host")) {
            podcasts = SearchUtils.searchByAttributeUsingFunction(podcasts, podcast ->
                    PodcastService.getInstance().getHostNameByPodcast(podcast), query.get("host"));
        }
        return podcasts;
    }
}
