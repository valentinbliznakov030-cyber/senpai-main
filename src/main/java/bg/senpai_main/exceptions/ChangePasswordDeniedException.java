package bg.senpai_main.exceptions;

public class ChangePasswordDeniedException extends RuntimeException {
    public ChangePasswordDeniedException(String message) {
        super(message);
    }
}
