package commands.concrete.hosts.podcasts;

import commands.Command;
import services.PodcastService;

import static utils.OutputUtils.showCollectionMessage;

public class ViewPodcastsCommand implements Command {
    @Override
    public void execute() {
        showCollectionMessage("podcasts", PodcastService.getInstance().getPodcastsForCurrentUser());
    }
}
