package commands.concrete.users.notifications;

import commands.Command;
import models.users.User;
import services.NotificationService;

import java.util.Objects;
import java.util.function.Predicate;

import static utils.InputUtils.askForField;


public class FollowUnfollowHostCommand implements Command {
    @Override
    public void execute() {
        int hostId = askForField("host ID", Integer::parseInt);
        NotificationService.getInstance().subscribeOrUnsubscribeFromHost(hostId);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return Objects::nonNull;
    }

    @Override
    public String getCommandName() {
        return "Follow/Unfollow Host";
    }

    @Override
    public String getCommandDescription() {
        return "Follow or unfollow a host, given their id (toggle effect)";
    }
}
