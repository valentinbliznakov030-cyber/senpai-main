package bg.senpai_main.exceptions;

public class ImageNotDeletedException extends RuntimeException {
    public ImageNotDeletedException(String message) {
        super(message);
    }
}
