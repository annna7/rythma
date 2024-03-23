package commands.concrete.artists;

import commands.Command;
import services.MusicService;

import static utils.InputUtils.askForField;

public class RemoveSongFromAlbumCommand implements Command {
    @Override
    public void execute() {
        int songId = Integer.parseInt(askForField("the song ID"));

        try {
            MusicService.getInstance().removeSongFromAlbum(songId);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
