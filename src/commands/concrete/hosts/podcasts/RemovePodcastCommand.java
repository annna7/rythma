package commands.concrete.hosts.podcasts;

import commands.Command;
import models.users.Host;
import models.users.User;
import services.PodcastService;

import java.util.function.Predicate;

import static utils.InputUtils.askForField;

public class RemovePodcastCommand implements Command {
    @Override
    public void execute() {
        int podcastId = Integer.parseInt(askForField("podcast id"));
        PodcastService.getInstance().removePodcast(podcastId);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return user -> user instanceof Host;
    }

    @Override
    public String getCommandName() {
        return "Remove podcast";
    }

    @Override
    public String getCommandDescription() {
        return "Remove one of your podcasts, by providing the podcast ID";
    }
}
