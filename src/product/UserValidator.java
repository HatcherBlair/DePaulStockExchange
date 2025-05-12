package product;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    public static String validate(String user) throws InvalidUserException {
        // User must be 3 letters
        Pattern pattern = Pattern.compile("^[A-Z]{3}$");
        Matcher matcher = pattern.matcher(user);
        if (!matcher.matches()) {
            throw new InvalidUserException("User must be 3 letters exactly");
        }
        return user;
    }
}
