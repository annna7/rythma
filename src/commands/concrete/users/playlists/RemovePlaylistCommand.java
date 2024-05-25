package commands.concrete.users.playlists;

import commands.Command;
import models.users.User;
import services.PlaylistService;

import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Predicate;

import static utils.InputUtils.askForField;

public class RemovePlaylistCommand implements Command {
    @Override
    public void execute() throws SQLException {
        int playlistId = Integer.parseInt(askForField("playlist ID"));
        PlaylistService.getInstance().removePlaylist(playlistId);
        System.out.println("Playlist removed successfully.");
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return Objects::nonNull;
    }

    @Override
    public String getCommandName() {
        return "Remove playlist";
    }

    @Override
    public String getCommandDescription() {
        return "Remove a playlist by providing the playlist ID";
    }
}
