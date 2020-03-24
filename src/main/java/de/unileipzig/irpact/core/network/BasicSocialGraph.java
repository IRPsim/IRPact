package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.graph.MultiGraph;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.SpatialAgent;
import de.unileipzig.irpact.core.network.exception.NodeWithSameAgentException;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialModel;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class BasicSocialGraph implements SocialGraph {

    private MultiGraph<BasicNode, BasicEdge, EdgeType> graph;
    private Map<Agent, BasicNode> agentNodeMapping;

    public BasicSocialGraph(
            MultiGraph<BasicNode, BasicEdge, EdgeType> graph,
            Map<Agent, BasicNode> agentNodeMapping) {
        this.graph = graph;
        this.agentNodeMapping = agentNodeMapping;
    }

    @Override
    public boolean hasAgent(Agent agent) {
        return agentNodeMapping.containsKey(agent);
    }

    @Override
    public boolean hasNode(Node node) {
        return graph.hasNode((BasicNode) node);
    }

    @Override
    public BasicNode getNode(Agent agent) {
        return agentNodeMapping.get(agent);
    }

    @Override
    public BasicNode findNode(Agent agent) throws NoSuchElementException {
        BasicNode node = getNode(agent);
        if(node == null) {
            throw new NoSuchElementException();
        }
        return node;
    }

    @Override
    public BasicNode addAgent(Agent agent) throws NodeWithSameAgentException {
        if(hasAgent(agent)) {
            throw new NodeWithSameAgentException();
        }
        BasicNode node = new BasicNode(agent);
        agentNodeMapping.put(agent, node);
        graph.addNode(node);
        return node;
    }

    @Override
    public BasicEdge addEdge(Node source, Node target, EdgeType type) throws EdgeAlreadyExistsException {
        BasicNode basicSource = (BasicNode) source;
        BasicNode basicTarget = (BasicNode) target;
        if(graph.hasEdge(basicSource, basicTarget, type)) {
            throw new EdgeAlreadyExistsException();
        }
        BasicEdge edge = new BasicEdge(basicSource, basicTarget, 0.0);
        graph.addEdge(edge, type);
        return edge;
    }

    @Override
    public boolean removeEdge(Edge edge, EdgeType type) {
        return graph.removeEdge((BasicEdge) edge, type);
    }

    @Override
    public BasicEdge getEdge(Node source, Node target, EdgeType type) {
        return graph.getEdge(
                (BasicNode) source,
                (BasicNode) target,
                type
        );
    }

    @Override
    public BasicEdge findEdge(Node source, Node target, EdgeType type) throws NoSuchElementException {
        BasicEdge edge = getEdge(source, target, type);
        if(edge == null) {
            throw new NoSuchElementException();
        }
        return edge;
    }

    @Override
    public Set<? extends Node> getNodes() {
        return graph.getNodes();
    }

    @Override
    public Stream<? extends Node> streamNodes() {
        return graph.getNodes().stream();
    }

    @Override
    public Set<? extends Node> getSourceNodes(Node targetNode, EdgeType type) {
        return graph.getSourceNodes((BasicNode) targetNode, type);
    }

    @Override
    public Stream<? extends Node> streamSourceNodes(Node targetNode, EdgeType type) {
        return graph.streamSourceNodes((BasicNode) targetNode, type);
    }

    @Override
    public Set<? extends Node> getTargetNodes(Node sourceNode, EdgeType type) {
        return graph.getTargetNodes((BasicNode) sourceNode, type);
    }

    @Override
    public Set<? extends Edge> getEdges(EdgeType type) {
        return graph.getEdges(type);
    }

    @Override
    public Set<? extends Edge> getOutEdges(Node node, EdgeType type) {
        return graph.getOutEdges((BasicNode) node, type);
    }

    @Override
    public Set<? extends Edge> getInEdges(Node node, EdgeType type) {
        return graph.getInEdges((BasicNode) node, type);
    }

    //=========================
    //Util
    //=========================

    private static Comparator<SocialGraph.Node> distanceTo(Node node) {
        final SpatialInformation info = node.getAgent(SpatialAgent.class).getSpatialInformation();
        return (node1, node2) ->  {
            SpatialModel model = node1.getAgent()
                    .getEnvironment()
                    .getSpatialModel();
            SpatialInformation info1 = node1.getAgent(SpatialAgent.class).getSpatialInformation();
            SpatialInformation info2 = node2.getAgent(SpatialAgent.class).getSpatialInformation();
            double distance1 = model.distance(info, info1);
            double distance2 = model.distance(info, info2);
            return Double.compare(distance1, distance2);
        };
    }

    @Override
    public Stream<? extends SocialGraph.Node> streamKNearest(Node node, int k) {
        return graph.getNodes()
                .stream()
                .filter(_node -> _node != node && _node.isAgent(SpatialAgent.class))
                .sorted(distanceTo(node))
                .limit(k);
    }

    @Override
    public Stream<? extends Node> streamKNearestNeighbours(Node node, int k, EdgeType type) {
        return graph.getTargetNodes((BasicNode) node, type)
                .stream()
                .filter(_node -> _node != node && _node.isAgent(SpatialAgent.class))
                .sorted(distanceTo(node))
                .limit(k);
    }

    //=========================
    //Nodes + Edges
    //=========================

    /**
     * @author Daniel Abitz
     */
    public static class BasicNode implements SocialGraph.Node, de.unileipzig.irpact.commons.graph.Node {

        private Agent agent;

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
        public String toString() {
            return getLabel();
        }
    }

    /**
     * @author Daniel Abitz
     */
    public static class BasicEdge implements SocialGraph.Edge, de.unileipzig.irpact.commons.graph.Edge<BasicNode> {

        private BasicNode source;
        private BasicNode target;
        private double weight;

        public BasicEdge(BasicNode source, BasicNode target, double weight) {
            this.source = source;
            this.target = target;
            this.weight = weight;
        }

        @Override
        public String getLabel() {
            return source.getLabel() + "->" + target.getLabel();
        }

        @Override
        public BasicNode getSource() {
            return source;
        }

        @Override
        public BasicNode getTarget() {
            return target;
        }

        @Override
        public double getWeight() {
            return weight;
        }

        @Override
        public void setWeight(double weight) {
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "(" + getLabel() + ", " + getWeight() + ")";
        }
    }
}
