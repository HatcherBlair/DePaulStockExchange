package product;

import product.price.Price;

public class QuoteSide implements Tradable {
    private final String user;
    private final String product;
    private final Price price;
    private final BookSide side;
    private final int originalVolume;
    private int remainingVolume;
    private int cancelledVolume;
    private int filledVolume;
    private final String id;

    public QuoteSide(String user, String product, Price price, int originalVolume, BookSide side) throws Exception {
        this.user = UserValidator.validate(user);
        this.product = ProductValidator.validate(product);

        if (price == null) {
            throw new Exception("Invalid price");
        }
        this.price = price;

        if (originalVolume > 10000 || originalVolume <= 0) {
            throw new Exception("OriginalVolume must be between 1 and 10k");
        }
        this.originalVolume = originalVolume;

        this.remainingVolume = originalVolume;
        this.cancelledVolume = 0;
        this.filledVolume = 0;

        this.id = user + product + price.toString() + System.nanoTime();

        if (side == null) {
            throw new Exception("Side cannot be null");
        }
        this.side = side;
    }

    @Override
    public String toString() {
        return String.format("%s %s side quote for %s: %s, Orig Vol: %d, Rem Vol: %d, " +
                        "Fill Vol: %d, CXL Vol: %d, ID: %s",
                this.user, this.side, this.product, this.price.toString(), this.originalVolume,
                this.remainingVolume, this.filledVolume, this.cancelledVolume, this.id);
    }

    public TradableDTO makeTradableDTO() {
        return new TradableDTO(this);
    }
}
