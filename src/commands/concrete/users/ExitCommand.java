package commands.concrete.users;

import commands.Command;
import models.users.User;

import java.util.function.Predicate;

public class ExitCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Exit command executed!");
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return user -> true;
    }

    @Override
    public String getCommandName() {
        return "Exit";
    }

    @Override
    public String getCommandDescription() {
        return "Exit the application";
    }
}
