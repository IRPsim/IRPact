package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.graph.DirectedAdjacencyListMultiGraph;
import de.unileipzig.irpact.commons.graph.DirectedMultiGraph;
import de.unileipzig.irpact.core.agent.Agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class BasicSocialGraph implements SocialGraph {

    /**
     * @author Daniel Abitz
     */
    private static class BasicNode implements Node {

        private final Agent AGENT;

        public BasicNode(Agent agent) {
            AGENT = agent;
        }

        @Override
        public String getLabel() {
            return AGENT.getName();
        }

        @Override
        public Agent getAgent() {
            return AGENT;
        }

        @Override
        public <T extends Agent> T getAgent(Class<T> type) {
            return type.cast(AGENT);
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj) return true;
            if(!(obj instanceof BasicNode)) return false;
            BasicNode other = (BasicNode) obj;
            return Objects.equals(AGENT, other.AGENT);
        }

        @Override
        public int hashCode() {
            return Objects.hash(AGENT);
        }
    }

    /**
     * @author Daniel Abitz
     */
    private static class BasicEdge implements Edge {

        private Node source;
        private Node target;
        private double weight;

        public BasicEdge() {
        }

        @Override
        public void setSource(Node source) {
            this.source = source;
        }

        @Override
        public Node getSource() {
            return source;
        }

        @Override
        public void setTarget(Node target) {
            this.target = target;
        }

        @Override
        public Node getTarget() {
            return target;
        }

        @Override
        public void setWeight(double weight) {
            this.weight = weight;
        }

        @Override
        public double getWeight() {
            return weight;
        }
    }

    private final Function<? super Agent, ? extends Node> NODE_SUPPLIER;
    private final Supplier<? extends Edge> EDGE_SUPPLIER;
    private final DirectedMultiGraph<Node, Edge, Type> GRAPH;
    private final Map<Agent, Node> NODE_CACHE = new HashMap<>();

    public BasicSocialGraph() {
        this(BasicNode::new, BasicEdge::new, new DirectedAdjacencyListMultiGraph<>());
    }

    public BasicSocialGraph(
            Function<? super Agent, ? extends Node> nodeSupplier,
            Supplier<? extends Edge> edgeSupplier,
            DirectedMultiGraph<Node, Edge, Type> graph) {
        NODE_SUPPLIER = nodeSupplier;
        EDGE_SUPPLIER = edgeSupplier;
        GRAPH = graph;
    }

    @Override
    public boolean addAgent(Agent agent) {
        Node node = NODE_SUPPLIER.apply(agent);
        return addNode(node);
    }

    @Override
    public boolean addNode(Node node) {
        boolean added = GRAPH.addVertex(node);
        if(added) {
            NODE_CACHE.put(node.getAgent(), node);
        }
        return added;
    }

    @Override
    public Node getNode(Agent agent) {
        return NODE_CACHE.get(agent);
    }

    @Override
    public boolean hasNode(Node node) {
        return GRAPH.hasVertex(node);
    }

    @Override
    public Set<? extends Node> getNodes() {
        return GRAPH.getVertices();
    }

    @Override
    public Set<? extends Node> getTargets(Node from, Type type) {
        return GRAPH.getTargets(from, type);
    }

    @Override
    public Stream<? extends Node> streamTargets(Node source, Type type) {
        return GRAPH.streamTargets(source, type);
    }

    @Override
    public boolean addEdge(Node from, Node to, Type type) {
        if(GRAPH.hasEdge(from, to, type)) {
            return false;
        } else {
            Edge edge = EDGE_SUPPLIER.get();
            edge.setSource(from);
            edge.setTarget(to);
            return GRAPH.addEdge(from, to, type, edge);
        }
    }

    @Override
    public Edge getEdge(Node from, Node to, Type type) {
        return GRAPH.getEdge(from, to, type);
    }

    @Override
    public Set<? extends Edge> getEdges(Type type) {
        return GRAPH.getEdges(type);
    }

    @Override
    public boolean hasEdge(Node from, Node to, Type type) {
        return GRAPH.hasEdge(from, to, type);
    }

    @Override
    public boolean removeEdge(Edge edge) {
        return GRAPH.removeEdge(edge);
    }

    @Override
    public Set<? extends Edge> removeAllEdges(Type type) {
        return GRAPH.removeAllEdges(type);
    }
}
