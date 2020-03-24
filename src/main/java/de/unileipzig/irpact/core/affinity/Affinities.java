package de.unileipzig.irpact.core.affinity;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
public interface Affinities<T> {

    boolean hasValue(T target);

    double getValue(T target);

    void setValue(T target, double value);

    T getRandom(Random rnd);
}
