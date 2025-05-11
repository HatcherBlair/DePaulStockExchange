package product;

import product.price.Price;

public class QuoteSide implements Tradable {
    private String user;
    private String product;
    private Price price;
    private BookSide side;
    private int originalVolume;
    private int remainingVolume;
    private int cancelledVolume;
    private int filledVolume;
    private final String id;

    public QuoteSide(String user, String product, Price price, int originalVolume, BookSide side) throws Exception {
        setUser(user);
        setProduct(product);
        setPrice(price);
        setSide(side);
        setOriginalVolume(originalVolume);
        setRemainingVolume(originalVolume);
        setFilledVolume(0);
        setCancelledVolume(0);

        this.id = user + product + price.toString() + System.nanoTime();
    }

    public String getUser() {
        return this.user;
    }

    private void setUser(String user) throws Exception {
        this.user = UserValidator.validate(user.toUpperCase());
    }

    public String getProduct() {
        return this.product;
    }

    private void setProduct(String product) throws Exception {
        this.product = ProductValidator.validate(product.toUpperCase());
    }

    public Price getPrice() {
        return this.price;
    }

    private void setPrice(Price price) throws Exception {
        if (price == null) {
            throw new Exception("Price cannot be null");
        }
        this.price = price;
    }

    public BookSide getSide() {
        return this.side;
    }

    private void setSide(BookSide side) throws Exception {
        if (side == null) {
            throw new Exception("Side cannot be null");
        }
        this.side = side;
    }

    public int getOriginalVolume() {
        return this.originalVolume;
    }

    private void setOriginalVolume(int originalVolume) throws Exception {
        if (originalVolume > 10000 || originalVolume <= 0) {
            throw new Exception("Original volume must be between 1 and 10k");
        }
        this.originalVolume = originalVolume;
    }

    public int getRemainingVolume() {
        return this.remainingVolume;
    }

    public void setRemainingVolume(int remainingVolume) {
        this.remainingVolume = remainingVolume;
    }

    public int getFilledVolume() {
        return this.filledVolume;
    }

    public void setFilledVolume(int filledVolume) {
        this.filledVolume = filledVolume;
    }

    public int getCancelledVolume() {
        return this.cancelledVolume;
    }

    public void setCancelledVolume(int cancelledVolume) {
        this.cancelledVolume = cancelledVolume;
    }

    public String getId() {
        return this.id;
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