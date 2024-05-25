package commands.concrete.users.notifications;

import commands.Command;
import models.users.User;
import services.NotificationService;

import java.util.Objects;
import java.util.function.Predicate;

import static utils.InputUtils.askForField;

public class FollowUnfollowArtistCommand implements Command {
    @Override
    public void execute() {
        int artistId = askForField("artist ID", Integer::parseInt);
        NotificationService.getInstance().subscribeOrUnsubscribeFromArtist(artistId);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return Objects::nonNull;
    }

    @Override
    public String getCommandName() {
        return "Follow/Unfollow Artist";
    }

    @Override
    public String getCommandDescription() {
        return "Follow or unfollow an artist, given their id (toggle efect)";
    }
}
