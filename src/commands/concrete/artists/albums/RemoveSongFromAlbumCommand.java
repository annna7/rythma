package commands.concrete.artists.albums;

import commands.Command;
import services.MusicService;

import static utils.InputUtils.askForField;

public class RemoveSongFromAlbumCommand implements Command {
    @Override
    public void execute() {
        int songId = Integer.parseInt(askForField("the song ID"));

        MusicService.getInstance().removeSongFromAlbum(songId);
    }
}
