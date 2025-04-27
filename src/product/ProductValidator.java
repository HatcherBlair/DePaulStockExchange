package product;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductValidator {
    public static String validate(String product) throws Exception {
        // Product must be 1-5 chars/num and can contain .
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9.]{1,5}$");
        Matcher matcher = pattern.matcher(product);
        if (!matcher.matches()) {
            throw new Exception("invalid product");
        }
        return product;
    }
}
