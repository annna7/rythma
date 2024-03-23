package commands.concrete.users.playlists;

import commands.Command;
import services.MusicService;

import static utils.OutputUtils.showCollectionMessage;

public class ViewPlaylistsCommand implements Command {
    @Override
    public void execute() {
        showCollectionMessage("playlists", MusicService.getInstance().getCurrentPlaylists());
    }
}
