package de.unileipzig.irpact.commons.affinity;

import java.util.Random;
import java.util.Set;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public interface Affinities<T> {

    Set<T> targets();

    int size();

    boolean hasValue(T target);

    double getValue(T target);

    void setValue(T target, double value);

    double sum();

    T getRandom(Random rnd);

    T getWeightedRandom(Random rnd);
}
