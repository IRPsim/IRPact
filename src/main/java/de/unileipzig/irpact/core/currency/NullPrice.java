package de.unileipzig.irpact.core.currency;

import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public final class NullPrice implements Price {

    public static final String NAME = NullPrice.class.getSimpleName();
    public static final NullPrice INSTANCE = new NullPrice();

    public NullPrice() {
    }

    public static Price check(Price price) {
        return price == null
                ? INSTANCE
                : price;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public double getValue() {
        throw new NoSuchElementException();
    }
}
