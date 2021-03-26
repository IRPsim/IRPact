package de.unileipzig.irpact.commons.affinity;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.Rnd;

import java.util.Random;
import java.util.Set;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public interface Affinities<T> extends ChecksumComparable {

    Affinities<T> createWithout(T target);

    Set<T> targets();

    Set<T> accessibleTargets();

    boolean isEmpty();

    int size();

    boolean hasValue(T target);

    double getValue(T target);

    void setValue(T target, double value);

    double sum();

    T getRandom(Random rnd);

    T getWeightedRandom(Random rnd);

    T getRandom(Rnd rnd);

    T getWeightedRandom(Rnd rnd);
}
