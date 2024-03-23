package commands.concrete.hosts.podcasts;

import commands.Command;

public class RemovePodcastCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Remove podcast command executed!");
    }
}
