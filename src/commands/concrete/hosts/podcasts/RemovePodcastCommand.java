package commands.concrete.hosts.podcasts;

import commands.Command;
import services.PodcastService;

import static utils.InputUtils.askForField;

public class RemovePodcastCommand implements Command {
    @Override
    public void execute() {
        int podcastId = Integer.parseInt(askForField("podcast id"));
        PodcastService.getInstance().removePodcast(podcastId);
    }
}
