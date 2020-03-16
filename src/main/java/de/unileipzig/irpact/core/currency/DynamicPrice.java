package de.unileipzig.irpact.core.currency;

/**
 * @author Daniel Abitz
 */
public class DynamicPrice implements Price {

    private double value;

    public DynamicPrice(double value) {
        this.value = value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Price getCurrentPrice() {
        return new FinalPrice(value);
    }

    @Override
    public double getValue() {
        return value;
    }
}
