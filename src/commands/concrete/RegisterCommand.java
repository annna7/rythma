package commands.concrete;

import commands.Command;

public class RegisterCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Register command executed!");
    }
}
