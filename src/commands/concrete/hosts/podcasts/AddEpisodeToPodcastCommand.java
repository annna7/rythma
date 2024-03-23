package commands.concrete.hosts.podcasts;

import commands.Command;
import enums.PlayableItemEnum;
import factories.PlayableItemFactory;
import models.audio.items.Episode;
import services.MusicService;
import services.PodcastService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static utils.InputUtils.*;

public class AddEpisodeToPodcastCommand implements Command {
    @Override
    public void execute() {
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
}
