package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.Rnd;

/**
 * Bernoulli: P(X=trueValue) = p and P(X=falseValue) = 1-p
 *
 * @author Daniel Abitz
 */
public class BernoulliDistribution extends NameableBase implements UnivariateDoubleDistribution {

    protected Rnd rnd;
    protected double p;
    protected double trueValue;
    protected double falseValue;

    public BernoulliDistribution() {
    }

    public BernoulliDistribution(String name, Rnd rnd, double p) {
        this(name, rnd, p, 1, 0);
    }

    public BernoulliDistribution(String name, Rnd rnd, double p, double trueValue, double falseValue) {
        setName(name);
        setRandom(rnd);
        setP(p);
        setTrueValue(trueValue);
        setFalseValue(falseValue);
    }

    public void setRandom(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRandom() {
        return rnd;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getP() {
        return p;
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
        return rnd.nextDouble() < p ? trueValue : falseValue;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                name,
                rnd.getChecksum(),
                p,
                trueValue,
                falseValue
        );
    }
}
