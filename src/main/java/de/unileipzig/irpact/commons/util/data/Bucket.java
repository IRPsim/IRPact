package de.unileipzig.irpact.commons.util.data;

import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public interface Bucket<T> extends Comparable<Bucket<T>> {

    T getFrom();

    T getTo();

    boolean isInside(T value);

    String print(Function<? super T, ? extends String> toStringFunction);
}
