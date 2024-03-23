package commands.concrete.users.playlists;

import commands.Command;
import services.MusicService;
import services.PlaylistService;

import static utils.InputUtils.askForField;

public class RemovePlaylistCommand implements Command {
    @Override
    public void execute() {
        int playlistId = Integer.parseInt(askForField("playlist ID"));
        PlaylistService.getInstance().removePlaylist(playlistId);
        System.out.println("Playlist removed successfully.");
    }
}
