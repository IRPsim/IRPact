package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.graph.DirectedGraph;
import de.unileipzig.irpact.commons.graph.SimpleEdge;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.network.exception.NodeWithSameAgentException;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class AgentGraph extends DirectedGraph<AgentGraph.Node, AgentGraph.Edge> {

    protected Map<ConsumerAgent, AgentGraph.Node> agentToNodeMap = new HashMap<>();

    public AgentGraph() {
    }

    public Node findNode(ConsumerAgent agent) throws NoSuchElementException {
        Node node = agentToNodeMap.get(agent);
        if(node == null) {
            throw new NoSuchElementException(agent.getName());
        }
        return node;
    }

    protected void checkNodeAndAdd(Node node) {
        ConsumerAgent agent = node.getAgent();
        Node nodeInMap = agentToNodeMap.get(agent);
        if(nodeInMap == null) {
            agentToNodeMap.put(agent, node);
        }
        else if(node != nodeInMap) {
            throw new NodeWithSameAgentException(agent.getName());
        }
    }

    @Override
    protected void addEdge(Node source, Node target, Edge edge) throws EdgeAlreadyExistsException {
        validateEdgeNodes(source, target, edge);
        checkNodeAndAdd(source);
        checkNodeAndAdd(target);
        super.addEdge(source, target, edge);
    }

    @Override
    protected void setEdge(Node source, Node target, Edge edge) {
        validateEdgeNodes(source, target, edge);
        checkNodeAndAdd(source);
        checkNodeAndAdd(target);
        super.setEdge(source, target, edge);
    }

    private static Comparator<Node> distanceTo(Node node) {
        final SpatialInformation info = node.getAgent().getSpatialInformation();
        return (node1, node2) ->  {
            SpatialInformation info1 = node1.getAgent().getSpatialInformation();
            SpatialInformation info2 = node2.getAgent().getSpatialInformation();
            double distance1 = info.distance(info1);
            double distance2 = info.distance(info2);
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

    public List<? extends Node> getKNearest(Node node, int k) {
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
