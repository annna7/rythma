package commands.concrete.users;

import commands.Command;
import services.UserService;

public class LogoutCommand implements Command {
    @Override
    public void execute() {
         UserService.getInstance().logout();
        // AuditService.getInstance().log("User logged out: " + UserService.getInstance().getCurrentUser());
    }
}
