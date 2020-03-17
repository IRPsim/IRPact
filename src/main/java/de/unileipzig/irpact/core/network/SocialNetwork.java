package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.commons.graph.DirectedMultiGraph;
import de.unileipzig.irpact.commons.graph.SimpleEdge;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.network.exception.NodeWithSameAgentException;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public class SocialNetwork extends DirectedMultiGraph<SocialNetwork.Node, SocialNetwork.Edge, EdgeType> {

    private Map<ConsumerAgent, Node> agentToNodeMap = new HashMap<>();

    public SocialNetwork() {
        super(new HashMap<>());
    }

    @Override
    protected Map<Node, Map<EdgeType, Edge>> createSubMap() {
        return new HashMap<>();
    }

    @Override
    protected Map<EdgeType, Edge> createEdgeMap() {
        return new EnumMap<>(EdgeType.class);
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

    public void addAgent(ConsumerAgent agent) throws NodeWithSameAgentException {
        if(agentToNodeMap.containsKey(agent)) {
            throw new NodeWithSameAgentException(agent.getName());
        }
        Node node = new Node(agent);
        addNode(node);
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
    public static final class Edge extends SimpleEdge<SocialNetwork.Node> {

        private double weight;

        public Edge(SocialNetwork.Node source, SocialNetwork.Node target, String label, double weight) {
            super(source, target, label);
            setWeight(weight);
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public double getWeight() {
            return weight;
        }
    }
}
