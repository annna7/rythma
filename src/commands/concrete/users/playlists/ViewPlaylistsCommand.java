package commands.concrete.users.playlists;

import commands.Command;
import services.PlaylistService;

import static utils.OutputUtils.showCollectionMessage;

public class ViewPlaylistsCommand implements Command {
    @Override
    public void execute() {
        showCollectionMessage("playlists", PlaylistService.getInstance().getPlaylistsForCurrentUser());
    }
}
