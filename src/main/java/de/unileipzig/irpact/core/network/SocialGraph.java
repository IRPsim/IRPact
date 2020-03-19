package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.network.exception.NodeWithSameAgentException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface SocialGraph {

    boolean hasAgent(ConsumerAgent agent);

    Node addAgent(ConsumerAgent agent) throws NodeWithSameAgentException;

    Node getNode(ConsumerAgent agent);

    Node findNode(ConsumerAgent agent) throws NoSuchElementException;

    Edge addEdge(ConsumerAgent source, ConsumerAgent target, EdgeType type);

    Edge addEdge(Node source, Node target, EdgeType type) throws EdgeAlreadyExistsException;

    Edge getEdge(ConsumerAgent source, ConsumerAgent target, EdgeType type);

    Edge findEdge(ConsumerAgent source, ConsumerAgent target, EdgeType type) throws NoSuchElementException;

    Edge getEdge(Node source, Node target, EdgeType type);

    Edge findEdge(Node source, Node target, EdgeType type) throws NoSuchElementException;

    default boolean hasEdge(ConsumerAgent source, ConsumerAgent target, EdgeType type) {
        return getEdge(source, target, type) != null;
    }

    default boolean hasEdge(Node source, Node target, EdgeType type) {
        return getEdge(source, target, type) != null;
    }

    Stream<? extends Node> streamKNearest(SocialGraph.Node node, int k);

    List<Node> getKNearest(SocialGraph.Node node, int k);

    /**
     * @author Daniel Abitz
     */
    interface Node {

        String getLabel();

        ConsumerAgent getAgent();
    }

    /**
     * @author Daniel Abitz
     */
    interface Edge {

        String getLabel();

        Node getSource();

        Node getTarget();

        double getWeight();

        void setWeight(double weight);
    }
}
