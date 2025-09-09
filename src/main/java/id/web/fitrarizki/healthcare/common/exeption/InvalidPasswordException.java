package id.web.fitrarizki.healthcare.common.exeption;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
