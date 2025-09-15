package id.web.fitrarizki.healthcare.common.exeption;

public class ForbiddenAccessException extends RuntimeException {
    public ForbiddenAccessException(String message) {
        super(message);
    }
}
