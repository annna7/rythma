package commands.concrete.users;

import commands.Command;
import models.users.User;
import services.UserService;

import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Predicate;

public class ShowUserDetailsCommand implements Command {
    @Override
    public void execute() throws SQLException {
        System.out.println(UserService.getInstance().getCurrentUser());
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return Objects::nonNull;
    }

    @Override
    public String getCommandName() {
        return "Show user details";
    }

    @Override
    public String getCommandDescription() {
        return "Show the details of the currently logged in user";
    }
}
