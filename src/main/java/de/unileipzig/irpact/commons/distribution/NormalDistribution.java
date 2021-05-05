package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.Rnd;

/**
 * Calculates normal distribution.
 *
 * @author Daniel Abitz
 */
public class NormalDistribution extends NameableBase implements UnivariateDoubleDistribution {

    protected Rnd rnd;
    protected double mean;
    protected double standardDeviation;

    public NormalDistribution() {
    }

    public NormalDistribution(String name, Rnd rnd) {
        this(name, rnd, 1.0, 0);
    }

    public NormalDistribution(String name, Rnd rnd, double standardDeviation, double mean) {
        setName(name);
        setRandom(rnd);
        setMean(mean);
        setStandardDeviation(standardDeviation);
    }

    public void setRandom(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRandom() {
        return rnd;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getMean() {
        return mean;
    }

    public void setVariance(double variance) {
        setStandardDeviation(Math.sqrt(Math.abs(variance)));
    }

    public double getVariance() {
        return standardDeviation * standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    @Override
    public double drawDoubleValue() {
        return rnd.nextGaussian(standardDeviation, mean);
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                name,
                rnd.getChecksum(),
                standardDeviation,
                mean
        );
    }
}
