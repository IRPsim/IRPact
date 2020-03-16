package de.unileipzig.irpact.core.currency;

/**
 * @author Daniel Abitz
 */
public final class FinalPrice implements Price {

    private final double value;

    public FinalPrice(double value) {
        this.value = value;
    }

    @Override
    public double getValue() {
        return value;
    }
}
