package de.unileipzig.irpact.commons.distribution;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractRealDistributionWithRounding extends AbstractRealDistribution {

    protected RoundingMode mode;

    public AbstractRealDistributionWithRounding(RandomGenerator rng) {
        super(rng);
    }

    public void setRoundingMode(RoundingMode mode) {
        this.mode = mode;
    }

    public RoundingMode getRoundingMode() {
        return mode;
    }

    protected double roundValue(double value) {
        return mode == null
                ? value
                : mode.apply(value);
    }
}
