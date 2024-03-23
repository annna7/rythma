package commands.concrete.users;

import commands.Command;

public class ExitCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Exit command executed!");
    }
}
