package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.util.Rnd;

/**
 * Calculates normal distribution.
 *
 * @author Daniel Abitz
 */
public class SlowTruncatedNormalDistribution extends AbstractBoundedUnivariateDoubleDistribution implements UnivariateDoubleDistribution {

    protected Rnd rnd;
    protected double mean;
    protected double standardDeviation;

    public SlowTruncatedNormalDistribution() {
    }

    public SlowTruncatedNormalDistribution(String name, Rnd rnd, double lowerBound, double upperBound) {
        this(name, rnd, 1.0, 0, lowerBound, upperBound);
    }

    public SlowTruncatedNormalDistribution(String name, Rnd rnd, double standardDeviation, double mean, double lowerBound, double upperBound) {
        setName(name);
        setRandom(rnd);
        setStandardDeviation(standardDeviation);
        setMean(mean);
        setLowerBound(lowerBound);
        setUpperBound(upperBound);
    }

    public void setRandom(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRandom() {
        return rnd;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setVariance(double variance) {
        setStandardDeviation(Math.sqrt(Math.abs(variance)));
    }

    public double getVariance() {
        return standardDeviation * standardDeviation;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getMean() {
        return mean;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean isNotInRange(double drawnValue) {
        if(lowerBoundInclusive && drawnValue < lowerBound) return true;
        if(!lowerBoundInclusive && drawnValue <= lowerBound) return true;
        if(upperBoundInclusive && drawnValue > upperBound) return true;
        if(!upperBoundInclusive && drawnValue >= upperBound) return true;
        return false;
    }

    @Override
    public double drawDoubleValue() {
        double drawnValue;
        do {
            drawnValue = rnd.nextGaussian(standardDeviation, mean);
        } while(isNotInRange(drawnValue));
        return drawnValue;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                name,
                rnd.getChecksum(),
                standardDeviation,
                mean,
                lowerBound,
                upperBound
        );
    }
}
