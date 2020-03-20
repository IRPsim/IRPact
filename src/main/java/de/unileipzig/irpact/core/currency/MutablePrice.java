package de.unileipzig.irpact.core.currency;

/**
 * @author Daniel Abitz
 */
public class MutablePrice implements Price {

    private double value;

    public MutablePrice(double value) {
        this.value = value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public final ImmutablePrice fixCurrentPrice() {
        return new ImmutablePrice(value);
    }

    @Override
    public double getValue() {
        return value;
    }
}
