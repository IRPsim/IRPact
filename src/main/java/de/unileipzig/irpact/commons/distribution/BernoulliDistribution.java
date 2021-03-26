package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.util.Todo;

import java.util.Objects;

/**
 * Bernoulli: P(X=1) = p and P(X=0) = 1-p
 *
 * @author Daniel Abitz
 */
@Todo("spec + persi adden")
public class BernoulliDistribution extends NameableBase implements UnivariateDoubleDistribution {

    protected long seed;
    protected Rnd rnd;
    protected double p;

    public BernoulliDistribution() {
    }

    public BernoulliDistribution(String name, Rnd rnd, double p) {
        setName(name);
        setRandom(rnd);
        setP(p);
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

    @Override
    public double drawDoubleValue() {
        return rnd.nextDouble() < p ? 1.0 : 0.0;
    }

    @Override
    public int getChecksum() {
        return Objects.hash(name, seed, rnd.getChecksum(), p);
    }
}
