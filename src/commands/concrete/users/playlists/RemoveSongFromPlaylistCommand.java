package commands.concrete.users.playlists;

import commands.Command;
import services.MusicService;

import static utils.InputUtils.askForField;

public class RemoveSongFromPlaylistCommand implements Command {
    @Override
    public void execute() {
        int songId = Integer.parseInt(askForField("song ID"));
        int playlistId = Integer.parseInt(askForField("playlist ID"));

        MusicService.getInstance().removeSongFromPlaylist(songId, playlistId);
    }
}
