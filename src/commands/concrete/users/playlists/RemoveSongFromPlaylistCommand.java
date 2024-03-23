package commands.concrete.users.playlists;

import commands.Command;
import services.MusicService;
import services.PlaylistService;

import static utils.InputUtils.askForField;

public class RemoveSongFromPlaylistCommand implements Command {
    @Override
    public void execute() {
        int songId = Integer.parseInt(askForField("song ID"));
        int playlistId = Integer.parseInt(askForField("playlist ID"));

        PlaylistService.getInstance().removeSongFromPlaylist(songId, playlistId);
    }
}
