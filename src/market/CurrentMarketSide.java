package market;

import price.InvalidPriceException;
import price.Price;

public class CurrentMarketSide {
    private final Price price;
    private final int volume;

    CurrentMarketSide(Price price, int volume) throws InvalidPriceException {
        if (price == null) {
            throw new InvalidPriceException("Price cannot be null");
        }
        this.price = price;
        this.volume = volume;
    }

    @Override
    public String toString() {
        return this.price + "x" + this.volume;
    }
}
