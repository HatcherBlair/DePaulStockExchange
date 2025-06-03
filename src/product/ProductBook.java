package product;

import market.CurrentMarketTracker;
import quote.InvalidQuoteException;
import quote.Quote;
import price.Price;
import tradable.InvalidTradableException;
import tradable.Tradable;
import tradable.TradableDTO;
import validator.InvalidProductException;
import validator.InvalidUserException;
import validator.ProductValidator;

public class ProductBook {
    private final String product;
    private final ProductBookSide buySide;
    private final ProductBookSide sellSide;

    public ProductBook(String product) throws InvalidProductException {
        this.product = ProductValidator.validate(product.toUpperCase());

        try {
            this.buySide = new ProductBookSide(BookSide.BUY);
            this.sellSide = new ProductBookSide(BookSide.SELL);
        } catch (InvalidSideException e) {
            // This catch will never execute because the buySide and sellSide are always passed a valid side
            // Need to crash here because otherwise buySide and sellSide might not be initialized
            throw new RuntimeException("Unable to create ProductBookSide");
        }
    }

    public TradableDTO add(Tradable t) throws InvalidTradableException {
        if (t == null) {
            throw new InvalidTradableException("Cannot add a null Tradable to book side");
        }
        System.out.println("**ADD: " + t);

        TradableDTO ret;
        if (t.getSide() == BookSide.BUY) {
            ret = buySide.add(t);
        } else {
            ret = sellSide.add(t);
        }

        tryTrade();
        updateMarket();
        return ret;
    }

    public TradableDTO[] add(Quote qte) throws InvalidQuoteException, InvalidTradableException {
        if (qte == null) {
            throw new InvalidQuoteException("Cannot add a null Quote to book");
        }
        try {
            removeQuotesForUser(qte.getUser());
        } catch (InvalidUserException ignored) {
        }
        TradableDTO buy = buySide.add(qte.getQuoteSide(BookSide.BUY));
        TradableDTO sell = sellSide.add(qte.getQuoteSide(BookSide.SELL));
        System.out.println("**ADD: " + qte.getQuoteSide(BookSide.BUY));
        System.out.println("**ADD: " + qte.getQuoteSide(BookSide.SELL));
        tryTrade();
        updateMarket();
        return new TradableDTO[]{buy, sell};
    }

    public TradableDTO cancel(BookSide side, String orderId) throws InvalidTradableException {
        if (side.equals(BookSide.BUY)) {
            updateMarket();
            return buySide.cancel(orderId);
        } else {
            updateMarket();
            return sellSide.cancel(orderId);
        }

    }

    public TradableDTO[] removeQuotesForUser(String userName) throws InvalidTradableException, InvalidUserException {
        if (userName == null) {
            throw new InvalidUserException("User cannot be null");
        }
        TradableDTO buy = buySide.removeQuotesForUser(userName);
        TradableDTO sell = sellSide.removeQuotesForUser(userName);
        updateMarket();
        return new TradableDTO[]{buy, sell};
    }

    public void tryTrade() {
        if (buySide.topOfbookPrice() == null || sellSide.topOfbookPrice() == null) {
            return;
        }

        int totalToTrade = Math.max(buySide.topOfBookVolume(), sellSide.topOfBookVolume());
        if (totalToTrade == 0) {
            return;
        }

        while (totalToTrade > 0) {
            Price buy = buySide.topOfbookPrice();
            Price sell = sellSide.topOfbookPrice();
            if (buy == null || sell == null) {
                return;
            }
            if (sell.compareTo(buy) > 0) {
                return;
            }

            int toTrade = Math.min(buySide.topOfBookVolume(), sellSide.topOfBookVolume());

            buySide.tradeOut(buy, toTrade);
            sellSide.tradeOut(buy, toTrade);
            totalToTrade -= toTrade;
        }
    }

    public String getTopOfBookSide(BookSide side) {
        Price p;
        int vol;
        if (side.equals(BookSide.BUY)) {
            p = buySide.topOfbookPrice();
            vol = buySide.topOfBookVolume();
        } else {
            p = sellSide.topOfbookPrice();
            vol = sellSide.topOfBookVolume();
        }

        return String.format("Top of %s book: %s x %d", side, p == null ? "$0.00" : p.toString(), vol);
    }

    private void updateMarket() {
        CurrentMarketTracker.getInstance().updateMarket(this.product, this.buySide.topOfbookPrice(),
                this.buySide.topOfBookVolume(), this.sellSide.topOfbookPrice(), this.sellSide.topOfBookVolume());
    }

    @Override
    public String toString() {
        return String.format("Product: %s\n%s\n%s", this.product, buySide.toString(), sellSide.toString());
    }
}
