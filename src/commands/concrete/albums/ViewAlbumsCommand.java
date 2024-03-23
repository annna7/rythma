package commands.concrete.albums;

import commands.Command;

public class ViewAlbumsCommand implements Command {
    @Override
    public void execute() {
        System.out.println("View albums command executed!");
    }
}
