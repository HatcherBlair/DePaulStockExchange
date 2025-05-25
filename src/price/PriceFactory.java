package price;

import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PriceFactory {
    /* Regex's suck but felt like a good use of one here...
     * Surrounded by ^...$ - Full string match
     * \\$?-? - Optional dollar sign and negative number
     * \\d{1,3}(?:,\\d{3})+ - 1-3 digits followed by a comma and 3 digits 1+n number of times
     * \\d* - Any number of digits(even 0 for values less than $1),
     *      matches numbers less than 1000 and numbers supplied without commas
     * (?:\\.|\\.\\d{2}) - Optional decimal with 0 or 2 following digits
     * ?: - Ignores capturing groups to help make it faster.  I don't care about parts of the string, only the whole
     *      thing.
     */
    private static final Pattern validPrice = Pattern.compile("^\\$?-?(\\d{1,3}(?:,\\d{3})+|\\d*)(?:\\.|\\.\\d{2})?$");
    private static final TreeMap<Integer, Price> prices = new TreeMap<>();

    public static Price makePrice(int cents) {
        if (prices.containsKey(cents)) {
            return prices.get(cents);
        }
        Price tmp = new Price(cents);
        prices.put(cents, tmp);
        return tmp;
    }

    public static Price makePrice(String stringValueIn) throws InvalidPriceException {
        Matcher matcher = validPrice.matcher(stringValueIn);
        if (!matcher.matches() || stringValueIn.isEmpty()) {
            throw new InvalidPriceException("Invalid String Format: " + stringValueIn);
        }

        int temp = stringValueIn.indexOf('.');
        String cleanInput = stringValueIn.replaceAll("[$,.]", "");
        if ((temp == -1) || (temp == stringValueIn.length() - 1)) {
            cleanInput = cleanInput + "00";
        }
        int cents = Integer.parseInt(cleanInput);
        return makePrice(cents);
    }
}
