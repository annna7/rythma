package exceptions;

public class BadLoginAttemptException extends RuntimeException {
    public BadLoginAttemptException() {
        super("Invalid username or password. Please try again.");
    }
}
