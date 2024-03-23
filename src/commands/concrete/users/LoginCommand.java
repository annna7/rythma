package commands.concrete.users;

import commands.Command;
import services.UserService;

import static utils.InputUtils.askForField;

public class LoginCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Fill in the following fields to log in: ");
        while (true) {
            String username = askForField("username");
            String password = askForField("password");
            if (UserService.getInstance().login(username, password)) {
                break;
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        }
        // AuditService.getInstance().log("User logged in: " + UserService.getInstance().getCurrentUser());
        // DatabaseService.getInstance().saveUser(UserService.getInstance().getCurrentUser());
    }
}
