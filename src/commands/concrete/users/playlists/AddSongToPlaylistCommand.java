package commands.concrete.users.playlists;

import commands.Command;
import models.users.User;
import services.PlaylistService;

import java.util.Objects;
import java.util.function.Predicate;

import static utils.InputUtils.askForField;

public class AddSongToPlaylistCommand implements Command {
    @Override
    public void execute() {
        int songId = Integer.parseInt(askForField("song ID"));
        int playlistId = Integer.parseInt(askForField("playlist ID"));

        PlaylistService.getInstance().addSongToPlaylist(songId, playlistId);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return Objects::nonNull;
    }

    @Override
    public String getCommandName() {
        return "Add song to playlist";
    }

    @Override
    public String getCommandDescription() {
        return "Add a song to a playlist, by providing the song ID and the playlist ID";
    }
}
