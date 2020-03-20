package de.unileipzig.irpact.core.currency;

/**
 * @author Daniel Abitz
 */
public final class ImmutablePrice implements Price {

    private final double value;

    public ImmutablePrice(double value) {
        this.value = value;
    }

    @Override
    public double getValue() {
        return value;
    }
}
