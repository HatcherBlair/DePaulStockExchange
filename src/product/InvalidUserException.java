package product;

public class InvalidUserException extends Exception {
    InvalidUserException(String msg) {
        super(msg);
    }
}
