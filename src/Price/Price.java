package Price;

import java.util.Objects;

public class Price implements Comparable<Price> {
    private int cents;

    Price(int cents) {
        this.cents = cents;
    }

    boolean isNegative() {
        return cents < 0;
    }

    Price add(Price p) throws InvalidPriceException {
        if (p == null) throw new InvalidPriceException("Cannot add a null price");
        return new Price(cents + p.cents);
    }

    Price subtract(Price p) throws InvalidPriceException {
        if (p == null) throw new InvalidPriceException("Cannot subtract a null price");
        return new Price(cents + p.cents);
    }

    Price multiply(int n) {
        return new Price(cents * n);
    }

    public boolean greaterOrEqual(Price p) {
        return this.compareTo(p) >= 0;
    }

    public boolean lessOrEqual(Price p) {
        return this.compareTo(p) <= 0;
    }

    public boolean greaterThan(Price p) {
        return this.compareTo(p) > 0;
    }

    public boolean lessThan(Price p) {
        return this.compareTo(p) < 0;
    }

    @Override
    public String toString() {
        return String.format("$%,d.%d", cents / 100, cents % 100);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return cents == price.cents;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cents);
    }

    @Override
    public int compareTo(Price p) {
        return cents - p.cents;
    }
}
