package de.unileipzig.irpact.commons.distribution;

/**
 * @author Daniel Abitz
 */
public class ConstantDistribution extends UnivariateDistributionBase {

    public static final String NAME = ConstantDistribution.class.getSimpleName();

    private double value;

    public ConstantDistribution(String name, double value) {
        super(name);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public double drawValue() {
        return value;
    }
}
