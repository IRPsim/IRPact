package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.util.Rnd;

/**
 * @author Daniel Abitz
 */
public class BoundedUniformDoubleDistribution extends AbstractBoundedUnivariateDoubleDistribution implements BoundedUnivariateDoubleDistribution {

    protected long seed;
    protected Rnd rnd;

    public BoundedUniformDoubleDistribution() {
    }

    public BoundedUniformDoubleDistribution(String name, double lowerBound, double upperBound, Rnd rnd) {
        setName(name);
        setLowerBound(lowerBound);
        setUpperBound(upperBound);
        setRandom(rnd);
    }

    public void setRandom(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRandom() {
        return rnd;
    }

    @Override
    public double drawDoubleValue() {
        return rnd.nextDouble(lowerBound, upperBound);
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(name, lowerBound, upperBound, rnd.getChecksum());
    }
}
