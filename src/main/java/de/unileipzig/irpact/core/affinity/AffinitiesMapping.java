package de.unileipzig.irpact.core.affinity;

import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public interface AffinitiesMapping<S, T> {

    boolean has(S source);

    Affinities<T> get(S source);

    void put(S from, Affinities<T> affinities);

    void put(S from, T to, double value);

    default boolean hasValue(S source, T target) {
        Affinities<T> affinities = get(source);
        if(affinities == null) {
            return false;
        }
        return affinities.hasValue(target);
    }

    default double getValue(S source, T target) throws NoSuchElementException {
        Affinities<T> affinities = get(source);
        if(affinities == null) {
            throw new NoSuchElementException();
        }
        return affinities.getValue(target);
    }
}
