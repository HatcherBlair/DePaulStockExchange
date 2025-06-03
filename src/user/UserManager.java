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

    public void updateTradable(String userId, TradableDTO o) throws DataValidationException {
        if (userId == null || !users.containsKey(userId)) {
            throw new DataValidationException("UserId does not exist: " + userId);
        }

        if (o == null) {
            throw new DataValidationException("Cannot update a null tradable");
        }

        users.get(userId).updateTradable(o);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (User user : users.values()) {
            sb.append(user.toString());
        }
        return sb.toString();
    }

    public User getUser(String userId) throws InvalidUserException {
        User ret = users.get(userId);
        if (ret == null) {
            throw new InvalidUserException("User id doesn't exist");
        }
        return ret;
    }
}
