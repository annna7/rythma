package commands.concrete;

import commands.Command;

public class LoginCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Login command executed!");
    }
}
