package exceptions;

public class CurrentUserNotInDatabaseException extends InternalServerException {
    public CurrentUserNotInDatabaseException(String message) {
        super(String.format("The current user is not found in the database: %s", message));
    }
}
