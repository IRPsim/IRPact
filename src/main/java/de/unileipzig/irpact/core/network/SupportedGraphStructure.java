package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.IsEquals;
import de.unileipzig.irpact.commons.graph.DirectedAdjacencyListMultiGraph;
import de.unileipzig.irpact.commons.graph.DirectedMultiGraph;
import de.unileipzig.irpact.commons.graph.FastDirectedMultiGraph;

/**
 * @author Daniel Abitz
 */
public enum SupportedGraphStructure implements IsEquals {
    UNKNOWN(0) {
        @Override
        public <V, E, T> DirectedMultiGraph<V, E, T> newInstance() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
    },
    DIRECTED_ADJACENCY_LIST_MULTI_GRAPH(1) {
        @Override
        public <V, E, T> DirectedAdjacencyListMultiGraph<V, E, T> newInstance() {
            return new DirectedAdjacencyListMultiGraph<>();
        }
    },
    FAST_DIRECTED_MULTI_GRAPH(2) {
        @Override
        public <V, E, T> FastDirectedMultiGraph<V, E, T> newInstance() {
            return new FastDirectedMultiGraph<>();
        }
    };

    private final int ID;

    SupportedGraphStructure(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }

    @Override
    public int getHashCode() {
        return ID;
    }

    public abstract <V, E, T> DirectedMultiGraph<V, E, T> newInstance();

    public static SupportedGraphStructure get(int id) {
        for(SupportedGraphStructure v: values()) {
            if(v.getID() == id) {
                return v;
            }
        }
        return UNKNOWN;
    }
}
