package commands.concrete;

import commands.Command;

public class CreatePlaylistCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Create playlist command executed!");
    }
}
