package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.v2.commons.CollectionUtil;
import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class DirectedMultiGraph2Test {

    @Test
    void testType() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        assertTrue(dmg.isDirected());
        assertFalse(dmg.isUndirected());
    }

    @Test
    void testAddNode() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode n0 = new SimpleNode("x");
        assertFalse(dmg.hasNode(n0));
        dmg.addNode(n0);
        assertTrue(dmg.hasNode(n0));
        assertThrows(NodeAlreadyExistsException.class, () -> dmg.addNode(n0));
    }

    /*
    //ueberlegen, ob selbe edge instanz mehrmals hinzugefuegt werden darf siehe todo
    @Test
    void testAddEdge() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
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
    */

    @Test
    void testGetEdge() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode n0 = new SimpleNode("x");
        SimpleNode n1 = new SimpleNode("y");
        SimpleEdge<SimpleNode> e = new SimpleEdge<>(n0, n1, "z");

        assertNull(dmg.getEdge(n0, n1, "abc"));
        dmg.addEdge(e, "abc");
        assertSame(e, dmg.getEdge(n0, n1, "abc"));
    }

    @Test
    void testRemoveNode() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode n = new SimpleNode("x");
        assertFalse(dmg.removeNode(n));
        dmg.addNode(n);
        assertTrue(dmg.hasNode(n));
        assertTrue(dmg.removeNode(n));
        assertFalse(dmg.removeNode(n));
    }

    @Test
    void testRemoveInterNode() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
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
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
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

    @Test
    void testAddNode2() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode n0 = new SimpleNode("0");
        SimpleNode n1 = new SimpleNode("1");
        dmg.addNode(n0);
        assertThrows(NodeAlreadyExistsException.class, () -> dmg.addNode(n0));
        dmg.addNode(n1);
    }

    @Test
    void testAddEdge2() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode n0 = new SimpleNode("0");
        SimpleNode n1 = new SimpleNode("1");
        SimpleEdge<SimpleNode> e01 = new SimpleEdge<>(n0, n1, "e");
        dmg.addEdge(e01, "x");
        assertThrows(EdgeAlreadyExistsException.class, () -> dmg.addEdge(e01, "x"));
        dmg.addEdge(e01, "y");
    }

    @Test
    void testHasNode() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode n0 = new SimpleNode("0");
        SimpleNode n1 = new SimpleNode("1");
        assertFalse(dmg.hasNode(n0));
        assertFalse(dmg.hasNode(n1));
        dmg.addNode(n0);
        assertTrue(dmg.hasNode(n0));
        assertFalse(dmg.hasNode(n1));
    }

    @Test
    void testHasEdge() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode n0 = new SimpleNode("0");
        SimpleNode n1 = new SimpleNode("1");
        SimpleEdge<SimpleNode> e01 = new SimpleEdge<>(n0, n1, "e");
        assertFalse(dmg.hasEdge(e01, "x"));
        assertFalse(dmg.hasEdge(e01, "y"));
        dmg.addEdge(e01, "x");
        assertTrue(dmg.hasEdge(e01, "x"));
        assertFalse(dmg.hasEdge(e01, "y"));
    }

    @Test
    void testRemoveNode2() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode n0 = new SimpleNode("0");
        assertFalse(dmg.removeNode(n0));
        dmg.addNode(n0);
        assertTrue(dmg.hasNode(n0));
        assertTrue(dmg.removeNode(n0));
        assertFalse(dmg.hasNode(n0));
        assertFalse(dmg.scanNode(n0));
    }

    @Test
    void testRemoveEdge2() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x");
        SimpleEdge<SimpleNode> s0_t1 = new SimpleEdge<>(s0, t1, "y");

        assertFalse(dmg.removeEdge(s0_t0, "x"));
        dmg.addEdge(s0_t0, "x");
        dmg.addEdge(s0_t1, "y");
        assertSame(s0_t0, dmg.getEdge(s0, t0, "x"));
        assertTrue(dmg.hasEdge(s0_t0, "x"));
        assertTrue(dmg.hasEdge(s0_t1, "y"));
        assertTrue(dmg.scanEdge(s0_t0, "x"));
        assertTrue(dmg.removeEdge(s0_t0, "x"));
        assertNull(dmg.getEdge(s0, t0, "x"));
        assertFalse(dmg.scanEdge(s0_t0, "x"));
        assertFalse(dmg.hasEdge(s0_t0, "x"));
        assertTrue(dmg.hasEdge(s0_t1, "y"));
        assertFalse(dmg.removeEdge(s0_t0, "x"));
    }

    @Test
    void testRemoveEdgeWithSameEdgeInstance() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x");

        dmg.addEdge(s0_t0, "x");
        dmg.addEdge(s0_t0, "y");

        assertTrue(dmg.hasEdge(s0_t0, "x"));
        assertTrue(dmg.hasEdge(s0_t0, "y"));
        assertTrue(dmg.scanEdge(s0_t0, "x"));
        assertTrue(dmg.scanEdge(s0_t0, "y"));
        assertTrue(dmg.scanEdge(s0_t0));

        assertTrue(dmg.removeEdge(s0_t0, "x"));

        assertFalse(dmg.hasEdge(s0_t0, "x"));
        assertTrue(dmg.hasEdge(s0_t0, "y"));
        assertFalse(dmg.scanEdge(s0_t0, "x"));
        assertTrue(dmg.scanEdge(s0_t0, "y"));
        assertTrue(dmg.scanEdge(s0_t0));

        assertFalse(dmg.removeEdge(s0_t0, "x"));
        assertTrue(dmg.removeEdge(s0_t0, "y"));

        assertFalse(dmg.hasEdge(s0_t0, "x"));
        assertFalse(dmg.hasEdge(s0_t0, "y"));
        assertFalse(dmg.scanEdge(s0_t0, "x"));
        assertFalse(dmg.scanEdge(s0_t0, "y"));
        assertFalse(dmg.scanEdge(s0_t0));
    }

    @Test
    void testRemoveNodeComplex() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "y");
        dmg.addEdge(s0_t0, "x");
        dmg.addEdge(s1_t0, "y");
        assertTrue(dmg.hasNode(t0));

        assertSame(s0_t0, dmg.getEdge(s0, t0, "x"));
        assertTrue(dmg.removeNode(t0));
        assertFalse(dmg.hasNode(t0));
        assertFalse(dmg.scanNode(t0));
        assertFalse(dmg.hasEdge(s0_t0, "x"));
        assertFalse(dmg.hasEdge(s1_t0, "y"));
        assertTrue(dmg.hasNode(s0));
        assertTrue(dmg.hasNode(s1));
        assertNull(dmg.getEdge(s0, t0, "x"));
    }

    @Test
    void testDegree() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "x1");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "y");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "y");
        dmg.addEdge(s0_t0, "x");
        dmg.addEdge(s1_t0, "x");
        dmg.addEdge(s1_t1, "y");
        dmg.addEdge(t1_s1, "y");

        assertEquals(1, dmg.getDegree(s0, "x")); //s0-t0
        assertEquals(1, dmg.getDegree(s1, "x")); //s1-t0
        assertEquals(2, dmg.getDegree(s1, "y")); //s1-t1, t1-s1
        assertEquals(2, dmg.getDegree(t0, "x")); //s0-t0, s1-t0
        assertEquals(2, dmg.getDegree(t1, "y")); //s1-t1, t1-s1
        assertEquals(0, dmg.getDegree(t1, "x"));
        assertEquals(-1, dmg.getDegree(new SimpleNode(""), "z")); //da note nicht existiert
    }

    @SuppressWarnings("unchecked")
    @Test
    void testInEdges() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "x1");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "y");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "y");
        dmg.addEdge(s0_t0, "x");
        dmg.addEdge(s1_t0, "x");
        dmg.addEdge(s1_t1, "x");
        dmg.addEdge(t1_s1, "x");

        assertEquals(
                CollectionUtil.hashSetOf(t1_s1),
                dmg.getInEdges(s1, "x")
        );

        assertEquals(
                CollectionUtil.hashSetOf(s0_t0, s1_t0),
                dmg.getInEdges(t0, "x")
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    void testOutEdges() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "x1");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "y");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "y");
        dmg.addEdge(s0_t0, "x");
        dmg.addEdge(s1_t0, "x");
        dmg.addEdge(s1_t1, "x");
        dmg.addEdge(t1_s1, "x");

        assertEquals(
                CollectionUtil.hashSetOf(s1_t0, s1_t1),
                dmg.getOutEdges(s1, "x")
        );

        assertEquals(
                CollectionUtil.hashSetOf(t1_s1),
                dmg.getOutEdges(t1, "x")
        );
    }

    @Test
    void testGetNodes() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "x1");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "y");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "y");
        dmg.addEdge(s0_t0, "x");
        dmg.addEdge(s1_t0, "x");
        dmg.addEdge(s1_t1, "x");
        dmg.addEdge(t1_s1, "x");

        assertEquals(
                CollectionUtil.hashSetOf(s0, s1, t0, t1),
                dmg.getNodes()
        );
    }

    @Test
    void testGetEdge2() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode a = new SimpleNode("a");
        SimpleNode b = new SimpleNode("b");
        SimpleEdge<SimpleNode> ab = new SimpleEdge<>(a, b, "ab");

        assertNull(dmg.getEdge(a, b, "x"));
        dmg.addEdge(ab, "x");
        assertSame(ab, dmg.getEdge(a, b, "x"));
        assertNull(dmg.getEdge(a, b, "y"));
        assertNull(dmg.getEdge(b, a, "x"));

        assertTrue(dmg.removeEdge(ab, "x"));
        assertNull(dmg.getEdge(a, b, "x"));
    }

    @Test
    void testGetSourceNodes() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "s0t0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "s1t0");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "s1t1");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "t1s1");
        SimpleEdge<SimpleNode> t1_t0 = new SimpleEdge<>(t1, t0, "t1t0");
        dmg.addEdge(s0_t0, "x");
        dmg.addEdge(s1_t0, "x");
        dmg.addEdge(s1_t1, "x");
        dmg.addEdge(t1_s1, "x");
        dmg.addEdge(t1_t0, "y");

        assertEquals(
                CollectionUtil.hashSetOf(s0, s1),
                dmg.getSourceNodes(t0, "x")
        );
    }

    @Test
    void testStreamSourceNodes() {
        DirectedMultiGraph2<SimpleNode, SimpleEdge<SimpleNode>, String> dmg = new DirectedMultiGraph2<>(new HashSet<>(), new HashMap<>());
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "s0t0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "s1t0");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "s1t1");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "t1s1");
        SimpleEdge<SimpleNode> t1_t0 = new SimpleEdge<>(t1, t0, "t1t0");
        dmg.addEdge(s0_t0, "x");
        dmg.addEdge(s1_t0, "x");
        dmg.addEdge(s1_t1, "x");
        dmg.addEdge(t1_s1, "x");
        dmg.addEdge(t1_t0, "y");

        assertEquals(
                CollectionUtil.hashSetOf(s0, s1),
                dmg.streamSourceNodes(t0, "x").collect(Collectors.toSet())
        );
    }
}