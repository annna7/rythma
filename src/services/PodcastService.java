package services;

import exceptions.NotFoundException;
import models.audio.collections.Album;
import models.audio.collections.Podcast;
import models.audio.items.Episode;
import models.users.Host;
import repositories.audio.collections.PodcastRepository;
import repositories.audio.items.EpisodeRepository;
import repositories.audio.items.SongRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PodcastService {
    private static PodcastService instance = null;
    private static final ArrayList<Podcast> podcasts = new ArrayList<>();

    private final PodcastRepository podcastRepository = new PodcastRepository();
    private final EpisodeRepository episodeRepository = new EpisodeRepository();

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
        Host host = UserService.getInstance().getHostById(podcast.getOwnerId());
        try {
            episodeRepository.create(episode);
            podcast.addItem(episode);
            Optional<Podcast> podcastRef = host.getPodcasts().stream().filter(item -> item.getId() == podcastId).findFirst();
            if (podcastRef.isEmpty()) {
                throw new NotFoundException("Podcast with id " + podcastId + " not found");
            }
            podcastRef.get().addItem(episode);
        } catch (SQLException e) {
            System.out.printf("SQL error: %s", e.getMessage());
        }
    }

    public Podcast getPodcastById(int podcastId) {
        Optional<Podcast> podcastOptional;

        podcastOptional = podcasts.stream().filter(item -> item.getId() == podcastId).findFirst();

        if (podcastOptional.isEmpty()) {
            try {
                podcastOptional = podcastRepository.findById(podcastId);
                podcastOptional.ifPresent(podcasts::add);
            } catch (SQLException e) {
                System.out.printf("SQL error: %s", e.getMessage());
            }
        }

        if (podcastOptional.isEmpty()) {
            throw new NotFoundException("Podcast with id " + podcastId + " not found");
        }

        return podcastOptional.get();
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
        try {
            podcastRepository.create(podcast);
        } catch (SQLException e) {
            System.out.printf("SQL error: %s", e.getMessage());
        }
        host.addPodcast(podcast);
        podcasts.add(podcast);
    }

    public void removeEpisodeFromPodcast(int podcastId, int episodeId) {
        Podcast podcast = getPodcastById(podcastId);
        Host host = UserService.getInstance().getHostById(podcast.getOwnerId());
        Episode episode = getEpisode(podcastId, episodeId);
        try {
            episodeRepository.delete(episodeId);
            podcast.removeItem(episode);
            Optional<Podcast> podcastRef = host.getPodcasts().stream().filter(item -> item.getId() == podcastId).findFirst();
            if (podcastRef.isEmpty()) {
                throw new NotFoundException("Podcast with id " + podcastId + " not found");
            }
            podcastRef.get().removeItem(episode);
        } catch (SQLException e) {
            System.out.printf("SQL error: %s", e.getMessage());
        }
    }

    public void removePodcast(int podcastId) {
        Host host = UserService.getInstance().getCurrentHost();
        Podcast podcast = getPodcastById(podcastId);
        try {
            if (podcastRepository.delete(podcastId)) {
                podcasts.remove(podcast);
                host.removePodcast(podcast);
            }
        } catch (SQLException e) {
            System.out.printf("SQL error: %s", e.getMessage());
        }
    }

    public List<Episode> getAllEpisodes() {
        return getAllPodcasts().stream().flatMap(p -> p.getItems().stream()).collect(Collectors.toList());
    }

    public List<Podcast> getAllPodcasts() {
        try {
            return podcastRepository.findAll();
        } catch (SQLException e) {
            System.out.printf("SQL error: %s", e.getMessage());
            return List.of();
        }
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
