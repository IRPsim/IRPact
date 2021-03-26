package de.unileipzig.irpact.commons.affinity;

import de.unileipzig.irpact.commons.ChecksumComparable;

import java.util.Set;

/**
 * @param <S>
 * @param <T>
 * @author Daniel Abitz
 */
public interface AffinityMapping<S, T> extends ChecksumComparable {

    Set<S> sources();

    boolean has(S source);

    boolean has(S source, T target);

    Affinities<T> get(S source);

    double get(S source, T target);

    void put(S source, T target, double value);

    void put(S source, Affinities<T> affinities);
}
