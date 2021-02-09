package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.core.agent.Agent;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

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

        void setSource(Node node);

        Node getSource();

        void setTarget(Node node);

        Node getTarget();

        void setWeight(double value);

        double getWeight();
    }

    /**
     * @author Daniel Abitz
     */
    enum Type {
        COMMUNICATION(0),
        UNKNOWN(-1);

        private final int ID;

        Type(int id) {
            ID = id;
        }

        public static Type get(int id) {
            for(Type t: values()) {
                if(id == t.ID) {
                    return t;
                }
            }
            return UNKNOWN;
        }

        public int id() {
            return ID;
        }

        public boolean isValid() {
            return this != UNKNOWN;
        }
    }

    void replacePlaceholder(Agent realAgent) throws IllegalStateException;

    boolean addAgent(Agent agent);

    Node addAgentAndGetNode(Agent agent);

    boolean addNode(Node node);

    Node getNode(Agent agent);

    boolean hasNode(Node node);

    Set<? extends Node> getNodes();

    Set<? extends Node> getTargets(Node from, Type type);

    boolean getTargets(Node from, Type type, Collection<? super Node> targets);

    Stream<? extends Node> streamTargets(Node source, Type type);

    boolean addEdge(Node from, Node to, Type type, double weight);

    Edge getEdge(Node from, Node to, Type type);

    Set<? extends Edge> getEdges(Type type);

    boolean hasEdge(Node from, Node to, Type type);

    boolean removeEdge(Edge edge);

    Set<? extends Edge> removeAllEdges(Type type);
}
