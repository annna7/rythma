package commands.concrete.hosts.podcasts;

import commands.Command;
import enums.AudioCollectionEnum;
import factories.AudioCollectionFactory;
import models.audio.collections.Podcast;
import services.PodcastService;

import static utils.InputUtils.askForField;

public class AddPodcastCommand implements Command {
    @Override
    public void execute() {
        String podcastName = askForField("podcast name");
        String podcastDescription = askForField("podcast description");

        Podcast podcast = (Podcast) AudioCollectionFactory.createAudioCollection(AudioCollectionEnum.PODCAST, podcastName, podcastDescription);
        PodcastService.getInstance().addPodcast(podcast);
    }
}
