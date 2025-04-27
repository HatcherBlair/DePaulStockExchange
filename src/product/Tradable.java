package product;

import product.price.Price;

public interface Tradable {
    String getId();
    int getRemainingVolume();
    void setRemainingVolume(int newVol);
    void setCancelledVolume(int newVol);
    int getCancelledVolume();
    TradableDTO makeTradableDTO();
    Price getPrice();
    void setFilledVolume(int newVol);
    int getFilledVolume();
    BookSide getSide();
    String getUser();
    String getProduct();
    int getOriginalVolume();
}
