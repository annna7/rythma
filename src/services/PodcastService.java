package services;

import models.audio.collections.Podcast;
import models.audio.items.Episode;
import models.users.Host;
import models.users.User;

import java.util.ArrayList;

import static utils.RoleValidator.validateHost;

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
        if (podcast == null) {
            throw new IllegalArgumentException("Podcast not found");
        }
        podcast.addItem(episode);
    }

    private Podcast getPodcast(int podcastId) {
        return podcasts.stream().filter(p -> p.getId() == podcastId).findFirst().orElse(null);
    }

    public ArrayList<Podcast> getPodcastsForCurrentUser() {
        User currentUser = UserService.getInstance().getCurrentUser();
        validateHost(currentUser);
        return (ArrayList<Podcast>) ((Host) currentUser).getPodcasts();
    }

    public void addPodcast(Podcast podcast) {
        podcasts.add(podcast);
        User currentUser = UserService.getInstance().getCurrentUser();
        validateHost(currentUser);
        ((Host) currentUser).addPodcast(podcast);
    }

    public void removeEpisodeFromPodcast(int podcastId, int episodeId) {
        Podcast podcast = getPodcast(podcastId);
        if (podcast == null) {
            throw new IllegalArgumentException("Podcast not found");
        }
        Episode episode = podcast.getItems().stream().filter(e -> e.getId() == episodeId).findFirst().orElse(null);
        if (episode == null) {
            throw new IllegalArgumentException("Episode not found");
        }
        podcast.removeItem(episode);
    }

    public void removePodcast(int podcastId) {
        Podcast podcast = getPodcast(podcastId);
        if (podcast == null) {
            throw new IllegalArgumentException("Podcast not found");
        }
        podcasts.remove(podcast);
        User currentUser = UserService.getInstance().getCurrentUser();
        validateHost(currentUser);
        ((Host) currentUser).removePodcast(podcast);
    }
}
