package market;

import price.InvalidPriceException;
import price.Price;
import price.PriceFactory;

public class CurrentMarketTracker {

    private static final CurrentMarketTracker instance = new CurrentMarketTracker();

    private CurrentMarketTracker() {
    }

    public static CurrentMarketTracker getInstance() {
        return instance;
    }

    public void updateMarket(String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume) {
        String marketWidth;
        if (buyPrice == null || sellPrice == null) {
            marketWidth = "$0.00";
        } else {
            try {
                marketWidth = sellPrice.subtract(buyPrice).toString();
            } catch (InvalidPriceException ignored) {
                // Impossible to be here
                marketWidth = "";
            }
        }
        if (buyPrice == null) {
            buyPrice = PriceFactory.makePrice(0);
        }
        if (sellPrice == null) {
            sellPrice = PriceFactory.makePrice(0);
        }
        CurrentMarketSide buySide;
        CurrentMarketSide sellSide;
        try {
            buySide = new CurrentMarketSide(buyPrice, buyVolume);
            sellSide = new CurrentMarketSide(buyPrice, buyVolume);
        } catch (InvalidPriceException ignored) {
        }
        System.out.print("*********** Current Market ***********\n");
        System.out.printf("* %s   %sx%d - %sx%d [%s]\n", symbol, buyPrice, buyVolume, sellPrice, sellVolume, marketWidth);
        System.out.print("***********************************\n");
    }
}
