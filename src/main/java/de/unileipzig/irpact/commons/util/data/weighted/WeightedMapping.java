package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.weighted.WeightedValue;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public interface WeightedMapping<T> {

    boolean isEmpty();

    boolean has(T target);

    double getWeight(T target);

    boolean remove(T target);

    void set(T target, double weight);

    void set(WeightedValue<T> value);

    T getRandom(Rnd rnd);

    T getWeightedRandom(Rnd rnd);
}
