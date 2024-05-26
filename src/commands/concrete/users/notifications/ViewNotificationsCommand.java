package commands.concrete.users.notifications;

import commands.Command;
import models.users.User;
import services.NotificationService;
import services.UserService;

import java.util.Objects;
import java.util.function.Predicate;

public class ViewNotificationsCommand implements Command {
    @Override
    public void execute() {
        NotificationService.getInstance().viewNotifications(UserService.getInstance().getCurrentUser().getId());
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return Objects::nonNull;
    }

    @Override
    public String getCommandName() {
        return "View your notifications";
    }

    @Override
    public String getCommandDescription() {
        return "View all your notifications, with the ones you haven't seen yet marked as unread";
    }
}
