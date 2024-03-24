package commands;

import models.users.User;

import java.util.function.Predicate;

public interface Command {
    void execute();
    Predicate<User> getVisibilityRule();
    String getCommandName();
    String getCommandDescription();
}
