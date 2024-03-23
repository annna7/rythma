package commands.concrete.albums;

import commands.Command;

public class RemoveSongFromAlbumCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Remove song from album command executed!");
    }
}
