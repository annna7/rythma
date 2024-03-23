package commands.concrete.hosts.podcasts;

import commands.Command;
import models.users.Host;
import models.users.User;
import services.PodcastService;

import java.util.function.Predicate;

import static utils.InputUtils.askForField;

public class RemoveEpisodeFromPodcastCommand implements Command {
    @Override
    public void execute() {
        int podcastId = Integer.parseInt(askForField("podcast id"));
        // TODO: should validate podcast exists here
        int episodeId = Integer.parseInt(askForField("episode id"));

        PodcastService.getInstance().removeEpisodeFromPodcast(podcastId, episodeId);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return user -> user instanceof Host;
    }

    @Override
    public String getCommandName() {
        return "Remove episode from podcast";
    }

    @Override
    public String getCommandDescription() {
        return "Remove an episode from a podcast, by providing the podcast ID and the episode ID";
    }
}
