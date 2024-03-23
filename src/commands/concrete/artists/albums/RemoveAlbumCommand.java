package commands.concrete.artists.albums;

import commands.Command;
import services.MusicService;

import static utils.InputUtils.askForField;
public class RemoveAlbumCommand implements Command {
    @Override
    public void execute() {
        int albumId = Integer.parseInt(askForField("the album ID"));
        MusicService.getInstance().removeAlbum(albumId);
    }
}
