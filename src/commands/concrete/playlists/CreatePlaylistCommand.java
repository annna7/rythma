package commands.concrete.playlists;

import commands.Command;

public class CreatePlaylistCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Create playlist command executed!");
    }
}
