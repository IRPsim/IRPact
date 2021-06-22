package de.unileipzig.irpact.commons.distribution;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;

/**
 * @author Daniel Abitz
 */
public class TruncatedNormalDistribution extends AbstractRealDistribution {

    public TruncatedNormalDistribution(RandomGenerator gen) {
        super(gen);
    }
    @Override
    public double density(double x) {
        return 0;
    }

    @Override
    public double cumulativeProbability(double x) {
        return 0;
    }

    @Override
    public double getNumericalMean() {
        return 0;
    }

    @Override
    public double getNumericalVariance() {
        return 0;
    }

    @Override
    public double getSupportLowerBound() {
        return 0;
    }

    @Override
    public double getSupportUpperBound() {
        return 0;
    }

    @Override
    public boolean isSupportLowerBoundInclusive() {
        return false;
    }

    @Override
    public boolean isSupportUpperBoundInclusive() {
        return false;
    }

    @Override
    public boolean isSupportConnected() {
        return false;
    }
}
