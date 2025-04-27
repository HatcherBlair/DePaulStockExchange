package product;

import product.price.Price;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Order implements Tradable {

    private final String user;
    private final String product;
    private final Price price;
    private final BookSide side;
    private final int originalVolume;
    private int remainingVolume;
    private int cancelledVolume;
    private int filledVolume;
    private final String id;

    Order(String user, String product, Price price, BookSide side, int originalVolume) throws Exception {

        this.user = UserValidator.validate(user);
        this.product = ProductValidator.validate(product);

        if (price == null) {
            throw new Exception("Price cannot be null");
        }
        this.price = price;

        if (side == null) {
            throw new Exception("Side cannot be null");
        }
        this.side = side;

        if (originalVolume > 10000 || originalVolume <= 0) {
            throw new Exception("Original volume must be between 1 and 10k");
        }
        this.originalVolume = originalVolume;
        this.remainingVolume = originalVolume;

        this.cancelledVolume = 0;
        this.filledVolume = 0;

        this.id = this.user + this.product + this.price.toString() + System.nanoTime();
    }

    public String getUser() {
        return this.user;
    }

    public String getProduct() {
        return this.product;
    }

    public Price getPrice() {
        return this.price;
    }

    public BookSide getSide() {
        return this.side;
    }

    public int getOriginalVolume() {
        return this.originalVolume;
    }

    @Override
    public String toString() {
        return String.format("%s %s oder: %s at %s, Orig Vol: %d, Rem Vol: %d, Fill Vol: %d, CXL Vol: %d, ID: %s",
                this.user, this.side, this.product, this.price.toString(), this.originalVolume,
                this.remainingVolume, this.filledVolume, this.cancelledVolume, this.id);
    }

    public TradableDTO makeTradableDTO() {
        return new TradableDTO(this);
    }

}
