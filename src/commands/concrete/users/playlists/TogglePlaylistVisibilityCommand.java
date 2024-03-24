package commands.concrete.users.playlists;

import commands.Command;
import models.users.User;
import services.PlaylistService;

import java.util.Objects;
import java.util.function.Predicate;

import static utils.InputUtils.askForField;

public class TogglePlaylistVisibilityCommand implements Command {
    @Override
    public void execute() {
        int playlistId = Integer.parseInt(askForField("playlist ID"));
        PlaylistService.getInstance().togglePlaylistVisibility(playlistId);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return Objects::nonNull;
    }

    @Override
    public String getCommandName() {
        return "Toggle playlist visibility";
    }

    @Override
    public String getCommandDescription() {
        return "Toggle the visibility of one of your playlists between public and private";
    }
}
