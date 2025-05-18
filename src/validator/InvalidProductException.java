package validator;

public class InvalidProductException extends Exception {
    InvalidProductException(String msg) {
        super(msg);
    }
}
