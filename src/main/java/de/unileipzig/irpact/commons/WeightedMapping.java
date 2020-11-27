package de.unileipzig.irpact.commons;

import java.util.Random;

/**
 * @param <S>
 * @param <T>
 * @param <W>
 * @author Daniel Abitz
 */
public interface WeightedMapping<S, T, W> {

    boolean has(S source);

    boolean has(S source, T target);

    W get(S source, T target);

    W put(S source, T target, W value);

    boolean remove(S source);

    boolean remove(S source, T target);

    T getRandom(S source, Random rnd);

    T getWeightedRandom(S source, Random rnd);
}
