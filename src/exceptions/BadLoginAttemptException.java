package exceptions;

public class BadLoginAttemptException extends RuntimeException {
    public BadLoginAttemptException() {
        super("Invalid combination of username, password and role. Please try again.");
    }
}
