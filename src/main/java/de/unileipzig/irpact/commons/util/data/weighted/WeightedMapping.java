package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;

import java.util.Collection;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public interface WeightedMapping<T> {

    WeightedMapping<T> copy();

    WeightedMapping<T> copyWithout(T toRemove);

    void clear();

    Collection<T> elements();

    boolean isEmpty();

    int size();

    boolean allowsZeroWeight();

    boolean has(T target);

    double getWeight(T target);

    boolean remove(T target);

    default boolean removeAll(Collection<? extends T> targets) {
        boolean changed = false;
        for(T t: targets) {
            changed |= remove(t);
        }
        return changed;
    }

    double totalWeight();

    void set(T target, double weight);

    void set(WeightedValue<T> value);

    T getRandom(Rnd rnd);

    T getWeightedRandom(Rnd rnd);
}
