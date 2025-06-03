package market;

import price.InvalidPriceException;
import price.Price;

public class CurrentMarketTracker {

    private static final CurrentMarketTracker instance = new CurrentMarketTracker();

    private CurrentMarketTracker() {
    }

    public void updateMarket(String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume) throws InvalidPriceException {
        String marketWidth;
        if (buyPrice == null || sellPrice == null) {
            marketWidth = "$0.00";
        } else {
            marketWidth = sellPrice.subtract(buyPrice).toString();
        }
        CurrentMarketSide buySide = new CurrentMarketSide(buyPrice, buyVolume);
        CurrentMarketSide sellSide = new CurrentMarketSide(sellPrice, sellVolume);
        System.out.print("*********** Current Market ***********\n");
        System.out.printf("* %s   %sx%d - %sx%d [%s]\n", symbol, buyPrice, buyVolume, sellPrice, sellVolume, marketWidth);
        System.out.print("***********************************\n");
    }
}
