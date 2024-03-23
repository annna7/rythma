package commands.concrete.hosts.podcasts;

import commands.Command;

public class AddPodcastCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Add podcast command executed!");
    }
}
