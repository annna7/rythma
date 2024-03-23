package commands;

import models.users.User;

import java.util.function.Predicate;

// TODO: create Command Invoker to print errors
public interface Command {
    void execute();
    Predicate<User> getVisibilityRule();
    String getCommandName();
    String getCommandDescription();
}
