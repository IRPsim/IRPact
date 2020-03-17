package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.core.network.exception.NodeWithSameAgentException;
import de.unileipzig.irpact.mock.MockConsumerAgent;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class SocialNetworkTest {

    @Test
    void testAddAgent() {
        SocialNetwork smg = new SocialNetwork();
        MockConsumerAgent a = new MockConsumerAgent("1");
        assertFalse(smg.hasAgent(a));
        smg.addAgent(a);
        assertTrue(smg.hasAgent(a));
        assertThrows(NodeWithSameAgentException.class, () -> smg.addAgent(a));
    }

    @Test
    void testGetAndFindAgent() {
        SocialNetwork smg = new SocialNetwork();
        MockConsumerAgent a = new MockConsumerAgent("1");
        assertNull(smg.getNode(a));
        assertThrows(NoSuchElementException.class, () -> smg.findNode(a));
        smg.addAgent(a);
        assertSame(a, smg.getNode(a).getAgent());
        assertSame(a, smg.findNode(a).getAgent());
        assertSame(smg.getNode(a), smg.findNode(a));
    }

    @Test
    void testAgentExistsIfEdgeAdded() {
        SocialNetwork smg = new SocialNetwork();
        MockConsumerAgent a1 = new MockConsumerAgent("1");
        MockConsumerAgent a2 = new MockConsumerAgent("1");
        SocialNetwork.Node n1 = new SocialNetwork.Node(a1);
        SocialNetwork.Node n2 = new SocialNetwork.Node(a2);
        SocialNetwork.Edge e = new SocialNetwork.Edge(n1, n2, "x", 0);

        assertFalse(smg.hasAgent(a1));
        assertFalse(smg.hasAgent(a2));

        assertFalse(smg.hasEdge(e, EdgeType.COMMUNICATION));
        smg.addEdge(e, EdgeType.COMMUNICATION);
        assertTrue(smg.hasEdge(e, EdgeType.COMMUNICATION));
        assertFalse(smg.hasEdge(e, EdgeType.TRUST));

        assertTrue(smg.hasAgent(a1));
        assertTrue(smg.hasAgent(a2));
    }
}