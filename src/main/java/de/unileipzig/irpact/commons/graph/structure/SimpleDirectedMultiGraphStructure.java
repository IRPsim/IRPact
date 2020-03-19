package de.unileipzig.irpact.commons.graph.structure;

import de.unileipzig.irpact.commons.annotation.ToDo;
import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;
import de.unileipzig.irpact.commons.graph.Edge;
import de.unileipzig.irpact.commons.graph.Node;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
@ToDo("vllt auf emptyCollection statt null ausweichen oder weitere methode (find) nutzen")
public class SimpleDirectedMultiGraphStructure<N extends Node, E extends Edge<N>, T>
        implements DirectedMultiGraphDataStructure<N, E, T> {

    protected Supplier<? extends Map<N, Map<T, E>>> typeNodeEdgeSupplier;
    protected Supplier<? extends Map<T, E>> typeEdgeSupplier;
    protected Supplier<? extends Set<E>> edgeSetSupplier;
    protected Map<N, Map<N, Map<T, E>>> typeNodeNodeEdge;

    public SimpleDirectedMultiGraphStructure(
            Supplier<? extends Map<N, Map<T, E>>> typeNodeEdgeSupplier,
            Supplier<? extends Map<T, E>> typeEdgeSupplier,
            Supplier<? extends Set<E>> edgeSetSupplier,
            Map<N, Map<N, Map<T, E>>> typeNodeNodeEdge) {
        this.typeNodeEdgeSupplier = typeNodeEdgeSupplier;
        this.typeEdgeSupplier = typeEdgeSupplier;
        this.edgeSetSupplier = edgeSetSupplier;
        this.typeNodeNodeEdge = typeNodeNodeEdge;
    }

    protected boolean scanNode(N node) {
        if(typeNodeNodeEdge.containsKey(node)) return true;
        for(Map<N, Map<T, E>> map: typeNodeNodeEdge.values()) {
            if(map.containsKey(node)) {
                return true;
            }
        }
        return false;
    }

    protected boolean scanEdge(E edge) {
        for(Map<N, Map<T, E>> map: typeNodeNodeEdge.values()) {
            for(Map<T, E> map1: map.values()) {
                if(map1.containsValue(edge)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean scanEdge(E edge, T type) {
        for(Map<N, Map<T, E>> map: typeNodeNodeEdge.values()) {
            for(Map<T, E> map1: map.values()) {
                E e = map1.get(type);
                if(e != null && e == edge) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Collection<? extends N> getNodes() {
        return typeNodeNodeEdge.keySet();
    }

    @Override
    public Collection<? extends E> getEdges(T type) {
        Set<E> edges = edgeSetSupplier.get();
        for(Map<N, Map<T, E>> map: typeNodeNodeEdge.values()) {
            for(Map<T, E> map1: map.values()) {
                E edge = map1.get(type);
                if(edge != null) {
                    edges.add(edge);
                }
            }
        }
        return edges;
    }

    @Override
    public int getDegree(N node, T type) {
        int in = getIndegree(node, type);
        int out = getOutdegree(node, type);
        if(in == -1 && out == -1) {
            return -1;
        }
        return (in == -1 ? 0 : in)
                + (out == -1 ? 0 : out);
    }

    @Override
    public int getIndegree(N node, T type) {
        if(!hasNode(node)) {
            return -1;
        }
        int count = 0;
        for(Map<N, Map<T, E>> map: typeNodeNodeEdge.values()) {
            Map<T, E> map1 = map.get(node);
            if(map1 != null) {
                E e = map1.get(type);
                if(e != null) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public int getOutdegree(N node, T type) {
        Map<N, Map<T, E>> map = typeNodeNodeEdge.get(node);
        if(map == null) {
            return -1;
        }
        int count = 0;
        for(Map<T, E> map1: map.values()) {
            if(map1.containsKey(type)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Collection<? extends E> getInEdges(N node, T type) {
        if(!hasNode(node)) {
            return null;
        }
        Set<E> edges = edgeSetSupplier.get();
        for(Map<N, Map<T, E>> map: typeNodeNodeEdge.values()) {
            Map<T, E> map1 = map.get(node);
            if(map1 != null) {
                E e = map1.get(type);
                if(e != null) {
                    edges.add(e);
                }
            }
        }
        return edges;
    }

    @Override
    public Collection<? extends E> getOutEdges(N node, T type) {
        Map<N, Map<T, E>> map = typeNodeNodeEdge.get(node);
        if(map == null) {
            return null;
        }
        Set<E> edges = edgeSetSupplier.get();
        for(Map<T, E> map1: map.values()) {
            E e = map1.get(type);
            if(e != null) {
                edges.add(e);
            }
        }
        return edges;
    }

    @Override
    public boolean hasNode(N node) {
        return typeNodeNodeEdge.containsKey(node);
    }

    @Override
    public boolean hasEdge(N source, N target, T type) {
        return getEdge(source, target, type) != null;
    }

    @Override
    public boolean hasEdge(E edge, T type) {
        return getEdge(edge.getSource(), edge.getTarget(), type) != null;
    }

    protected boolean addIfNotExists(N node) {
        if(typeNodeNodeEdge.containsKey(node)) {
            return false;
        }
        typeNodeNodeEdge.put(node, typeNodeEdgeSupplier.get());
        return true;
    }

    @Override
    public void addNode(N node) throws NodeAlreadyExistsException {
        if(!addIfNotExists(node)) {
            throw new NodeAlreadyExistsException();
        }
    }

    @Override
    public void addEdge(E edge, T type) throws EdgeAlreadyExistsException {
        if(hasEdge(edge, type)) {
            throw new EdgeAlreadyExistsException();
        }
        N source = edge.getSource();
        N target = edge.getTarget();
        addIfNotExists(source);
        addIfNotExists(target);
        Map<N, Map<T, E>> map = typeNodeNodeEdge.computeIfAbsent(source, _source -> typeNodeEdgeSupplier.get());
        Map<T, E> map1 = map.computeIfAbsent(target, _target -> typeEdgeSupplier.get());
        map1.put(type, edge);
    }

    @Override
    public boolean removeNode(N node) {
        if(typeNodeNodeEdge.remove(node) != null) {
            for(Map<N, Map<T, E>> map: typeNodeNodeEdge.values()) {
                map.remove(node);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean removeEdge(E edge, T type) {
        N source = edge.getSource();
        N target = edge.getTarget();
        Map<N, Map<T, E>> map = typeNodeNodeEdge.get(source);
        if(map == null) {
            return false;
        }
        Map<T, E> map1 = map.get(target);
        if(map1 == null) {
            return false;
        }
        return map1.remove(type) != null;
    }

    @Override
    public E getEdge(N source, N target, T type) {
        Map<N, Map<T, E>> map = typeNodeNodeEdge.get(source);
        if(map == null) {
            return null;
        }
        Map<T, E> map1 = map.get(target);
        if(map1 == null) {
            return null;
        }
        return map1.get(type);
    }
}
