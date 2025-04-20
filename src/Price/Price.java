package Price;

import java.util.Objects;

public class Price implements Comparable<Price> {
    private final int cents;

    Price(int cents) {
        this.cents = cents;
    }

    public boolean isNegative() {
        return this.cents < 0;
    }

    public Price add(Price p) throws InvalidPriceException {
        if (p == null) throw new InvalidPriceException("Cannot add a null price");
        return new Price(this.cents + p.cents);
    }

    public Price subtract(Price p) throws InvalidPriceException {
        if (p == null) throw new InvalidPriceException("Cannot subtract a null price");
        return new Price(this.cents - p.cents);
    }

    public Price multiply(int n) {
        return new Price(this.cents * n);
    }

    public boolean greaterOrEqual(Price p) throws InvalidPriceException {
        if (p == null) throw new InvalidPriceException("Cannot compare to a null price");
        return this.compareTo(p) >= 0;
    }

    public boolean lessOrEqual(Price p) throws InvalidPriceException {
        if (p == null) throw new InvalidPriceException("Cannot compare to a null price");
        return this.compareTo(p) <= 0;
    }

    public boolean greaterThan(Price p) throws InvalidPriceException {
        if (p == null) throw new InvalidPriceException("Cannot compare to a null price");
        return this.compareTo(p) > 0;
    }

    public boolean lessThan(Price p) throws InvalidPriceException {
        if (p == null) throw new InvalidPriceException("Cannot compare to a null price");
        return this.compareTo(p) < 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return this.cents == price.cents;
    }

    @Override
    public int compareTo(Price p) {
        if (p == null) {
            return -1;
        }
        return this.cents - p.cents;
    }

    @Override
    public String toString() {
        return String.format("%s%,d.%02d", this.isNegative() ? "$-" : "$", Math.abs(cents / 100), Math.abs(cents % 100));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cents);
    }
}
