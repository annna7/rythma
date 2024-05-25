package commands.concrete.users;

import commands.Command;
import exceptions.BadLoginAttemptException;
import models.users.User;
import services.UserService;

import java.util.Objects;
import java.util.function.Predicate;

import static utils.InputUtils.askForField;

public class LoginCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Fill in the following fields to log in: ");
        while (true) {
            String username = askForField("username");
            String password = askForField("password");
            try {
                UserService.getInstance().login(username, password);
                break;
            } catch (BadLoginAttemptException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return Objects::isNull;
    }

    @Override
    public String getCommandName() {
        return "Login";
    }

    @Override
    public String getCommandDescription() {
        return "Log in to the application";
    }
}
