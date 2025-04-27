package product;

import product.price.Price;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Order implements Tradable{

    private final String user;
    private final String product;
    private final Price price;
    private final BookSide side;
    private final int originalVolume;
    private int remainingVolume;
    private int cancelledVolume;
    private int filledVolume;
    private final String id;
    Order(String user, String product,
          Price price, BookSide side,
          int originalVolume) throws Exception{
        // User must be 3 letters
        Pattern pattern = Pattern.compile("^[a-zA-Z]{3}$");
        Matcher matcher = pattern.matcher(user);
        if(!matcher.matches()) {
            throw new Exception("invalid user");
        }
        this.user = user;

        // Product must be 1-5 chars/num and can contain .
        pattern = Pattern.compile("^[a-zA-Z0-9.]{1,5}$");
        matcher = pattern.matcher(product);
        if(!matcher.matches()) {
            throw new Exception("invalid product");
        }
        this.product = product;

        if(price == null) {
            throw new Exception("Price cannot be null");
        }
        this.price = price;

        if(side == null) {
            throw new Exception("Side cannot be null");
        }
        this.side = side;

        if(originalVolume > 10000 || originalVolume < 0) {
            throw new Exception("Original volume must be between 0 and 10k");
        }
        this.originalVolume = originalVolume;
        this.remainingVolume = originalVolume;

        this.cancelledVolume = 0;
        this.filledVolume = 0;

        this.id = this.user + this.product + this.price.toString() + System.nanoTime();
    }
    
}
