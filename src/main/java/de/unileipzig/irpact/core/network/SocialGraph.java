package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.commons.graph.DirectedGraph;
import de.unileipzig.irpact.commons.graph.SimpleEdge;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.network.exception.NodeWithSameAgentException;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialModel;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
//ersetzt durch SocialNetwork aka SocialMultiGraph
@Deprecated
public class SocialGraph extends DirectedGraph<SocialGraph.Node, SocialGraph.Edge> {

    protected Map<ConsumerAgent, SocialGraph.Node> agentToNodeMap = new HashMap<>();

    public SocialGraph() {
    }

    @Override
    protected boolean addIfNotExists(Node node) {
        Node current = agentToNodeMap.get(node.getAgent());
        if(current != null && current != node) {
            throw new NodeWithSameAgentException();
        }
        if(super.addIfNotExists(node)) {
            agentToNodeMap.put(node.getAgent(), node);
            return true;
        }
        return false;
    }

    public boolean hasAgent(ConsumerAgent agent) {
        return agentToNodeMap.containsKey(agent);
    }

    public Node getNode(ConsumerAgent agent) {
        return agentToNodeMap.get(agent);
    }

    public Node findNode(ConsumerAgent agent) throws NoSuchElementException {
        Node node = getNode(agent);
        if(node == null) {
            throw new NoSuchElementException(agent.getName());
        }
        return node;
    }

    public Node addAgent(ConsumerAgent agent) throws NodeWithSameAgentException {
        if(agentToNodeMap.containsKey(agent)) {
            throw new NodeWithSameAgentException(agent.getName());
        }
        Node node = new Node(agent);
        addNode(node);
        return node;
    }

    private static Comparator<Node> distanceTo(Node node) {
        final SpatialInformation info = node.getAgent().getSpatialInformation();
        return (node1, node2) ->  {
            SpatialModel model = node1.getAgent()
                    .getEnvironment()
                    .getSpatialModel();
            SpatialInformation info1 = node1.getAgent().getSpatialInformation();
            SpatialInformation info2 = node2.getAgent().getSpatialInformation();
            double distance1 = model.distance(info, info1);
            double distance2 = model.distance(info, info2);
            return Double.compare(distance1, distance2);
        };
    }

    public Stream<? extends Node> streamKNearest(Node node, int k) {
        return graphData.keySet()
                .stream()
                .filter(_node -> _node != node)
                .sorted(distanceTo(node))
                .limit(k);
    }

    public List<Node> getKNearest(Node node, int k) {
        return streamKNearest(node, k).collect(Collectors.toList());
    }

    /**
     * @author Daniel Abitz
     */
    public static final class Node implements de.unileipzig.irpact.commons.graph.Node {

        private ConsumerAgent agent;

        public Node(ConsumerAgent agent) {
            this.agent = Check.requireNonNull(agent, "agent");
        }

        public ConsumerAgent getAgent() {
            return agent;
        }

        @Override
        public String getLabel() {
            return agent.getName();
        }
    }

    /**
     * @author Daniel Abitz
     */
    public static final class Edge extends SimpleEdge<Node> {

        private Map<EdgeType, Double> weights = new EnumMap<>(EdgeType.class);

        public Edge(Node source, Node target, String label) {
            super(source, target, label);
        }

        public boolean hasWeight(EdgeType edgeType) {
            return weights.containsKey(edgeType);
        }

        public void setWeight(EdgeType edgeType, double weight) {
            weights.put(edgeType, weight);
        }

        public double getWeight(EdgeType edgeType) {
            Double weight = weights.get(edgeType);
            if(weight == null) {
                throw new NoSuchElementException();
            }
            return weight;
        }
    }
}
