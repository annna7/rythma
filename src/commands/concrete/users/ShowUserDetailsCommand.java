package commands.concrete.users;

import commands.Command;
import services.UserService;

public class ShowUserDetailsCommand implements Command {
    @Override
    public void execute() {
        System.out.println(UserService.getInstance().getCurrentUser());
    }
}
