package de.unileipzig.irpact.commons.graph2;

import java.util.Set;
import java.util.stream.Stream;

/**
 * @param <V>
 * @param <E>
 * @param <T>
 * @author Daniel Abitz
 */
public interface MultiGraph2<V, E, T> extends Graph2<V, E> {

    //=========================
    // edges
    //=========================

    Set<E> getEdges(T type);

    void removeAllEdges(T type);

    Stream<E> streamEdges(T type);
}
