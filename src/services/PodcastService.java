package services;

import exceptions.NotFoundException;
import models.audio.collections.Podcast;
import models.audio.items.Episode;
import models.users.Host;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        Podcast podcast = getPodcastById(podcastId);
        podcast.addItem(episode);
    }

    private Podcast getPodcastById(int podcastId) {
        return podcasts.stream().filter(p -> p.getId() == podcastId).findFirst().orElseThrow(() -> new NotFoundException("Podcast"));
    }

    private Episode getEpisode(int podcastId, int episodeId) {
        return getPodcastById(podcastId).getItems().stream().filter(e -> e.getId() == episodeId).findFirst().orElseThrow(() -> new NotFoundException("Episode"));
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
        Podcast podcast = getPodcastById(podcastId);
        Episode episode = getEpisode(podcastId, episodeId);
        podcast.removeItem(episode);
    }

    public void removePodcast(int podcastId) {
        Host host = UserService.getInstance().getCurrentHost();
        Podcast podcast = getPodcastById(podcastId);
        host.removePodcast(podcast);
        podcasts.remove(podcast);
    }

    public List<Episode> getAllEpisodes() {
        return podcasts.stream().flatMap(p -> p.getItems().stream()).collect(Collectors.toList());
    }

    public List<Podcast> getAllPodcasts() {
        return podcasts;
    }

    public String getPodcastNameByEpisode(Episode episode) {
        return getPodcastById(episode.getCollectionId()).getName();
    }

    public String getHostNameByPodcast(Podcast podcast) {
        return UserService.getInstance().getUserById(podcast.getOwnerId()).getDisplayName();
    }

    public String getHostNamesByEpisode(Episode episode) {
        return getHostNameByPodcast(getPodcastById(episode.getCollectionId()));
    }
}
