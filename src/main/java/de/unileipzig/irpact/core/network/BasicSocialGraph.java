package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.checksum.LoggableChecksum;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.MapBasedTripleMapping;
import de.unileipzig.irpact.commons.graph.DirectedMultiGraph;
import de.unileipzig.irpact.commons.util.data.TripleMapping;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class BasicSocialGraph implements SocialGraph, LoggableChecksum {

    /**
     * @author Daniel Abitz
     */
    public static class BasicNode implements Node {

        private final BasicLinkageInformation linkageInformation = new BasicLinkageInformation();
        private final Agent agent;

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

        private BasicLinkageInformation getLinkageInformation() {
            return linkageInformation;
        }

        @Override
        public String toString() {
            return getLabel();
        }

        @Override
        public int getChecksum() {
            return agent.getChecksum();
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
        public int getChecksum() {
            return Checksums.SMART.getChecksum(
                    getSource(),
                    getTarget(),
                    getWeight(),
                    getType()
            );
        }

        @Override
        public String toString() {
            return "BasicEdge{" +
                    "source=" + source +
                    ", target=" + target +
                    ", type=" + type +
                    ", weight=" + weight +
                    '}';
        }
    }

    /**
     * @author Daniel Abitz
     */
    public static class BasicLinkageInformation implements LinkageInformation {

        private final TripleMapping<Type, Object, Integer> linkCounter = new MapBasedTripleMapping<>();

        public BasicLinkageInformation() {
        }

        @Override
        public void set(Object key, Type type, int value) {
            linkCounter.put(type, key, value);
        }

        @Override
        public void update(Object key, Type type, int delta) {
            int current = linkCounter.get(type, key, 0);
            int newValue = Math.max(current + delta, 0);
            linkCounter.put(type, key, newValue);
        }

        @Override
        public void inc(Object key, Type type) {
            update(key, type, 1);
        }

        @Override
        public void dec(Object key, Type type) {
            update(key, type, -1);
        }

        @Override
        public int get(Object key, Type type) {
            return linkCounter.get(type, key, 0);
        }

        @Override
        public int sum(Object[] keys, Type type) {
            return sum(Arrays.asList(keys), type);
        }

        @Override
        public int sum(Iterable<?> keys, Type type) {
            int total = 0;
            for(Object key: keys) {
                total += get(key, type);
            }
            return total;
        }

        @Override
        public int sum(Type type) {
            int total = 0;
            for(Object key: linkCounter.iterableB(type)) {
                total += get(key, type);
            }
            return total;
        }
    }

    private static final Function<? super Type, ? extends Edge> DEFAULT_EDGE_SUPPLIER = type -> {
        BasicEdge edge = new BasicEdge();
        edge.setType(type);
        return edge;
    };

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicSocialGraph.class);

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

    private static BasicNode check(Node node) {
        if(node == null) {
            throw new NullPointerException("node is null");
        }
        if(node instanceof BasicNode) {
            return (BasicNode) node;
        } else {
            throw new IllegalArgumentException("node '" + node.getLabel() + "' not supported");
        }
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getStructure(),
                getAllAgents(),
                Checksums.SMART.getUnorderedStreamChecksum(streamAllEdges())
        );
    }

    @Override
    public void logChecksums() {
        LOGGER.trace(IRPSection.GENERAL, "checksum @ getStructure: {}", Integer.toHexString(Checksums.SMART.getChecksum(getStructure())));
        LOGGER.trace(IRPSection.GENERAL, "checksum @ getAllAgents: {}", Integer.toHexString(Checksums.SMART.getChecksum(getAllAgents())));
        LOGGER.trace(IRPSection.GENERAL, "checksum @ getAllEdges: {}", Integer.toHexString(Checksums.SMART.getUnorderedStreamChecksum(streamAllEdges())));
    }

    public SupportedGraphStructure getStructure() {
        return STRUCTURE;
    }

    public Collection<Agent> getAllAgents() {
        return NODE_CACHE.keySet();
    }

    public Collection<Edge> getAllEdges() {
        return GRAPH.getAllEdges();
    }

    public Stream<Edge> streamAllEdges() {
        return GRAPH.streamAllEdges();
    }

    @Override
    public LinkageInformation getLinkageInformation(Node node) {
        BasicNode bNode = check(node);
        return bNode.getLinkageInformation();
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
    public List<? extends Node> listTargets(Node from, Type type) {
        List<Node> targets = new ArrayList<>();
        getTargets(from, type, targets);
        return targets;
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
    public Stream<? extends Node> streamSources(Node target, Type type) {
        return GRAPH.streamSources(target, type);
    }

    @Override
    public Stream<? extends Node> streamSourcesAndTargets(Node sourceAndTarget, Type type) {
        return GRAPH.streamSourcesAndTargets(sourceAndTarget, type);
    }

    @Override
    public Node getRandomTarget(Node source, Type type, Rnd rnd) {
        return GRAPH.getRandomTarget(source, type, rnd);
    }

    @Override
    public boolean addEdge(Node from, Node to, Type type, double weight) {
        Edge edge = EDGE_SUPPLIER.apply(type);
        edge.setSource(from);
        edge.setTarget(to);
        edge.setWeight(weight);
        return GRAPH.addEdge(from, to, type, edge);
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
    public boolean hasNoEdge(Node from, Node to, Type type) {
        return !hasEdge(from, to, type);
    }

    @Override
    public boolean removeEdge(Edge edge) {
        return GRAPH.removeEdge(edge);
    }

    @Override
    public Set<? extends Edge> removeAllEdges(Type type) {
        return GRAPH.removeAllEdges(type);
    }

    @Override
    public void printGraph(Appendable target) throws IOException {
        GRAPH.print(
                target,
                Node::getLabel,
                Edge::printLabel,
                Enum::name
        );
    }
}
