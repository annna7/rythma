package commands.concrete.hosts.podcasts;

import commands.Command;
import enums.PlayableItemEnum;
import factories.PlayableItemFactory;
import models.audio.items.Episode;
import models.users.Host;
import models.users.User;
import services.PodcastService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import static utils.InputUtils.*;

public class AddEpisodeToPodcastCommand implements Command {
    @Override
    public void execute() throws SQLException {
        String episodeName = askForField("episode name");
        String episodeDescription = askForField("episode description");
        int episodeDuration = Integer.parseInt(askForField("episode duration"));
        int episodeNumber = Integer.parseInt(askForField("episode number"));
        int podcastId = Integer.parseInt(askForField("podcast ID"));
        LocalDate release = askForReleaseDate();

        ArrayList<String> episodeGuests = askForEpisodeGuests();

        String[] args = {episodeDescription, String.valueOf(episodeNumber), Arrays.toString(episodeGuests.toArray(new String[0]))};

        Episode episode = (Episode) PlayableItemFactory.createPlayableItem(PlayableItemEnum.EPISODE, episodeName, episodeDuration,
                                                                           release, podcastId, args);

        PodcastService.getInstance().addEpisodeToPodcast(episode, podcastId);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return user -> user instanceof Host;
    }

    @Override
    public String getCommandName() {
        return "Add episode to podcast";
    }

    @Override
    public String getCommandDescription() {
        return "Add a new episode to a podcast, by providing the episode name, description, duration, number, release date, and guests";
    }
}
