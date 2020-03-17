package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class DirectedMultiGraphTest {

    @Test
    void testType() {
        DirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph<>(new HashMap<>());
        assertTrue(dmg.isDirected());
        assertFalse(dmg.isUndirected());
    }

    @Test
    void testAddNode() {
        DirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph<>(new HashMap<>());
        SimpleNode n0 = new SimpleNode("x");
        assertFalse(dmg.hasNode(n0));
        dmg.addNode(n0);
        assertTrue(dmg.hasNode(n0));
        assertThrows(NodeAlreadyExistsException.class, () -> dmg.addNode(n0));
    }

    @Test
    void testAddEdge() {
        DirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph<>(new HashMap<>());
        SimpleNode n0 = new SimpleNode("x");
        SimpleNode n1 = new SimpleNode("y");
        SimpleEdge<SimpleNode> e = new SimpleEdge<>(n0, n1, "z");
        assertFalse(dmg.hasEdge(e, "abc"));
        dmg.addEdge(e, "abc");
        assertTrue(dmg.hasEdge(e, "abc"));
        assertFalse(dmg.hasEdge(e, "efg"));
        assertThrows(EdgeAlreadyExistsException.class, () -> dmg.addEdge(e, "abc"));
        assertThrows(EdgeAlreadyExistsException.class, () -> dmg.addEdge(e, "efg"));
        SimpleEdge<SimpleNode> e2 = new SimpleEdge<>(n0, n1, "z2");
        assertThrows(EdgeAlreadyExistsException.class, () -> dmg.addEdge(e2, "abc"));
        dmg.addEdge(e2, "efg");
    }

    @Test
    void testGetEdge() {
        DirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph<>(new HashMap<>());
        SimpleNode n0 = new SimpleNode("x");
        SimpleNode n1 = new SimpleNode("y");
        SimpleEdge<SimpleNode> e = new SimpleEdge<>(n0, n1, "z");

        assertNull(dmg.getEdge(n0, n1, "abc"));
        dmg.addEdge(e, "abc");
        assertSame(e, dmg.getEdge(n0, n1, "abc"));
    }

    @Test
    void testRemoveNode() {
        DirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph<>(new HashMap<>());
        SimpleNode n = new SimpleNode("x");
        assertFalse(dmg.removeNode(n));
        dmg.addNode(n);
        assertTrue(dmg.hasNode(n));
        assertTrue(dmg.removeNode(n));
        assertFalse(dmg.removeNode(n));
    }

    @Test
    void testRemoveInterNode() {
        DirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph<>(new HashMap<>());
        SimpleNode n0 = new SimpleNode("x");
        SimpleNode n1 = new SimpleNode("y");
        SimpleEdge<SimpleNode> e = new SimpleEdge<>(n0, n1, "z");
        dmg.addEdge(e, "abc");

        assertTrue(dmg.hasNode(n1));
        assertTrue(dmg.hasEdge(e, "abc"));
        assertTrue(dmg.removeNode(n1));
        assertFalse(dmg.hasNode(n1));
        assertFalse(dmg.hasEdge(e, "abc"));
        assertTrue(dmg.hasNode(n0));
    }

    @Test
    void testRemoveEdge() {
        DirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph<>(new HashMap<>());
        SimpleNode n0 = new SimpleNode("x");
        SimpleNode n1 = new SimpleNode("y");
        SimpleEdge<SimpleNode> e1 = new SimpleEdge<>(n0, n1, "z1");
        SimpleEdge<SimpleNode> e2 = new SimpleEdge<>(n0, n1, "z2");
        dmg.addEdge(e1, "abc");
        dmg.addEdge(e2, "efg");

        assertTrue(dmg.hasEdge(e1, "abc"));
        assertTrue(dmg.removeEdge(e1, "abc"));
        assertFalse(dmg.hasEdge(e1, "abc"));
        assertTrue(dmg.hasEdge(e2, "efg"));
    }
}