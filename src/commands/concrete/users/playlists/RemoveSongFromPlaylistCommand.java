package commands.concrete.users.playlists;

import commands.Command;
import models.users.User;
import services.PlaylistService;

import java.util.Objects;
import java.util.function.Predicate;

import static utils.InputUtils.askForField;

public class RemoveSongFromPlaylistCommand implements Command {
    @Override
    public void execute() {
        int songId = Integer.parseInt(askForField("song ID"));
        int playlistId = Integer.parseInt(askForField("playlist ID"));

        PlaylistService.getInstance().removeSongFromPlaylist(songId, playlistId);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return Objects::nonNull;
    }

    @Override
    public String getCommandName() {
        return "Remove song from playlist";
    }

    @Override
    public String getCommandDescription() {
        return "Remove a song from a playlist, by providing the song ID and the playlist ID";
    }
}
