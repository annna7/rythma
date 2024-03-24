package commands;

import exceptions.IllegalOperationException;
import exceptions.NotFoundException;
import exceptions.UnauthorizedAccessException;

public class CommandInvoker {
    public void executeCommand(Command command) {
        try {
            command.execute();
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IllegalOperationException e) {
            System.out.println("Illegal operation: " + e.getMessage());
        } catch (UnauthorizedAccessException e) {
            System.out.println("Unauthorized access: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
