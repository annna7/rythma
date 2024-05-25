package commands.concrete.users;

import commands.Command;
import enums.UserRoleEnum;
import factories.UserFactory;
import models.users.User;
import services.UserService;

import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Predicate;

import static enums.UserRoleEnum.*;
import static utils.InputUtils.askForField;
import static utils.InputUtils.askForPasswordConfirmation;
import static utils.InputUtils.askForRole;

public class RegisterCommand implements Command {
    @Override
    public void execute() throws SQLException {
        System.out.println("Fill in the following fields to register: ");
        int roleIndex = askForRole();
        roleIndex -= 1; // 1-based index
        UserRoleEnum role = getRoleByIndex(roleIndex);

        String username = askForField("username");
        String password = askForPasswordConfirmation();
        String firstName = askForField("first name");
        String lastName = askForField("last name");
        String additionalInfo = role == ARTIST ? askForField("biography") :
                                role == HOST ? askForField("affiliation") : null;
        User newUser = UserFactory.createUser(role, username, firstName, lastName, password, additionalInfo);
        UserService.getInstance().register(newUser);
        // AuditService.getInstance().log("User registered: " + newUser);
        // DatabaseService.getInstance().saveUser(newUser);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return Objects::isNull;
    }

    @Override
    public String getCommandName() {
        return "Register";
    }

    @Override
    public String getCommandDescription() {
        return "Register a new user; you can choose between 3 roles: regular user, artist (can create albums and songs), and podcast host (can create podcasts and post episodes)";
    }
}
