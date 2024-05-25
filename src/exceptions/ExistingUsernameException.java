package exceptions;

public class ExistingUsernameException extends RuntimeException {
    public ExistingUsernameException(String username) {
        super("Can't create a user with the username " + username + "\nThe username you entered is already in use.");
    }
}
