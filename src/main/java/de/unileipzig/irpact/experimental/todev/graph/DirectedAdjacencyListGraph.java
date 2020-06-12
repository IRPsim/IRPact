package de.unileipzig.irpact.experimental.todev.graph;

import de.unileipzig.irpact.experimental.annotation.ToDevelop;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Graphimplementierung basierend auf Adjazenzlisten, implementiert mittels Maps.
 *
 * @param <V> Vertextyp
 * @param <W> Typ des Kantengewichtes
 * @author Daniel Abitz
 */
@ToDevelop
public class DirectedAdjacencyListGraph<V, W> implements DirectedGraph<V, W> {

    private final Supplier<? extends Map<V, Map<V, W>>> mainMapSupplier;
    private final Function<? super V, ? extends Map<V, W>> subMapFunction;
    private final Map<V, Map<V, W>> adjacencyMap;
    private int modCount = 0;

    public DirectedAdjacencyListGraph() {
        this(HashMap::new, key -> new HashMap<>());
    }

    public DirectedAdjacencyListGraph(
            Supplier<? extends Map<V, Map<V, W>>> mainMapSupplier,
            Function<? super V, ? extends Map<V, W>> subMapFunction) {
        this.mainMapSupplier = mainMapSupplier;
        this.subMapFunction = subMapFunction;
        adjacencyMap = mainMapSupplier.get();
    }

    public DirectedAdjacencyListGraph(DirectedAdjacencyListGraph<V, W> other) {
        this(other.mainMapSupplier, other.subMapFunction);
        for(Map.Entry<V, Map<V, W>> otherEntry: other.adjacencyMap.entrySet()) {
            Map<V, W> thisSub = adjacencyMap.computeIfAbsent(otherEntry.getKey(), subMapFunction);
            thisSub.putAll(otherEntry.getValue());
        }
        this.modCount = 0;
    }

    //=========================
    //util
    //=========================

    private void init(V from, V to) {
        init(from);
        init(to);
    }

    private void init(V vertex) {
        if(!adjacencyMap.containsKey(vertex)) {
            incModCount();
            adjacencyMap.put(vertex, subMapFunction.apply(vertex));
        }
    }

    private void incModCount() {
        modCount++;
    }

    //=========================
    //Graph
    //=========================

    @Override
    public DirectedAdjacencyListGraph<V, W> copy() {
        return new DirectedAdjacencyListGraph<>(this);
    }

    @Override
    public int vertexCount() {
        return adjacencyMap.size();
    }

    @Override
    public void addVertex(V vertex) {
        init(vertex);
    }

    @Override
    public boolean hasVertex(V vertex) {
        return adjacencyMap.containsKey(vertex);
    }

    @Override
    public boolean removeVertex(V vertex) {
        if(!hasVertex(vertex)) {
            return false;
        }
        incModCount();
        adjacencyMap.remove(vertex);
        for(Map<V, W> toMap: adjacencyMap.values()) {
            toMap.remove(vertex);
        }
        return true;
    }

    @Override
    public void forEachNode(Consumer<? super V> consumer) {
        adjacencyMap.keySet().forEach(consumer);
    }

    @Override
    public int edgeCount() {
        return adjacencyMap.values()
                .stream()
                .mapToInt(Map::size)
                .sum();
    }

    @Override
    public boolean addEdge(V from, V to, W weight) {
        if(hasEdge(from, to)) {
            return false;
        }
        setEdge(from, to, weight);
        return true;
    }

    @Override
    public W setEdge(V from, V to, W weight) {
        incModCount();
        init(from, to);
        Map<V, W> toMap = adjacencyMap.get(from);
        return toMap.put(to, weight);
    }

    @Override
    public boolean hasEdge(V from, V to) {
        Map<V, W> toMap = adjacencyMap.get(from);
        return toMap != null && toMap.containsKey(to);
    }

    @Override
    public void changeWeight(V from, V to, W weight) throws NoSuchElementException {
        if(!hasEdge(from, to)) {
            throw new NoSuchElementException();
        }
        incModCount();
        Map<V, W> toMap = adjacencyMap.get(from);
        toMap.put(to, weight);
    }

    @Override
    public W getWeight(V from, V to) {
        if(!hasEdge(from, to)) {
            return null;
        }
        Map<V, W> toMap = adjacencyMap.get(from);
        return toMap.get(to);
    }

    @Override
    public W removeEdge(V from, V to) {
        if(!hasEdge(from, to)) {
            return null;
        }
        incModCount();
        Map<V, W> toMap = adjacencyMap.get(from);
        return toMap.remove(to);
    }

    @Override
    public void forEachEdge(EdgeConsumer<? super V, ? super W> consumer) {
        for(Map.Entry<V, Map<V, W>> fromEntry: adjacencyMap.entrySet()) {
            for(Map.Entry<V, W> toEntry: fromEntry.getValue().entrySet()) {
                consumer.accept(
                        fromEntry.getKey(),
                        toEntry.getKey(),
                        toEntry.getValue()
                );
            }
        }
    }

    @Override
    public Iterator<V> iterateNeighbours(V from) {
        return iterateNeighbours(from, true, true);
    }

    @Override
    public Iterator<V> iterateNeighbours(V from, boolean directed, boolean distinct) {
        return new NeighborIterator(from, directed, distinct);
    }

    @Override
    public Iterable<V> iterableNeighbours(V from) {
        return iterableNeighbours(from, true, true);
    }

    @Override
    public Iterable<V> iterableNeighbours(V from, boolean directed, boolean distinct) {
        return () -> iterateNeighbours(from, directed, distinct);
    }

    @Override
    public Stream<V> streamNeighbours(V from) {
        return streamNeighbours(from, true, true);
    }

    @Override
    public Stream<V> streamNeighbours(V from, boolean directed, boolean distinct) {
        int c = 0;
        if(distinct) {
            c |= Spliterator.DISTINCT;
        }
        Spliterator<V> spliterator = Spliterators.spliteratorUnknownSize(
                iterateNeighbours(from, directed, distinct),
                c
        );
        return StreamSupport.stream(spliterator, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final boolean hasPath(V... path) {
        return hasPath(Arrays.asList(path));
    }

    @Override
    public boolean hasPath(Collection<? extends V> path) {
        V from = null;
        V to;
        boolean first = true;
        for(V node: path) {
            if(first) {
                from = node;
                first = false;
            } else {
                to = node;
                if(!hasEdge(from, to)) {
                    return false;
                }
                from = to;
            }
        }
        return true;
    }

    //=========================
    //helper
    //=========================

    private class NeighborIterator implements Iterator<V> {

        private final int currentModCount = modCount;
        private final V vertex;
        private final boolean directed;
        private final Iterator<Map.Entry<V, Map<V, W>>> mainIter = adjacencyMap.entrySet().iterator();
        private final Iterator<V> directIter;
        private final Set<V> processed;
        private V next;

        private NeighborIterator(V vertex, boolean directed, boolean distinct) {
            this.vertex = vertex;
            this.directed = directed;
            if(distinct && !directed) {
                processed = new HashSet<>();
            } else {
                processed = null;
            }
            Map<V, W> directMap = adjacencyMap.get(vertex);
            if(directMap != null) {
                directIter = directMap.keySet().iterator();
            } else {
                directIter = null;
            }
        }

        private void checkModCount() throws ConcurrentModificationException {
            if(modCount != currentModCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            boolean hasNext = handleHasNext();
            if(!hasNext && processed != null) {
                processed.clear();
            }
            return hasNext;
        }

        private boolean handleHasNext() {
            if(next != null) {
                return true;
            }
            if(isFinished()) {
                return false;
            }
            checkModCount();
            return directed
                    ? hasNextDirected()
                    : hasNextUndirected();
        }

        private boolean isFinished() {
            return (directIter == null || !directIter.hasNext()) && !mainIter.hasNext();
        }

        private boolean notProcessed(V vertex) {
            return processed == null || !processed.contains(vertex);
        }

        private void addProcessed(V vertex) {
            if(processed != null) {
                processed.add(vertex);
            }
        }

        private void setNext(V vertex) {
            next = vertex;
            addProcessed(vertex);
        }

        private boolean hasNextDirected() {
            if(directIter == null || !directIter.hasNext()) {
                return false;
            }
            setNext(directIter.next());
            return true;
        }

        private boolean hasNextUndirected() {
            if(hasNextDirected()) {
                return true;
            } else {
                boolean foundNext = false;
                while(mainIter.hasNext() && !foundNext) {
                    Map.Entry<V, Map<V, W>> entry = mainIter.next();
                    if(entry.getKey() != vertex
                            && entry.getValue().containsKey(vertex)
                            && notProcessed(entry.getKey())) {
                        setNext(entry.getKey());
                        foundNext = true;
                    }
                }
                return foundNext;
            }
        }

        @Override
        public V next() {
            if(hasNext()) {
                V temp = next;
                next = null;
                return temp;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}