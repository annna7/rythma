package commands.concrete.users.notifications;

import commands.Command;
import models.users.User;
import services.NotificationService;

import java.util.Objects;
import java.util.function.Predicate;

import static utils.InputUtils.askForField;

public class FollowUnfollowPlaylistCommand implements Command {
    @Override
    public void execute() {
        int playlistId = askForField("playlist ID", Integer::parseInt);
         NotificationService.getInstance().subscribeOrUnsubscribeFromPlaylist(playlistId);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return Objects::nonNull;
    }

    @Override
    public String getCommandName() {
        return "Follow/Unfollow Playlist";
    }

    @Override
    public String getCommandDescription() {
        return "Follow or unfollow a playlist, given its id (toggle effect); the playlist must be public";
    }
}
