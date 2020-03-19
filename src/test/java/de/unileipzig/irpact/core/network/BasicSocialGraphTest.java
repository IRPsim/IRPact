package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.graph.DirectedMultiGraph;
import de.unileipzig.irpact.core.network.exception.NodeWithSameAgentException;
import de.unileipzig.irpact.core.spatial.Metric;
import de.unileipzig.irpact.core.spatial.dim2.Point2D;
import de.unileipzig.irpact.core.spatial.dim2.SquareModel;
import de.unileipzig.irpact.mock.MockConsumerAgent;
import de.unileipzig.irpact.mock.MockSimulationEnvironment;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class BasicSocialGraphTest {

    @Test
    void testAddAgent() {
        BasicSocialGraph smg = new BasicSocialGraph(new DirectedMultiGraph<>(new HashMap<>()), new HashMap<>());
        MockConsumerAgent a = new MockConsumerAgent("1");
        assertFalse(smg.hasAgent(a));
        BasicSocialGraph.BasicNode node = smg.addAgent(a);
        assertTrue(smg.hasAgent(a));
        assertSame(node, smg.getNode(a));
        assertThrows(NodeWithSameAgentException.class, () -> smg.addAgent(a));
    }

    @Test
    void testGetAndFindAgent() {
        BasicSocialGraph smg = new BasicSocialGraph(new DirectedMultiGraph<>(new HashMap<>()), new HashMap<>());
        MockConsumerAgent a = new MockConsumerAgent("1");
        assertNull(smg.getNode(a));
        assertThrows(NoSuchElementException.class, () -> smg.findNode(a));
        BasicSocialGraph.BasicNode node = smg.addAgent(a);
        assertSame(a, smg.getNode(a).getAgent());
        assertSame(a, smg.findNode(a).getAgent());
        assertSame(smg.getNode(a), smg.findNode(a));
        assertSame(node, smg.getNode(a));
    }

    @Test
    void testAgentExistsIfEdgeAdded() {
        BasicSocialGraph smg = new BasicSocialGraph(new DirectedMultiGraph<>(new HashMap<>()), new HashMap<>());
        MockConsumerAgent a1 = new MockConsumerAgent("1");
        MockConsumerAgent a2 = new MockConsumerAgent("1");

        assertFalse(smg.hasAgent(a1));
        assertFalse(smg.hasAgent(a2));
        assertFalse(smg.hasEdge(a1, a2, EdgeType.COMMUNICATION));

        BasicSocialGraph.BasicNode n1 = smg.addAgent(a1);
        BasicSocialGraph.BasicNode n2 = smg.addAgent(a2);
        BasicSocialGraph.BasicEdge e = smg.addEdge(n1, n2, EdgeType.COMMUNICATION);

        assertTrue(smg.hasAgent(a1));
        assertTrue(smg.hasAgent(a2));
        assertTrue(smg.hasEdge(a1, a2, EdgeType.COMMUNICATION));
        assertTrue(smg.hasEdge(n1, n2, EdgeType.COMMUNICATION));
        assertSame(e, smg.getEdge(n1, n2, EdgeType.COMMUNICATION));

        assertFalse(smg.hasEdge(a1, a2, EdgeType.TRUST));
        assertFalse(smg.hasEdge(n1, n2, EdgeType.TRUST));
    }

    @Test
    void testGetKNearest() {
        MockSimulationEnvironment env = new MockSimulationEnvironment();
        env.setSpatialModel(new SquareModel("unit", Metric.EUCLIDEAN, 0, 0, 1, 1));
        MockConsumerAgent a0 = new MockConsumerAgent("a0", env, new Point2D(0, 0));
        MockConsumerAgent a100 = new MockConsumerAgent("a0", env, new Point2D(100, 100));

        BasicSocialGraph smg = new BasicSocialGraph(new DirectedMultiGraph<>(new HashMap<>()), new HashMap<>());
        BasicSocialGraph.BasicNode n0 = smg.addAgent(a0);
        BasicSocialGraph.BasicNode n100 = smg.addAgent(a100);

        for(int i = 1; i < 10; i++) {
            smg.addAgent(new MockConsumerAgent("a" + i, env, new Point2D(i, i)));
        }

        List<SocialGraph.Node> n0nearest3 = smg.getKNearest(n0, 3);
        assertEquals(3, n0nearest3.size());
        assertEquals(new Point2D(1, 1), n0nearest3.get(0).getAgent().getSpatialInformation());
        assertEquals(new Point2D(2, 2), n0nearest3.get(1).getAgent().getSpatialInformation());
        assertEquals(new Point2D(3, 3), n0nearest3.get(2).getAgent().getSpatialInformation());

        List<SocialGraph.Node> n100nearest2 = smg.getKNearest(n100, 2);
        assertEquals(2, n100nearest2.size());
        assertEquals(new Point2D(9, 9), n100nearest2.get(0).getAgent().getSpatialInformation());
        assertEquals(new Point2D(8, 8), n100nearest2.get(1).getAgent().getSpatialInformation());
    }
}