package product;

import product.price.Price;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Quote {

    private final String user;
    private final String product;
    private final QuoteSide buySide;
    private final QuoteSide sellSide;

    public Quote(String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume, String userName) throws Exception {

        this.user = UserValidator.validate(userName);
        this.product = ProductValidator.validate(symbol);

        this.buySide = new QuoteSide(userName, symbol, buyPrice, buyVolume, BookSide.BUY);
        this.sellSide = new QuoteSide(userName, symbol, sellPrice, sellVolume, BookSide.SELL);
    }

    public QuoteSide getQuoteSide(BookSide sideIn) {
        if (sideIn == null) {
            return null;
        } else if (sideIn == BookSide.BUY) {
            return this.buySide;
        } else {
            return this.sellSide;
        }
    }

    public String getSymbol() {
        return this.product;
    }

    public String getUser() {
        return this.user;
    }
}
