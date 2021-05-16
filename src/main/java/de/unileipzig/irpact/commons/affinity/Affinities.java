package de.unileipzig.irpact.commons.affinity;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.util.Rnd;

import java.util.Collection;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public interface Affinities<T> extends ChecksumComparable {

    Affinities<T> createWithout(T target);

    Collection<T> targets();

    boolean isEmpty();

    int size();

    boolean hasValue(T target);

    boolean remove(T target);

    double getValue(T target);

    void setValue(T target, double value);

    double sum();

    T getRandom(Rnd rnd);

    T getWeightedRandom(Rnd rnd);
}
