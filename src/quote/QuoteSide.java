package quote;

import order.InvalidVolumeException;
import price.InvalidPriceException;
import product.*;
import price.Price;
import tradable.Tradable;
import tradable.TradableDTO;
import validator.InvalidProductException;
import validator.InvalidUserException;
import validator.ProductValidator;
import validator.UserValidator;

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

    public QuoteSide(String user, String product, Price price, int originalVolume, BookSide side)
            throws InvalidUserException, InvalidProductException, InvalidPriceException, InvalidSideException, InvalidVolumeException {
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

    private void setUser(String user) throws InvalidUserException {
        this.user = UserValidator.validate(user.toUpperCase());
    }

    public String getProduct() {
        return this.product;
    }

    private void setProduct(String product) throws InvalidProductException {
        this.product = ProductValidator.validate(product.toUpperCase());
    }

    public Price getPrice() {
        return this.price;
    }

    private void setPrice(Price price) throws InvalidPriceException {
        if (price == null) {
            throw new InvalidPriceException("Price cannot be null");
        }
        this.price = price;
    }

    public BookSide getSide() {
        return this.side;
    }

    private void setSide(BookSide side) throws InvalidSideException {
        if (side == null) {
            throw new InvalidSideException("Side cannot be null");
        }
        this.side = side;
    }

    public int getOriginalVolume() {
        return this.originalVolume;
    }

    private void setOriginalVolume(int originalVolume) throws InvalidVolumeException {
        if (originalVolume > 10000 || originalVolume <= 0) {
            throw new InvalidVolumeException("Original volume must be between 1 and 10k");
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
        return String.format("%s %s side quote: %s at %s, Orig Vol: %3d, Rem Vol: %3d, Fill Vol: %3d, Cxl'd Vol: %3d, ID: %s",
                this.user, this.side, this.product, this.price.toString(), this.originalVolume,
                this.remainingVolume, this.filledVolume, this.cancelledVolume, this.id);
    }

    public TradableDTO makeTradableDTO() {
        return new TradableDTO(this);
    }
}