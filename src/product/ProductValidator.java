package product;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductValidator {
    public static String validate(String product) throws InvalidProductException {
        // Product must be 1-5 chars/num and can contain .
        Pattern pattern = Pattern.compile("^[A-Z0-9.]{1,5}$");
        Matcher matcher = pattern.matcher(product);
        if (!matcher.matches()) {
            throw new InvalidProductException("Product must be 1-5 characters, numbers, or optional .");
        }
        return product;
    }
}
