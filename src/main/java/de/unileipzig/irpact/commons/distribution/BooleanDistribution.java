package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.Rnd;

/**
 * @author Daniel Abitz
 */
public class BooleanDistribution extends NameableBase implements UnivariateDoubleDistribution {

    protected long seed;
    protected Rnd rnd;
    protected double trueValue;
    protected double falseValue;

    public BooleanDistribution() {
    }

    public BooleanDistribution(String name, Rnd rnd) {
        this(name, rnd, 1, 0);
    }

    public BooleanDistribution(String name, Rnd rnd, double trueValue, double falseValue) {
        setName(name);
        setRandom(rnd);
        setTrueValue(trueValue);
        setFalseValue(falseValue);
    }

    public void setRandom(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRandom() {
        return rnd;
    }

    public void setTrueValue(double trueValue) {
        this.trueValue = trueValue;
    }

    public double getTrueValue() {
        return trueValue;
    }

    public void setFalseValue(double falseValue) {
        this.falseValue = falseValue;
    }

    public double getFalseValue() {
        return falseValue;
    }

    @Override
    public double drawDoubleValue() {
        return rnd.nextBoolean() ? trueValue : falseValue;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                name,
                seed,
                rnd.getChecksum(),
                trueValue,
                falseValue
        );
    }
}
