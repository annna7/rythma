package commands.concrete.hosts.podcasts;

import commands.Command;
import enums.AudioCollectionEnum;
import factories.AudioCollectionFactory;
import models.audio.collections.Podcast;
import models.users.Host;
import models.users.User;
import services.PodcastService;

import java.sql.SQLException;
import java.util.function.Predicate;

import static utils.InputUtils.askForField;

public class AddPodcastCommand implements Command {
    @Override
    public void execute() throws SQLException {
        String podcastName = askForField("podcast name");
        String podcastDescription = askForField("podcast description");

        Podcast podcast = (Podcast) AudioCollectionFactory.createAudioCollection(AudioCollectionEnum.PODCAST, podcastName, podcastDescription);
        PodcastService.getInstance().addPodcast(podcast);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return user -> user instanceof Host;
    }

    @Override
    public String getCommandName() {
        return "Add podcast";
    }

    @Override
    public String getCommandDescription() {
        return "Create a new podcast, which can later be populated with episodes";
    }
}
