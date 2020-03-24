package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.network.exception.NodeWithSameAgentException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface SocialGraph {

    boolean hasAgent(Agent agent);

    Node addAgent(Agent agent) throws NodeWithSameAgentException;

    Node getNode(Agent agent);

    Node findNode(Agent agent) throws NoSuchElementException;

    Set<? extends Node> getNodes();

    Stream<? extends Node> streamNodes();

    Set<? extends Node> getSourceNodes(Node targetNode, EdgeType type);

    Stream<? extends Node> streamSourceNodes(Node targetNode, EdgeType type);

    Set<? extends Node> getTargetNodes(Node sourceNode, EdgeType type);

    Set<? extends Edge> getEdges(EdgeType type);

    Set<? extends Edge> getOutEdges(Node node, EdgeType type);

    Set<? extends Edge> getInEdges(Node node, EdgeType type);

    boolean hasNode(Node node);

    Edge addEdge(Node source, Node target, EdgeType type) throws EdgeAlreadyExistsException;

    boolean removeEdge(Edge edge, EdgeType type);

    Edge getEdge(Node source, Node target, EdgeType type);

    Edge findEdge(Node source, Node target, EdgeType type) throws NoSuchElementException;

    default boolean hasEdge(Node source, Node target, EdgeType type) {
        return getEdge(source, target, type) != null;
    }

    //=========================
    //Util
    //=========================

    Stream<? extends Node> streamKNearest(Node node, int k);

    default List<Node> getKNearest(Node node, int k) {
        return streamKNearest(node, k).collect(Collectors.toList());
    }

    Stream<? extends Node> streamKNearestNeighbours(Node node, int k, EdgeType type);

    default List<Node> getKNearestNeighbours(Node node, int k, EdgeType type) {
        return streamKNearestNeighbours(node, k, type).collect(Collectors.toList());
    }

    //=========================
    //Nodes + Edges
    //=========================

    /**
     * @author Daniel Abitz
     */
    interface Node {

        String getLabel();

        Agent getAgent();

        default <T extends Agent> T getAgent(Class<T> type) {
            return type.cast(getAgent());
        }

        default <T extends Agent> boolean isAgent(Class<T> type) {
            return type.isInstance(getAgent());
        }
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
