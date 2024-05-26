package commands.concrete.users;

import commands.Command;
import enums.UserRoleEnum;
import exceptions.BadLoginAttemptException;
import models.users.User;
import services.UserService;

import java.util.Objects;
import java.util.function.Predicate;

import static enums.UserRoleEnum.getRoleByIndex;
import static utils.InputUtils.askForField;
import static utils.InputUtils.askForRole;

public class LoginCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Fill in the following fields to log in: ");
        while (true) {
            String username = askForField("username");
            String password = askForField("password");
            UserRoleEnum role = getRoleByIndex(askForRole() - 1);

            try {
                UserService.getInstance().login(username, password, role);
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
