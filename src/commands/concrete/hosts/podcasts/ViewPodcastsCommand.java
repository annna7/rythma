package commands.concrete.hosts.podcasts;

import commands.Command;
import services.MusicService;

import static utils.OutputUtils.showCollectionMessage;

public class ViewPodcastsCommand implements Command {
    @Override
    public void execute() {
        showCollectionMessage("podcasts", MusicService.getInstance().getCurrentPodcasts());
    }
}
