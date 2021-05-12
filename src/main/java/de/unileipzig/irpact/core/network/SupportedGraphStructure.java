package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.graph.DirectedAdjacencyListMultiGraph;
import de.unileipzig.irpact.commons.graph.DirectedMultiGraph;
import de.unileipzig.irpact.commons.graph.FastDirectedMultiGraph;
import de.unileipzig.irpact.commons.graph.FastDirectedMultiGraph2;

import java.util.Collections;
import java.util.LinkedHashMap;

/**
 * @author Daniel Abitz
 */
public enum SupportedGraphStructure implements ChecksumComparable {
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
    },
    FAST_DIRECTED_MULTI_GRAPH2(3) {
        @Override
        public <V, E, T> FastDirectedMultiGraph2<V, E, T> newInstance() {
            return new FastDirectedMultiGraph2<>();
        }
    },
    FAST_DIRECTED_MULTI_GRAPH2_CONCURRENT(4) {
        @Override
        public <V, E, T> FastDirectedMultiGraph2<V, E, T> newInstance() {
            return new FastDirectedMultiGraph2<>(
                    () -> Collections.synchronizedMap(new LinkedHashMap<>()),
                    () -> Collections.synchronizedMap(new LinkedHashMap<>()),
                    () -> Collections.synchronizedMap(new LinkedHashMap<>()),
                    t -> Collections.synchronizedMap(new LinkedHashMap<>())
            );
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
    public int getChecksum() {
        return ID;
    }

    public abstract <V, E, T> DirectedMultiGraph<V, E, T> newInstance();

    public static SupportedGraphStructure get(int id) {
        for(SupportedGraphStructure v: values()) {
            if(v.getID() == id) {
                return v;
            }
        }
        throw new IllegalArgumentException("unknown id: " + id);
    }

    public static SupportedGraphStructure getDefault() {
        return FAST_DIRECTED_MULTI_GRAPH2_CONCURRENT;
    }
}
