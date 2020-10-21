package de.unileipzig.irpact.v2.core.network;

import de.unileipzig.irpact.v2.core.agent.Agent;

/**
 * @author Daniel Abitz
 */
public interface SocialGraph {

    /**
     * @author Daniel Abitz
     */
    interface Node {

        String getLabel();

        Agent getAgent();

        <T extends Agent> T getAgent(Class<T> type);
    }

    /**
     * @author Daniel Abitz
     */
    interface Edge {

        void setWeight(double value);

        double getWeight();
    }

    /**
     * @author Daniel Abitz
     */
    enum Type {
        COMMUNICATION
    }

    boolean addAgent(Agent agent);

    boolean addNode(Node node);

    Node getNode(Agent agent);

    boolean hasNode(Node node);

    boolean addEdge(Node from, Node to, Type type);

    Edge getEdge(Node from, Node to, Type type);

    boolean hasEdge(Node from, Node to, Type type);
}
