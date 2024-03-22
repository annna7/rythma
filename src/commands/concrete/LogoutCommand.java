package commands.concrete;

import commands.Command;

public class LogoutCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Logout command executed!");
    }
}
