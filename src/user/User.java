package user;

import tradable.TradableDTO;
import validator.InvalidUserException;
import validator.UserValidator;

import java.util.HashMap;

public class User {
    private String userId;
    private final HashMap<String, TradableDTO> tradables = new HashMap<>();

    public User(String userId) throws InvalidUserException {
        setUserId(userId);
    }

    private void setUserId(String userId) throws InvalidUserException {
        this.userId = UserValidator.validate(userId);
    }

    public void updateTradable(TradableDTO o) {
        if (o == null) {
            return;
        }

        tradables.put(o.tradableId(), o);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(String.format("User Id: %s\n", this.userId));
        for (TradableDTO tradable : this.tradables.values()) {
            ret.append(String.format("\tProduct: %s, Price: %s, OriginalVolume: %d, RemainingVolume: %d, " +
                            "CancelledVolume: %d, FilledVolume: %d, User: %s, Side: %s, Id: %s\n",
                    tradable.product(), tradable.price(), tradable.originalVolume(), tradable.remainingVolume(),
                    tradable.cancelledVolume(), tradable.filledVolume(), tradable.user(), tradable.side(),
                    tradable.tradableId()));
        }
        return ret.toString();
    }
}
