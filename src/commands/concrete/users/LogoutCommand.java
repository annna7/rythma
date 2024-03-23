package commands.concrete.users;

import commands.Command;
import models.users.User;
import services.UserService;

import java.util.Objects;
import java.util.function.Predicate;

public class LogoutCommand implements Command {
    @Override
    public void execute() {
         UserService.getInstance().logout();
        // AuditService.getInstance().log("User logged out: " + UserService.getInstance().getCurrentUser());
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return Objects::nonNull;
    }

    @Override
    public String getCommandName() {
        return "Logout";
    }

    @Override
    public String getCommandDescription() {
        return "Log out of the application";
    }
}
