package de.unileipzig.irpact.experimental.todev.graph;

import de.unileipzig.irpact.experimental.annotation.ToDevelop;

/**
 * @param <V> Vertextyp, null ist erlaubt und wird bei Aufrufen beruecksichtigt
 * @param <W> Typ der Kantengewichte, null ist erlaubt und wird bei Aufrufen beruecksichtigt
 * @author Daniel Abitz
 */
@ToDevelop
@FunctionalInterface
public interface EdgeConsumer<V, W> {

    void accept(V from, V to, W weight);
}
