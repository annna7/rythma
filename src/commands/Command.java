package commands;

import models.users.User;

import java.sql.SQLException;
import java.util.function.Predicate;

public interface Command {
    void execute() throws SQLException;
    Predicate<User> getVisibilityRule();
    String getCommandName();
    String getCommandDescription();
}
