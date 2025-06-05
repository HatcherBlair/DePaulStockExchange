package user;

import market.CurrentMarketObserver;
import market.CurrentMarketSide;
import tradable.TradableDTO;
import validator.InvalidUserException;
import validator.UserValidator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class User implements CurrentMarketObserver {
    private String userId;
    private final HashMap<String, TradableDTO> tradables = new HashMap<>();
    private final HashMap<String, CurrentMarketSide[]> currentMarkets = new HashMap<>();

    public User(String userId) throws InvalidUserException {
        setUserId(userId);
    }

    private void setUserId(String userId) throws InvalidUserException {
        this.userId = UserValidator.validate(userId);
    }

    public String getUserId() {
        return this.userId;
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
        ret.append(String.format("\tUser Id: %s\n", this.userId));
        for (TradableDTO tradable : this.tradables.values()) {
            ret.append(String.format("\t\tProduct: %s, Price: %s, OriginalVolume: %d, RemainingVolume: %d, " +
                            "CancelledVolume: %d, FilledVolume: %d, User: %s, Side: %s, Id: %s\n",
                    tradable.product(), tradable.price(), tradable.originalVolume(), tradable.remainingVolume(),
                    tradable.cancelledVolume(), tradable.filledVolume(), tradable.user(), tradable.side(),
                    tradable.tradableId()));
        }
        ret.append("\n");
        return ret.toString();
    }

    @Override
    public void updateCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide) {
        this.currentMarkets.put(symbol, new CurrentMarketSide[]{buySide, sellSide});
    }

    public String getCurrentMarkets() {
        StringBuilder sb = new StringBuilder();
        for (String symbol : currentMarkets.keySet()) {
            CurrentMarketSide[] sides = currentMarkets.get(symbol);
            sb.append(String.format("%s\t%s - %s\n", symbol, sides[0], sides[1]));
        }
        return sb.toString();
    }
}
