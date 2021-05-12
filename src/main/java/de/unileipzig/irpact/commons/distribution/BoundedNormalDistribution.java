package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.util.Rnd;

/**
 * Calculates normal distribution.
 *
 * @author Daniel Abitz
 */
public class BoundedNormalDistribution extends AbstractBoundedUnivariateDoubleDistribution implements UnivariateDoubleDistribution {

    protected Rnd rnd;
    protected double mean;
    protected double standardDeviation;

    public BoundedNormalDistribution() {
    }

    public BoundedNormalDistribution(String name, Rnd rnd, double lowerBound, double upperBound) {
        this(name, rnd, 1.0, 0, lowerBound, upperBound);
    }

    public BoundedNormalDistribution(String name, Rnd rnd, double standardDeviation, double mean, double lowerBound, double upperBound) {
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

    @SuppressWarnings("ManualMinMaxCalculation")
    @Override
    public double drawDoubleValue() {
        double drawnValue = rnd.nextGaussian(standardDeviation, mean);
        if(drawnValue < lowerBound) {
            return lowerBound;
        }
        if(drawnValue > upperBound) {
            return upperBound;
        }
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
