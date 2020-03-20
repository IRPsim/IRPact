package de.unileipzig.irpact.commons.distribution;

/**
 * @author Daniel Abitz
 */
public class InfinityDistribution extends ConstantDistribution {

    public static final String NAME = InfinityDistribution.class.getSimpleName();

    public InfinityDistribution(String name) {
        super(name, Double.POSITIVE_INFINITY);
    }
}
