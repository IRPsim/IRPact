package de.unileipzig.irpact.v2.commons.affinity;

/**
 * @param <S>
 * @param <T>
 * @author Daniel Abitz
 */
public interface AffinityMapping<S, T> {

    boolean has(S source);

    boolean has(S source, T target);

    Affinities<T> get(S source);

    double get(S source, T target);

    void put(S source, T target, double value);

    void put(S source, Affinities<T> affinities);
}
