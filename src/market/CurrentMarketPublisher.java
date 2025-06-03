package market;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrentMarketPublisher {
    private static final HashMap<String, ArrayList<CurrentMarketObserver>> filters = new HashMap<>();

    public void subscribeCurrentMarket(String symbol, CurrentMarketObserver cmo) {

    }

    public void unsubscribeCurrentMarket(String symbol, CurrentMarketObserver cmo) {

    }

    public void acceptCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide) {
        
    }
}
