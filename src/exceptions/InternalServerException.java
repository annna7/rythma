package exceptions;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(String.format("Internal server error (can't be resolved): %s", message));
    }
}
