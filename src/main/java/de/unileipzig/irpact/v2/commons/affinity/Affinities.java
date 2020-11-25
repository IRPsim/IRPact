package de.unileipzig.irpact.v2.commons.affinity;

import java.util.Random;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public interface Affinities<T> {

    int size();

    boolean hasValue(T target);

    double getValue(T target);

    void setValue(T target, double value);

    double sum();

    T getRandom(Random rnd);

    T getWeightedRandom(Random rnd);
}
