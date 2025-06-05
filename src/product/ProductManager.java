package product;

import quote.InvalidQuoteException;
import quote.Quote;
import tradable.InvalidTradableException;
import tradable.Tradable;
import tradable.TradableDTO;
import user.DataValidationException;
import user.UserManager;
import validator.InvalidProductException;
import validator.InvalidUserException;

import java.util.*;

public class ProductManager {
    private static final HashMap<String, ProductBook> productBooks = new HashMap<>();
    private static final ProductManager instance = new ProductManager();

    public static ProductManager getInstance() {
        return instance;
    }

    public void addProduct(String symbol) throws DataValidationException, InvalidProductException {
        // This check is redundant because the product book already has checks
        if (symbol == null) {
            throw new DataValidationException("Symbol cannot be null");
        }

        ProductBook newBook = new ProductBook(symbol);
        productBooks.put(symbol, newBook);
    }

    public ProductBook getProductBook(String symbol) throws DataValidationException {
        ProductBook ret = productBooks.get(symbol);
        if (ret == null) {
            throw new DataValidationException("Cannot find book with symbol: " + symbol);
        }
        return ret;
    }

    public String getRandomProduct() throws DataValidationException {
        ArrayList<String> keys = new ArrayList<>(productBooks.keySet());
        if (keys.isEmpty()) {
            throw new DataValidationException("No books");
        }
        Random random = new Random();
        return keys.get(random.nextInt(keys.size()));
    }

    public TradableDTO addTradable(Tradable o) throws DataValidationException, InvalidTradableException {
        if (o == null) {
            throw new DataValidationException("Cannot add null tradable");
        }

        // Add the tradable
        ProductBook book = productBooks.get(o.getProduct());
        TradableDTO result = book.add(o);

        // Update the user
        UserManager.getInstance().updateTradable(result.user(), result);
        return result;
    }

    public TradableDTO[] addQuote(Quote q) throws DataValidationException, InvalidTradableException, InvalidQuoteException {
        if (q == null) {
            throw new DataValidationException("Cannot add null quote");
        }

        // Add the quote
        ProductBook book = productBooks.get(q.getSymbol());
        TradableDTO[] result = book.add(q); // Add removes the quotes for the user

        // Update the user
        UserManager.getInstance().updateTradable(result[0].user(), result[0]);
        UserManager.getInstance().updateTradable(result[1].user(), result[1]);

        return result;
    }

    public TradableDTO cancel(TradableDTO o) throws DataValidationException {
        if (o == null) {
            throw new DataValidationException("Cannot cancel null tradable");
        }

        // Cancel the trade
        ProductBook book = productBooks.get(o.product());
        TradableDTO result;
        try {
            result = book.cancel(o.side(), o.tradableId());
        } catch (InvalidTradableException e) {
            System.out.println("Unable to cancel tradable: " + o.toString());
            return null;
        }

        // Update the user
        UserManager.getInstance().updateTradable(result.user(), result);
        return result;
    }

    public TradableDTO[] cancelQuote(String symbol, String user) throws DataValidationException, InvalidUserException, InvalidTradableException {
        if (symbol == null || user == null) {
            throw new DataValidationException("Cannot cancel with null user or symbol");
        }

        ProductBook book = productBooks.get(symbol);
        TradableDTO[] result = book.removeQuotesForUser(user);
        if (result[0] == null && result[1] == null) {
            throw new DataValidationException("Product does not exist: " + symbol);
        }

        // If either of these is null it means that side of the quote has been filled, no need to update the tradable
        if (result[0] != null) {
            UserManager.getInstance().updateTradable(result[0].user(), result[0]);
        }
        if (result[1] != null) {
            UserManager.getInstance().updateTradable(result[1].user(), result[1]);
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ProductBook book : productBooks.values()) {
            sb.append(book.toString());
        }
        return sb.toString();
    }
}










