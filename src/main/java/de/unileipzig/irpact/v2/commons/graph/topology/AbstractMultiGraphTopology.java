package de.unileipzig.irpact.v2.commons.graph.topology;

import de.unileipzig.irpact.v2.commons.TriFunction;
import de.unileipzig.irpact.v2.commons.graph.Graph;
import de.unileipzig.irpact.v2.commons.graph.MultiGraph;
import de.unileipzig.irpact.v2.commons.graph.SingleGraph;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * @param <V>
 * @param <E>
 * @param <T>
 * @author Daniel Abitz
 */
public abstract class AbstractMultiGraphTopology<V, E, T> implements GraphTopology<V, E> {

    protected BiFunction<? super V, ? super V, ? extends E> singleEdgeCreatorFunction;
    protected TriFunction<? super V, ? super V, ? super T, ? extends E> multiEdgeCreatorFunction;
    protected BiConsumer<? super E, ? super Double> initialWeightConsumer;
    protected T edgeType;
    protected double initialWeight;

    public AbstractMultiGraphTopology() {
    }

    public void setEdgeType(T edgeType) {
        this.edgeType = edgeType;
    }

    public void setSingleEdgeCreatorFunction(BiFunction<? super V, ? super V, ? extends E> singleEdgeCreatorFunction) {
        this.singleEdgeCreatorFunction = singleEdgeCreatorFunction;
    }

    public void setMultiEdgeCreatorFunction(TriFunction<? super V, ? super V, ? super T, ? extends E> multiEdgeCreatorFunction) {
        this.multiEdgeCreatorFunction = multiEdgeCreatorFunction;
    }

    public void setInitialWeightConsumer(BiConsumer<? super E, ? super Double> setInitialWeightConsumer) {
        this.initialWeightConsumer = setInitialWeightConsumer;
    }

    public void setInitialWeight(double initialWeight) {
        this.initialWeight = initialWeight;
    }

    protected int getOutDegree(Graph<V, E> graph, V vertex) {
        if(graph instanceof SingleGraph) {
            SingleGraph<V, E> sGraph = (SingleGraph<V, E>) graph;
            return sGraph.outDegree(vertex);
        } else {
            MultiGraph<V, E, T> mGraph = (MultiGraph<V, E, T>) graph;
            return mGraph.outDegree(vertex, edgeType);
        }
    }

    protected int getInDegree(Graph<V, E> graph, V vertex) {
        if(graph instanceof SingleGraph) {
            SingleGraph<V, E> sGraph = (SingleGraph<V, E>) graph;
            return sGraph.inDegree(vertex);
        } else {
            MultiGraph<V, E, T> mGraph = (MultiGraph<V, E, T>) graph;
            return mGraph.inDegree(vertex, edgeType);
        }
    }

    protected int getDegree(Graph<V, E> graph, V vertex) {
        if(graph instanceof SingleGraph) {
            SingleGraph<V, E> sGraph = (SingleGraph<V, E>) graph;
            return sGraph.degree(vertex);
        } else {
            MultiGraph<V, E, T> mGraph = (MultiGraph<V, E, T>) graph;
            return mGraph.degree(vertex, edgeType);
        }
    }

    protected boolean setInitialWeight(E edge) {
        if(initialWeightConsumer == null) {
            return false;
        } else {
            initialWeightConsumer.accept(edge, initialWeight);
            return true;
        }
    }

    protected Set<E> getEdges(Graph<V, E> graph) {
        if(graph instanceof SingleGraph) {
            SingleGraph<V, E> sGraph = (SingleGraph<V, E>) graph;
            return sGraph.getEdges();
        } else {
            MultiGraph<V, E, T> mGraph = (MultiGraph<V, E, T>) graph;
            return mGraph.getEdges(edgeType);
        }
    }

    protected Set<V> getTargets(Graph<V, E> graph, V source) {
        if(graph instanceof SingleGraph) {
            SingleGraph<V, E> sGraph = (SingleGraph<V, E>) graph;
            return sGraph.getTargets(source);
        } else {
            MultiGraph<V, E, T> mGraph = (MultiGraph<V, E, T>) graph;
            return mGraph.getTargets(source, edgeType);
        }
    }

    protected boolean hasEdge(Graph<V, E> graph, V from, V to) {
        if(graph instanceof SingleGraph) {
            SingleGraph<V, E> sGraph = (SingleGraph<V, E>) graph;
            return sGraph.hasEdge(from, to);
        } else {
            MultiGraph<V, E, T> mGraph = (MultiGraph<V, E, T>) graph;
            return mGraph.hasEdge(from, to, edgeType);
        }
    }

    protected boolean removeEdge(Graph<V, E> graph, V from, V to) {
        if(graph instanceof SingleGraph) {
            SingleGraph<V, E> sGraph = (SingleGraph<V, E>) graph;
            return sGraph.removeEdge(from, to);
        } else {
            MultiGraph<V, E, T> mGraph = (MultiGraph<V, E, T>) graph;
            return mGraph.removeEdge(from, to, edgeType);
        }
    }

    protected E addEdge(Graph<V, E> graph, V from, V to) {
        E edge;
        if(graph instanceof SingleGraph) {
            SingleGraph<V, E> sGraph = (SingleGraph<V, E>) graph;
            edge = Objects.requireNonNull(singleEdgeCreatorFunction).apply(from, to);
            sGraph.addEdge(from, to, edge);
        } else {
            MultiGraph<V, E, T> mGraph = (MultiGraph<V, E, T>) graph;
            edge = Objects.requireNonNull(multiEdgeCreatorFunction).apply(from, to, edgeType);
            mGraph.addEdge(from, to, edgeType, edge);
        }
        setInitialWeight(edge);
        return edge;
    }
}
