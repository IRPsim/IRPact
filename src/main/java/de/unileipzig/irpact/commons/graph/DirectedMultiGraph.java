package de.unileipzig.irpact.commons.graph;

import java.io.IOException;
import java.util.function.Function;

/**
 * @param <V>
 * @param <E>
 * @param <T>
 * @author Daniel Abitz
 */
public interface DirectedMultiGraph<V, E, T> extends MultiGraph<V, E, T> {

    void print(
            Appendable target,
            Function<? super V, ? extends String> vToString,
            Function<? super E, ? extends String> eToString,
            Function<? super T, ? extends String> tToString) throws IOException;
}
