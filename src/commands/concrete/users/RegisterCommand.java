package commands.concrete.users;

import commands.Command;
import factories.UserFactory;
import models.users.User;
import services.UserService;

import java.util.Scanner;

import static utils.InputUtils.askForField;

public class RegisterCommand implements Command {
    private static final Scanner scanner = new Scanner(System.in);
    @Override
    public void execute() {
        System.out.println("Fill in the following fields to register: ");
        int role = askForRole();
        String username = askForField("username");
        String password = askForPassword();
        String firstName = askForField("first name");
        String lastName = askForField("last name");
        String additionalInfo = role == 2 ? askForField("biography") :
                                role == 3 ? askForField("affiliation") : null;
        User newUser = UserFactory.createUser(role, username, firstName, lastName, password, additionalInfo);
        UserService.getInstance().register(newUser);
        // AuditService.getInstance().log("User registered: " + newUser);
        // DatabaseService.getInstance().saveUser(newUser);
    }

    private int askForRole() {
        while (true) {
            System.out.println("Enter your role: ");
            System.out.println("1. Regular User");
            System.out.println("2. Artist");
            System.out.println("3. Podcast Host");

            int role = scanner.nextInt();

            if (role >= 1 && role <= 3) {
                return role;
            }
        }
    }

    private String askForPassword() {
        String password;
        String confirmPassword;
        scanner.nextLine();

        do {
            System.out.println("Enter your password: ");
            password = scanner.nextLine().trim();
            System.out.println("Confirm your password: ");
            confirmPassword = scanner.nextLine().trim();
        } while (!password.equals(confirmPassword));

        return password;
    }
}
