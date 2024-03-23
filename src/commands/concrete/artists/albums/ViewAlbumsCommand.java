package commands.concrete.artists.albums;

import commands.Command;
import services.MusicService;

import static utils.OutputUtils.showCollectionMessage;

public class ViewAlbumsCommand implements Command {
    @Override
    public void execute() {
        showCollectionMessage("albums", MusicService.getInstance().getCurrentAlbums());
    }
}
