package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.commons.IsEquals;
import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.TripleMapping;
import de.unileipzig.irpact.commons.graph.DirectedMultiGraph;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.util.Todo;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class BasicSocialGraph implements SocialGraph {

    /**
     * @author Daniel Abitz
     */
    //Da wir den Agent austauschen, wird equals und hashCode nicht veraendert.
    public static class BasicNode implements Node {

        private final Agent agent;
        protected TripleMapping<Type, Object, Integer> linkCounter = new TripleMapping<>();

        public BasicNode(Agent agent) {
            this.agent = agent;
        }

        @Override
        public String getLabel() {
            return agent.getName();
        }

        @Override
        public Agent getAgent() {
            return agent;
        }

        @Override
        public <T extends Agent> T getAgent(Class<T> type) {
            return type.cast(agent);
        }

        @Override
        public <T extends Agent> boolean is(Class<T> type) {
            return type.isInstance(agent);
        }

        @Todo("HMMM, generisches Objekt: ja/nein?")
        public void inc(Type type, Object key, int delta) {
            int current = linkCounter.get(type, key, 0);
            linkCounter.put(type, key, current + delta);
        }

        public void dec(Type type, Object key, int delta) {
            int current = linkCounter.get(type, key, 0);
            linkCounter.put(type, key, current - delta);
        }

        public int getLinkCount(Type type, Object key) {
            return linkCounter.get(type, key, 0);
        }

        public int getTotalLinkCount(Type typ) {
            return linkCounter.streamValues(typ)
                    .mapToInt(i -> i)
                    .sum();
        }

        @Override
        public String toString() {
            return getLabel();
        }

        @Override
        public int getHashCode() {
            return agent.getHashCode();
        }
    }

    /**
     * @author Daniel Abitz
     */
    public static class BasicEdge implements Edge {

        private Node source;
        private Node target;
        private Type type;
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

        public void setType(Type type) {
            this.type = type;
        }

        public Type getType() {
            return type;
        }

        @Override
        public int getHashCode() {
            return Objects.hash(
                    getSource().getHashCode(),
                    getTarget().getHashCode(),
                    getWeight(),
                    getType().getHashCode()
            );
        }
    }

    private static final Function<? super Type, ? extends Edge> DEFAULT_EDGE_SUPPLIER = type -> {
        BasicEdge edge = new BasicEdge();
        edge.setType(type);
        return edge;
    };

    private final Function<? super Agent, ? extends Node> NODE_SUPPLIER;
    private final Function<? super Type, ? extends Edge> EDGE_SUPPLIER;
    private final SupportedGraphStructure STRUCTURE;
    private final DirectedMultiGraph<Node, Edge, Type> GRAPH;
    private final Map<Agent, Node> NODE_CACHE = new LinkedHashMap<>();

    public BasicSocialGraph(SupportedGraphStructure structure) {
        this(BasicNode::new, DEFAULT_EDGE_SUPPLIER, structure);
    }

    public BasicSocialGraph(
            Function<? super Agent, ? extends Node> nodeSupplier,
            Function<? super Type, ? extends Edge> edgeSupplier,
            SupportedGraphStructure structure) {
        NODE_SUPPLIER = nodeSupplier;
        EDGE_SUPPLIER = edgeSupplier;
        STRUCTURE = structure;
        GRAPH = structure.newInstance();
    }

    @Override
    public int getHashCode() {
        return Objects.hash(
                getStructure().getHashCode(),
                IsEquals.getCollHashCode(getAllAgents()),
                IsEquals.getCollHashCode(getAllEdges())
        );
    }

    public SupportedGraphStructure getStructure() {
        return STRUCTURE;
    }

    public Collection<Agent> getAllAgents() {
        return NODE_CACHE.keySet();
    }

    public Collection<Edge> getAllEdges() {
        return GRAPH.getAllEdges(Type.values());
    }

    @Override
    public boolean addAgent(Agent agent) {
        if(NODE_CACHE.containsKey(agent)) {
            return false;
        }
        Node node = NODE_SUPPLIER.apply(agent);
        return addNode(node);
    }

    @Override
    public Node addAgentAndGetNode(Agent agent) {
        if(NODE_CACHE.containsKey(agent)) {
            return NODE_CACHE.get(agent);
        } else {
            Node node = NODE_SUPPLIER.apply(agent);
            addNode(node);
            return node;
        }
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
    public boolean hasNode(Agent agent) {
        return NODE_CACHE.containsKey(agent);
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
    public boolean getTargets(Node from, Type type, Collection<? super Node> targets) {
        return GRAPH.getTargets(from, type, targets);
    }

    @Override
    public Stream<? extends Node> streamNodes() {
        return GRAPH.streamVertices();
    }

    @Override
    public Stream<? extends Node> streamTargets(Node source, Type type) {
        return GRAPH.streamTargets(source, type);
    }

    @Override
    public boolean addEdge(Node from, Node to, Type type, double weight) {
        if(GRAPH.hasEdge(from, to, type)) {
            return false;
        } else {
            Edge edge = EDGE_SUPPLIER.apply(type);
            edge.setSource(from);
            edge.setTarget(to);
            edge.setWeight(weight);
            return GRAPH.addEdge(from, to, type, edge);
        }
    }

    public void addEdgeDirect(BasicEdge edge) {
        if(GRAPH.hasEdge(edge.getSource(), edge.getTarget(), edge.getType())) {
            throw new IllegalArgumentException("edge '"
                    + edge.getSource().getLabel()
                    + "' -> '"
                    + edge.getTarget().getLabel()
                    + "' already exists"
            );
        }
        GRAPH.addEdge(edge.getSource(), edge.getTarget(), edge.getType(), edge);
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

    //=========================
    //special
    //=========================

    @Override
    public int countUnlinked(Node srcNode, ConsumerAgentGroup tarCag, Type type) {
        int count = 0;
        for(ConsumerAgent tar: tarCag.getAgents()) {
            Node tarNode = tar.getSocialGraphNode();
            if(!hasEdge(srcNode, tarNode, type)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int countUnlinked(Node srcNode, Collection<? extends ConsumerAgentGroup> tarCags, Type type) {
        int total = 0;
        for(ConsumerAgentGroup tarCag: tarCags) {
            total += countUnlinked(srcNode, tarCag, type);
        }
        return total;
    }

    @Override
    public Node getRandomUnlinked(Node srcNode, ConsumerAgentGroup tarCag, Type type, Rnd rnd) {
        List<Node> tars = new ArrayList<>();
        for(ConsumerAgent tar: tarCag.getAgents()) {
            Node tarNode = tar.getSocialGraphNode();
            if(!hasEdge(srcNode, tarNode, type)) {
                tars.add(tarNode);
            }
        }
        if(tars.isEmpty()) {
            return null;
        } else {
            return CollectionUtil.getRandom(tars, rnd);
        }
    }
}
