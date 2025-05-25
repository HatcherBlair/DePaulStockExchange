package user;

import tradable.TradableDTO;
import validator.InvalidUserException;

import java.util.TreeMap;

public class UserManager {

    private final TreeMap<String, User> users = new TreeMap<>();
    private static UserManager instance = null;

    private UserManager() {
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void init(String[] usersIn) throws DataValidationException, InvalidUserException {
        for (String username : usersIn) {
            if (username == null) {
                throw new DataValidationException("Cannot add a null user");
            }
            users.put(username, new User(username));
        }
    }

    public void updateTradable(String userId, TradableDTO o) {

    }
}
