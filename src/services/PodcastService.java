package services;

import exceptions.NotFoundException;
import models.audio.collections.Podcast;
import models.audio.items.Episode;
import models.users.Host;

import java.util.ArrayList;
import java.util.List;

public class PodcastService {
    private static PodcastService instance = null;
    private static final ArrayList<Podcast> podcasts = new ArrayList<>();

    private PodcastService() {
    }

    public static synchronized PodcastService getInstance() {
        if (instance == null) {
            instance = new PodcastService();
        }
        return instance;
    }

    public void addEpisodeToPodcast(Episode episode, int podcastId) {
        Podcast podcast = getPodcast(podcastId);
        podcast.addItem(episode);
    }

    private Podcast getPodcast(int podcastId) {
        return podcasts.stream().filter(p -> p.getId() == podcastId).findFirst().orElseThrow(() -> new NotFoundException("Podcast"));
    }

    private Episode getEpisode(int episodeId) {
        return podcasts.stream().flatMap(p -> p.getItems().stream()).filter(e -> e.getId() == episodeId).findFirst().orElseThrow(() -> new NotFoundException("Episode"));
    }

    private Episode getEpisode(int podcastId, int episodeId) {
        return getPodcast(podcastId).getItems().stream().filter(e -> e.getId() == episodeId).findFirst().orElseThrow(() -> new NotFoundException("Episode"));
    }
    public List<Podcast> getPodcastsForCurrentUser() {
        Host host = UserService.getInstance().getCurrentHost();
        return host.getPodcasts();
    }

    public void addPodcast(Podcast podcast) {
        Host host = UserService.getInstance().getCurrentHost();
        host.addPodcast(podcast);
        podcasts.add(podcast);
    }

    public void removeEpisodeFromPodcast(int podcastId, int episodeId) {
        Podcast podcast = getPodcast(podcastId);
        Episode episode = getEpisode(podcastId, episodeId);
        podcast.removeItem(episode);
    }

    public void removePodcast(int podcastId) {
        Host host = UserService.getInstance().getCurrentHost();
        Podcast podcast = getPodcast(podcastId);
        host.removePodcast(podcast);
        podcasts.remove(podcast);
    }
}
