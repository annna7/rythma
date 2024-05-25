package commands.concrete.users.notifications;

import commands.Command;
import models.users.User;
import services.NotificationService;

import java.util.Objects;
import java.util.function.Predicate;

public class ClearNotificationsCommand implements Command {
    @Override
    public void execute() {
        NotificationService.getInstance().clearNotifications();
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return Objects::nonNull;
    }

    @Override
    public String getCommandName() {
        return "Clear Notifications";
    }

    @Override
    public String getCommandDescription() {
        return "Clear all your notifications";
    }
}
