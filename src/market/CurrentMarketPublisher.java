package market;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrentMarketPublisher {
    private static final HashMap<String, ArrayList<CurrentMarketObserver>> filters = new HashMap<>();
    private static final CurrentMarketPublisher instance = new CurrentMarketPublisher();

    private CurrentMarketPublisher() {
    }

    public static CurrentMarketPublisher getInstance() {
        return instance;
    }

    public void subscribeCurrentMarket(String symbol, CurrentMarketObserver cmo) {
        ArrayList<CurrentMarketObserver> filter = filters.get(symbol);
        if (filter == null) {
            filter = new ArrayList<>();
        }
        filter.add(cmo);
        filters.put(symbol, filter);
    }

    public void unSubscribeCurrentMarket(String symbol, CurrentMarketObserver cmo) {
        filters.computeIfPresent(symbol, (k, o) -> {
            o.remove(cmo);
            return o;
        });
    }

    public void acceptCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide) {
        ArrayList<CurrentMarketObserver> filter = filters.get(symbol);
        if (filter == null) {
            return;
        }

        filter.forEach((observer) -> observer.updateCurrentMarket(symbol, buySide, sellSide));
    }
}
