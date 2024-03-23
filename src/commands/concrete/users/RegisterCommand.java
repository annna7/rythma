package commands.concrete.users;

import commands.Command;
import enums.UserRoleEnum;
import factories.UserFactory;
import models.users.User;
import services.UserService;

import java.util.Scanner;

import static enums.UserRoleEnum.*;
import static utils.InputUtils.askForField;
import static utils.InputUtils.askForPasswordConfirmation;
import static utils.InputUtils.askForRole;

public class RegisterCommand implements Command {
    private static final Scanner scanner = new Scanner(System.in);
    @Override
    public void execute() {
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
}
