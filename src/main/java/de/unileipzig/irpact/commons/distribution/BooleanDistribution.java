package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.Rnd;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BooleanDistribution extends NameableBase implements UnivariateDoubleDistribution {

    protected long seed;
    protected Rnd rnd;

    public BooleanDistribution() {
    }

    public BooleanDistribution(String name, Rnd rnd) {
        setName(name);
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
        return rnd.nextBoolean() ? 1.0 : 0.0;
    }

    @Override
    public int getHashCode() {
        return Objects.hash(name, seed, rnd.getHashCode());
    }
}
