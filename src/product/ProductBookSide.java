package product;

import price.Price;
import tradable.InvalidTradableException;
import tradable.Tradable;
import tradable.TradableDTO;
import user.DataValidationException;
import user.UserManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class ProductBookSide {
    private final BookSide side;
    private final TreeMap<Price, ArrayList<Tradable>> bookEntries;

    public ProductBookSide(BookSide side) throws InvalidSideException {
        if (side == null) {
            throw new InvalidSideException("Cannot create a book side with a null side");
        }
        this.side = side;

        if (this.side == BookSide.BUY) {
            bookEntries = new TreeMap<>(Collections.reverseOrder());
        } else {
            bookEntries = new TreeMap<>();
        }
    }

    public TradableDTO add(Tradable o) throws InvalidTradableException {
        if (o == null) {
            throw new InvalidTradableException("Cannot add a null tradable");
        }
        ArrayList<Tradable> priceEntries = bookEntries.get(o.getPrice());
        if (priceEntries == null) {
            priceEntries = new ArrayList<>();
        }
        priceEntries.add(o);
        bookEntries.put(o.getPrice(), priceEntries);

        TradableDTO result = new TradableDTO(o);
        try {
            UserManager.getInstance().updateTradable(result.user(), result);
        } catch (DataValidationException ignored) {
        }
        return result;
    }

    public TradableDTO cancel(String tradableId) throws InvalidTradableException {
        for (Price p : bookEntries.keySet()) {
            ArrayList<Tradable> entries = bookEntries.get(p);
            for (Tradable t : entries) {
                if (tradableId.equals(t.getId())) {
                    System.out.println("**CANCEL: " + t);
                    t.setCancelledVolume(t.getCancelledVolume() + t.getRemainingVolume());
                    t.setRemainingVolume(0);
                    entries.remove(t);
                    if (entries.isEmpty()) {
                        bookEntries.remove(p);
                    }
                    TradableDTO result = new TradableDTO(t);
                    try {
                        UserManager.getInstance().updateTradable(result.user(), result);
                    } catch (DataValidationException ignored) {
                    }
                    return result;
                }
            }
        }
        throw new InvalidTradableException("Cancelled order doesn't exist");
    }

    public TradableDTO removeQuotesForUser(String userName) throws InvalidTradableException {
        for (ArrayList<Tradable> entries : bookEntries.values()) {
            for (Tradable t : entries) {
                if (userName.equals(t.getUser())) {
                    TradableDTO result = cancel(t.getId());
                    try {
                        UserManager.getInstance().updateTradable(result.user(), result);
                    } catch (DataValidationException ignored) {
                    }
                    return result;
                }
            }
        }
        return null;
    }

    public Price topOfbookPrice() {
        return bookEntries.isEmpty() ? null : bookEntries.firstKey();
    }

    public int topOfBookVolume() {
        if (bookEntries.firstEntry() == null) {
            return 0;
        }
        int volume = 0;
        for (Tradable t : bookEntries.firstEntry().getValue()) {
            volume += t.getRemainingVolume();
        }
        return volume;
    }

    public void tradeOut(Price price, int vol) {
        // Can't trade
        Price p = topOfbookPrice();
        if (p == null || p.compareTo(price) > 0) {
            return;
        }

        ArrayList<Tradable> entries = bookEntries.get(p);
        int totalVol = topOfBookVolume();

        // We can trade entire book
        if (vol >= totalVol) {
            for (Tradable t : entries) {
                int temp = t.getRemainingVolume();
                t.setFilledVolume(t.getFilledVolume() + temp);
                t.setRemainingVolume(0);
                System.out.printf("FULL FILL: (%s %d) %s%n", t.getSide(), temp, t);
                try {
                    UserManager.getInstance().updateTradable(t.getUser(), t.makeTradableDTO());
                } catch (DataValidationException ignored) {
                    // We are making valid data straight from the tradable, it will never be invalid
                }
            }
            bookEntries.remove(p);
            return;
        }

        // Can't trade whole book
        int remainder = vol;
        for (Tradable t : entries) {
            double ratio = (double) t.getRemainingVolume() / (double) totalVol;
            int toTrade = (int) Math.ceil(vol * ratio);
            toTrade = Math.min(toTrade, remainder);
            t.setFilledVolume(t.getFilledVolume() + toTrade);
            t.setRemainingVolume(t.getRemainingVolume() - toTrade);
            System.out.printf("PARTIAL FILL: (%s %d) %s%n", t.getSide(), t.getFilledVolume(), t);
            remainder -= toTrade;
            try {
                UserManager.getInstance().updateTradable(t.getUser(), t.makeTradableDTO());
            } catch (DataValidationException ignored) {
                // We are making valid data straight from the tradable, it will never be invalid.
            }
        }
    }

    @Override
    public String toString() {
        if (bookEntries.isEmpty()) {
            return String.format("\tSide: %s\n\t\t<Empty>", this.side);
        }

        StringBuilder ret = new StringBuilder(String.format("\tSide: %s\n", this.side));
        for (Price p : bookEntries.keySet()) {
            ret.append(String.format("\t\tPrice: %s\n", p.toString()));
            for (Tradable t : bookEntries.get(p)) {
                ret.append(String.format("\t\t\t%s\n", t.toString()));
            }
        }
        return ret.toString();
    }
}
