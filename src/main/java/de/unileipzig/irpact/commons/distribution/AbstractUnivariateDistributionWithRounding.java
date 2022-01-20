package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractUnivariateDistributionWithRounding
        extends NameableBase
        implements UnivariateDoubleDistribution {

    protected RoundingMode mode;

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
