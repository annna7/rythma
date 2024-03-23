package commands.concrete.hosts.podcasts;

import commands.Command;

public class RemoveEpisodeFromPodcastCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Remove episode from podcast command executed!");
    }
}
