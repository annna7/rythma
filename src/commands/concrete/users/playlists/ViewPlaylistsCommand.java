package commands.concrete.users.playlists;

import commands.Command;
import models.users.User;
import services.PlaylistService;

import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Predicate;

import static utils.OutputUtils.showCollectionMessage;

public class ViewPlaylistsCommand implements Command {
    @Override
    public void execute() throws SQLException {
        showCollectionMessage("playlists", PlaylistService.getInstance().getPlaylistsForCurrentUser());
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return Objects::nonNull;
    }

    @Override
    public String getCommandName() {
        return "View playlists";
    }

    @Override
    public String getCommandDescription() {
        return "View all playlists for the currently logged in user";
    }
}
