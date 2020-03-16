package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.EdgeNodesMismatchException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;
import de.unileipzig.irpact.core.network.exception.NodeWithSameAgentException;
import de.unileipzig.irpact.mock.MockConsumerAgent;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class AgentGraphTest {

    @Test
    void testTypes() {
        AgentGraph graph = new AgentGraph();

        MockConsumerAgent a0 = new MockConsumerAgent("a0");
        MockConsumerAgent a1 = new MockConsumerAgent("a1");

        AgentGraph.Node n0 = new AgentGraph.Node(a0);
        AgentGraph.Node n1 = new AgentGraph.Node(a1);
        AgentGraph.Edge e0 = new AgentGraph.Edge(n0, n1, "e0");

        assertFalse(graph.hasEdge(n0, n1));
        assertFalse(graph.hasEdge(e0));

        graph.addEdge(e0);

        assertTrue(graph.hasEdge(n0, n1));
        assertTrue(graph.hasEdge(e0));

        assertFalse(e0.hasWeight(EdgeType.TRUST));
        e0.setWeight(EdgeType.TRUST, 1);
        assertTrue(e0.hasWeight(EdgeType.TRUST));
        assertEquals(1, e0.getWeight(EdgeType.TRUST));
        assertThrows(NoSuchElementException.class, () -> e0.getWeight(EdgeType.COMMUNICATION));
        e0.setWeight(EdgeType.COMMUNICATION, 2);
        assertDoesNotThrow(() -> e0.getWeight(EdgeType.COMMUNICATION));
        assertEquals(2, e0.getWeight(EdgeType.COMMUNICATION));
    }

    @Test
    void testSameAgent() {
        AgentGraph graph = new AgentGraph();

        MockConsumerAgent a0 = new MockConsumerAgent("a0");
        MockConsumerAgent a1 = new MockConsumerAgent("a1");
        MockConsumerAgent a2 = new MockConsumerAgent("a2");

        AgentGraph.Node n0_0 = new AgentGraph.Node(a0);
        AgentGraph.Node n0_1 = new AgentGraph.Node(a0);
        AgentGraph.Node n1 = new AgentGraph.Node(a1);
        AgentGraph.Node n2 = new AgentGraph.Node(a2);
        AgentGraph.Edge e0 = new AgentGraph.Edge(n0_0, n1, "e0");
        AgentGraph.Edge e1 = new AgentGraph.Edge(n0_1, n2, "e1");

        graph.addEdge(e0);
        assertThrows(NodeWithSameAgentException.class, () -> graph.addEdge(e1));
        assertThrows(EdgeAlreadyExistsException.class, () -> graph.addEdge(e0));
    }

    @Test
    void testWrongEdgeNodes() {
        AgentGraph graph = new AgentGraph();

        MockConsumerAgent a0 = new MockConsumerAgent("a0");
        MockConsumerAgent a1 = new MockConsumerAgent("a1");
        MockConsumerAgent a2 = new MockConsumerAgent("a2");

        AgentGraph.Node n0 = new AgentGraph.Node(a0);
        AgentGraph.Node n1 = new AgentGraph.Node(a1);
        AgentGraph.Node n2 = new AgentGraph.Node(a2);
        AgentGraph.Edge e0 = new AgentGraph.Edge(n0, n1, "e0");

        assertThrows(EdgeNodesMismatchException.class, () -> graph.addEdge(n0, n2, e0));
        assertThrows(EdgeNodesMismatchException.class, () -> graph.addEdge(n2, n1, e0));
        assertThrows(EdgeNodesMismatchException.class, () -> graph.addEdge(n1, n0, e0));
        assertDoesNotThrow(() -> graph.addEdge(n0, n1, e0));
    }
}