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
public class FastDirectedMultiGraphStructure<N extends Node, E extends Edge<N>, T>
        implements DirectedMultiGraphDataStructure<N, E, T> {

    protected Supplier<? extends Set<N>> nodeSetSupplier;
    protected Supplier<? extends Set<E>> edgeSetSupplier;
    protected Supplier<? extends Map<T, Set<N>>> typeSubNodeMapSupplier;
    protected Supplier<? extends Map<T, Set<E>>> typeSubEdgeMapSupplier;
    protected Supplier<? extends Map<N, Map<T, E>>> typeNodeEdgeSupplier;
    protected Supplier<? extends Map<T, E>> typeEdgeSupplier;
    protected Set<N> nodes;
    protected Map<T, Set<E>> typeEdges;
    protected Map<N, Map<T, Set<E>>> typeOutEdges;
    protected Map<N, Map<T, Set<E>>> typeInEdges;
    protected Map<N, Map<N, Map<T, E>>> typeNodeNodeEdge;

    public FastDirectedMultiGraphStructure(
            Supplier<? extends Set<N>> nodeSetSupplier,
            Supplier<? extends Set<E>> edgeSetSupplier,
            Supplier<? extends Map<T, Set<N>>> typeSubNodeMapSupplier,
            Supplier<? extends Map<T, Set<E>>> typeSubEdgeMapSupplier,
            Supplier<? extends Map<N, Map<T, E>>> typeNodeEdgeSupplier,
            Supplier<? extends Map<T, E>> typeEdgeSupplier,
            Set<N> nodes,
            Map<T, Set<E>> typeEdges,
            Map<N, Map<T, Set<E>>> typeOutEdges,
            Map<N, Map<T, Set<E>>> typeInEdges,
            Map<N, Map<N, Map<T, E>>> typeNodeNodeEdge) {
        this.nodeSetSupplier = nodeSetSupplier;
        this.edgeSetSupplier = edgeSetSupplier;
        this.typeSubNodeMapSupplier = typeSubNodeMapSupplier;
        this.typeSubEdgeMapSupplier = typeSubEdgeMapSupplier;
        this.typeNodeEdgeSupplier = typeNodeEdgeSupplier;
        this.typeEdgeSupplier = typeEdgeSupplier;
        this.nodes = nodes;
        this.typeEdges = typeEdges;
        this.typeOutEdges = typeOutEdges;
        this.typeInEdges = typeInEdges;
        this.typeNodeNodeEdge = typeNodeNodeEdge;
    }

    protected boolean scanNode(N node) {
        if(nodes.contains(node)) return true;
        if(typeOutEdges.containsKey(node)) return true;
        if(typeInEdges.containsKey(node)) return true;
        if(typeNodeNodeEdge.containsKey(node)) return true;
        for(Map<N, Map<T, E>> map: typeNodeNodeEdge.values()) {
            if(map.containsKey(node)) {
                return true;
            }
        }
        return false;
    }

    protected boolean scanEdge(E edge) {
        for(Set<E> set: typeEdges.values()) {
            if(set.contains(edge)) {
                return true;
            }
        }
        for(Map<T, Set<E>> map: typeOutEdges.values()) {
            for(Set<E> set: map.values()) {
                if(set.contains(edge)) {
                    return true;
                }
            }
        }
        for(Map<T, Set<E>> map: typeInEdges.values()) {
            for(Set<E> set: map.values()) {
                if(set.contains(edge)) {
                    return true;
                }
            }
        }
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
        Set<E> set = typeEdges.get(type);
        if(set != null && set.contains(edge)) {
            return true;
        }
        for(Map<T, Set<E>> map: typeOutEdges.values()) {
            Set<E> set1 = map.get(type);
            if(set1 != null && set1.contains(edge)) {
                return true;
            }
        }
        for(Map<T, Set<E>> map: typeInEdges.values()) {
            Set<E> set1 = map.get(type);
            if(set1 != null && set1.contains(edge)) {
                return true;
            }
        }
        for(Map<N, Map<T, E>> map: typeNodeNodeEdge.values()) {
            for(Map<T, E> map1: map.values()) {
                E e = map1.get(type);
                return e == edge;
            }
        }
        return false;
    }

    @Override
    public Collection<? extends N> getNodes() {
        return nodes;
    }

    @Override
    public Collection<? extends E> getEdges(T type) {
        return typeEdges.get(type);
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
        Map<T, Set<E>> map = typeInEdges.get(node);
        if(map == null) {
            return -1;
        }
        Set<E> set = map.get(type);
        return set == null
                ? 0
                : set.size();
    }

    @Override
    public int getOutdegree(N node, T type) {
        Map<T, Set<E>> map = typeOutEdges.get(node);
        if(map == null) {
            return -1;
        }
        Set<E> set = map.get(type);
        return set == null
                ? 0
                : set.size();
    }

    @Override
    public Collection<? extends E> getOutEdges(N node, T type) {
        Map<T, Set<E>> map = typeOutEdges.get(node);
        if(map == null) {
            return null;
        }
        return map.get(type);
    }

    @Override
    public Collection<? extends E> getInEdges(N node, T type) {
        Map<T, Set<E>> map = typeInEdges.get(node);
        if(map == null) {
            return null;
        }
        return map.get(type);
    }

    @Override
    public boolean hasNode(N node) {
        return nodes.contains(node);
    }

    @Override
    public boolean hasEdge(E edge, T type) {
        Set<E> edges = typeEdges.get(type);
        return edges != null && edges.contains(edge);
    }

    protected boolean addIfNotExists(N node) {
        return nodes.add(node);
    }

    @Override
    public void addNode(N node) throws NodeAlreadyExistsException {
        if(!addIfNotExists(node)) {
            throw new NodeAlreadyExistsException();
        }
    }

    protected void addEdgeTo(N node, E edge, T type, boolean out) {
        Map<T, Set<E>> edgeMap = out
                ? typeOutEdges.computeIfAbsent(node, _node -> typeSubEdgeMapSupplier.get())
                : typeInEdges.computeIfAbsent(node, _node -> typeSubEdgeMapSupplier.get());
        Set<E> edges = edgeMap.computeIfAbsent(type, _type -> edgeSetSupplier.get());
        edges.add(edge);
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
        addEdgeTo(source, edge, type, true);
        addEdgeTo(target, edge, type, false);
        Set<E> edges = typeEdges.computeIfAbsent(type, _type -> edgeSetSupplier.get());
        edges.add(edge);
        Map<N, Map<T, E>> map = typeNodeNodeEdge.computeIfAbsent(source, _source -> typeNodeEdgeSupplier.get());
        Map<T, E> map1 = map.computeIfAbsent(target, _target -> typeEdgeSupplier.get());
        map1.put(type, edge);
    }

    protected void removeAllEdges(Map<T, Set<E>> edges) {
        if(edges == null) {
            return;
        }
        for(Map.Entry<T, Set<E>> edgesEntry: edges.entrySet()) {
            Set<E> thisEdges = typeEdges.get(edgesEntry.getKey());
            if(thisEdges != null) {
                thisEdges.removeAll(edgesEntry.getValue());
            }
        }
    }

    @Override
    public boolean removeNode(N node) {
        if(nodes.remove(node)) {
            removeAllEdges(typeOutEdges.remove(node));
            removeAllEdges(typeInEdges.remove(node));
            typeNodeNodeEdge.remove(node);
            for(Map<N, Map<T, E>> map: typeNodeNodeEdge.values()) {
                map.remove(node);
            }
            return true;
        }
        return false;
    }

    protected void removeEdge(Map<N, Map<T, Set<E>>> edgeMap, N node, E edge, T type) {
        Map<T, Set<E>> map = edgeMap.get(node);
        if(map != null) {
            Set<E> set = map.get(type);
            if(set != null) {
                set.remove(edge);
            }
        }
    }

    @Override
    public boolean removeEdge(E edge, T type) {
        Set<E> edges = typeEdges.get(type);
        if(edges != null && edges.remove(edge)) {
            removeEdge(typeOutEdges, edge.getSource(), edge, type);
            removeEdge(typeInEdges, edge.getTarget(), edge, type);
            Map<N, Map<T, E>> map = typeNodeNodeEdge.get(edge.getSource());
            if(map != null) {
                Map<T, E> map1 = map.get(edge.getTarget());
                if(map1 != null) {
                    map1.remove(type);
                }
            }
            return true;
        }
        return false;
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
