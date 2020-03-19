package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.graph.MultiGraph;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.network.exception.NodeWithSameAgentException;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialModel;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class BasicSocialGraph implements SocialGraph {

    private MultiGraph<BasicNode, BasicEdge, EdgeType> graph;
    private Map<ConsumerAgent, BasicNode> agentNodeMapping;

    public BasicSocialGraph(
            MultiGraph<BasicNode, BasicEdge, EdgeType> graph,
            Map<ConsumerAgent, BasicNode> agentNodeMapping) {
        this.graph = graph;
        this.agentNodeMapping = agentNodeMapping;
    }

    protected BasicNode addIfNotExists(ConsumerAgent agent) {
        BasicNode node = agentNodeMapping.get(agent);
        if(node != null) {
            return node;
        }
        return addAgent(agent);
    }

    @Override
    public boolean hasAgent(ConsumerAgent agent) {
        return agentNodeMapping.containsKey(agent);
    }

    @Override
    public BasicNode getNode(ConsumerAgent agent) {
        return agentNodeMapping.get(agent);
    }

    @Override
    public BasicNode findNode(ConsumerAgent agent) throws NoSuchElementException {
        BasicNode node = getNode(agent);
        if(node == null) {
            throw new NoSuchElementException();
        }
        return node;
    }

    @Override
    public BasicNode addAgent(ConsumerAgent agent) throws NodeWithSameAgentException {
        if(hasAgent(agent)) {
            throw new NodeWithSameAgentException();
        }
        BasicNode node = new BasicNode(agent);
        agentNodeMapping.put(agent, node);
        graph.addNode(node);
        return node;
    }

    @Override
    public BasicEdge addEdge(ConsumerAgent source, ConsumerAgent target, EdgeType type) {
        BasicNode sourceNode = addIfNotExists(source);
        BasicNode targetNode = addIfNotExists(target);
        return addEdge(sourceNode, targetNode, type);
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
    public BasicEdge getEdge(ConsumerAgent source, ConsumerAgent target, EdgeType type) {
        BasicNode sourceNode = getNode(source);
        if(sourceNode == null) {
            return null;
        }
        BasicNode targetNode = getNode(target);
        if(targetNode == null) {
            return null;
        }
        return getEdge(sourceNode, targetNode, type);
    }

    @Override
    public BasicEdge findEdge(ConsumerAgent source, ConsumerAgent target, EdgeType type) throws NoSuchElementException {
        BasicEdge edge = getEdge(source, target, type);
        if(edge == null) {
            throw new NoSuchElementException();
        }
        return edge;
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

    private static Comparator<SocialGraph.Node> distanceTo(SocialGraph.Node node) {
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

    @Override
    public Stream<? extends SocialGraph.Node> streamKNearest(SocialGraph.Node node, int k) {
        return graph.getNodes()
                .stream()
                .filter(_node -> _node != node)
                .sorted(distanceTo(node))
                .limit(k);
    }

    @Override
    public List<SocialGraph.Node> getKNearest(SocialGraph.Node node, int k) {
        return streamKNearest(node, k).collect(Collectors.toList());
    }

    /**
     * @author Daniel Abitz
     */
    public static class BasicNode implements SocialGraph.Node, de.unileipzig.irpact.commons.graph.Node {

        private ConsumerAgent agent;

        public BasicNode(ConsumerAgent agent) {
            this.agent = agent;
        }

        @Override
        public String getLabel() {
            return agent.getName();
        }

        @Override
        public ConsumerAgent getAgent() {
            return agent;
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
    }
}
