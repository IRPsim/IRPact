package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;

/**
 * @author Daniel Abitz
 */
public interface MultiWeightedMapping<S, T> {

    boolean isEmpty();

    int size();

    int size(S source);

    boolean has(S source);

    boolean has(S source, T target);

    double getWeight(S source, T target);

    boolean remove(S source);

    boolean remove(S source, T target);

    void set(S source, T target, double weight);

    void set(S source, WeightedValue<T> value);

    T getRandom(S source, Rnd rnd);

    T getWeightedRandom(S source, Rnd rnd);
}
