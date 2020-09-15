package de.unileipzig.irpact.experimental.todev.graph;

import java.util.HashMap;
import java.util.Map;

/**
 * @param <V> Vertextyp
 * @param <W> Typ des Kantengewichtes
 * @param <E> Unterschiedliche Graphtypen
 * @author Daniel Abitz
 */
public class DirectedMultiGraph<V, W, E> {

    private final Map<E, DirectedGraph<V, W>> graphs = new HashMap<>();

    public DirectedMultiGraph() {
    }

    //=========================
    //general
    //=========================

    public boolean add(E type, DirectedGraph<V, W> graph) {
        if(graphs.containsKey(type)) {
            return false;
        }
        graphs.put(type, graph);
        return true;
    }

    public DirectedGraph<V, W> get(E type) {
        return graphs.get(type);
    }

    //=========================
    //util
    //=========================

    public void addEdge(V from, V to, W weight, E... types) {
        for(E type: types) {
            DirectedGraph<V, W> graph = get(type);
            graph.addEdge(from, to, weight);
        }
    }
}
