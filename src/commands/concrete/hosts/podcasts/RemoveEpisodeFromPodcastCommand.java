package commands.concrete.hosts.podcasts;

import commands.Command;
import models.audio.collections.Podcast;
import services.PodcastService;

import static utils.InputUtils.askForField;

public class RemoveEpisodeFromPodcastCommand implements Command {
    @Override
    public void execute() {
        int podcastId = Integer.parseInt(askForField("podcast id"));
        // TODO: should validate podcast exists here
        int episodeId = Integer.parseInt(askForField("episode id"));

        PodcastService.getInstance().removeEpisodeFromPodcast(podcastId, episodeId);
    }
}
