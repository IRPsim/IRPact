package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.EdgeNodesMismatchException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;
import de.unileipzig.irpact.core.network.exception.NodeWithSameAgentException;
import de.unileipzig.irpact.core.spatial.Point2D;
import de.unileipzig.irpact.mock.MockConsumerAgent;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class SocialGraphTest {

    @Test
    void testTypes() {
        SocialGraph graph = new SocialGraph();

        MockConsumerAgent a0 = new MockConsumerAgent("a0");
        MockConsumerAgent a1 = new MockConsumerAgent("a1");

        SocialGraph.Node n0 = new SocialGraph.Node(a0);
        SocialGraph.Node n1 = new SocialGraph.Node(a1);
        SocialGraph.Edge e0 = new SocialGraph.Edge(n0, n1, "e0");

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
        SocialGraph graph = new SocialGraph();

        MockConsumerAgent a0 = new MockConsumerAgent("a0");
        MockConsumerAgent a1 = new MockConsumerAgent("a1");
        MockConsumerAgent a2 = new MockConsumerAgent("a2");

        SocialGraph.Node n0_0 = new SocialGraph.Node(a0);
        SocialGraph.Node n0_1 = new SocialGraph.Node(a0);
        SocialGraph.Node n1 = new SocialGraph.Node(a1);
        SocialGraph.Node n2 = new SocialGraph.Node(a2);
        SocialGraph.Edge e0 = new SocialGraph.Edge(n0_0, n1, "e0");
        SocialGraph.Edge e1 = new SocialGraph.Edge(n0_1, n2, "e1");

        graph.addEdge(e0);
        assertThrows(NodeWithSameAgentException.class, () -> graph.addEdge(e1));
        assertThrows(EdgeAlreadyExistsException.class, () -> graph.addEdge(e0));
    }

    @Test
    void testWrongEdgeNodes() {
        SocialGraph graph = new SocialGraph();

        MockConsumerAgent a0 = new MockConsumerAgent("a0");
        MockConsumerAgent a1 = new MockConsumerAgent("a1");
        MockConsumerAgent a2 = new MockConsumerAgent("a2");

        SocialGraph.Node n0 = new SocialGraph.Node(a0);
        SocialGraph.Node n1 = new SocialGraph.Node(a1);
        SocialGraph.Node n2 = new SocialGraph.Node(a2);
        SocialGraph.Edge e0 = new SocialGraph.Edge(n0, n1, "e0");

        assertThrows(EdgeNodesMismatchException.class, () -> graph.addEdge(n0, n2, e0));
        assertThrows(EdgeNodesMismatchException.class, () -> graph.addEdge(n2, n1, e0));
        assertThrows(EdgeNodesMismatchException.class, () -> graph.addEdge(n1, n0, e0));
        assertDoesNotThrow(() -> graph.addEdge(n0, n1, e0));
    }

    @Test
    void testAgent() {
        MockConsumerAgent a0 = new MockConsumerAgent("a0", new Point2D(0, 0));
        MockConsumerAgent a100 = new MockConsumerAgent("a0", new Point2D(100, 100));

        SocialGraph graph = new SocialGraph();
        SocialGraph.Node n0 = graph.addAgent(a0);
        assertSame(n0, graph.findNode(a0));
        assertTrue(graph.hasAgent(a0));

        assertFalse(graph.hasAgent(a100));
        assertThrows(NoSuchElementException.class, () -> graph.findNode(a100));

        assertThrows(NodeWithSameAgentException.class, () -> graph.addAgent(a0));
        assertThrows(NodeAlreadyExistsException.class, () -> graph.addNode(n0));
    }

    @Test
    void testGetKNearest() {
        MockConsumerAgent a0 = new MockConsumerAgent("a0", new Point2D(0, 0));
        MockConsumerAgent a100 = new MockConsumerAgent("a0", new Point2D(100, 100));

        SocialGraph graph = new SocialGraph();
        graph.addAgent(a0);
        graph.addAgent(a100);

        for(int i = 1; i < 10; i++) {
            graph.addAgent(new MockConsumerAgent("a" + i, new Point2D(i, i)));
        }

        List<SocialGraph.Node> n0nearest3 = graph.getKNearest(graph.findNode(a0), 3);
        assertEquals(3, n0nearest3.size());
        assertEquals(new Point2D(1, 1), n0nearest3.get(0).getAgent().getSpatialInformation());
        assertEquals(new Point2D(2, 2), n0nearest3.get(1).getAgent().getSpatialInformation());
        assertEquals(new Point2D(3, 3), n0nearest3.get(2).getAgent().getSpatialInformation());

        List<SocialGraph.Node> n100nearest2 = graph.getKNearest(graph.findNode(a100), 2);
        assertEquals(2, n100nearest2.size());
        assertEquals(new Point2D(9, 9), n100nearest2.get(0).getAgent().getSpatialInformation());
        assertEquals(new Point2D(8, 8), n100nearest2.get(1).getAgent().getSpatialInformation());
    }
}