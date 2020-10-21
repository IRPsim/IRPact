package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.v2.commons.CollectionUtil;
import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class FastDirectedMultiGraphTest {
    
    private static FastDirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> newGraph() {
        return new FastDirectedMultiGraph<>(
                new HashSet<>(),
                new HashMap<>(),
                new HashMap<>(),
                new HashMap<>(),
                new HashMap<>()
        );
    }

    @Test
    void testAddNode() {
        FastDirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> fdmg = newGraph();
        SimpleNode n0 = new SimpleNode("0");
        SimpleNode n1 = new SimpleNode("1");
        fdmg.addNode(n0);
        assertThrows(NodeAlreadyExistsException.class, () -> fdmg.addNode(n0));
        fdmg.addNode(n1);
    }

    @Test
    void testAddEdge() {
        FastDirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> fdmg = newGraph();
        SimpleNode n0 = new SimpleNode("0");
        SimpleNode n1 = new SimpleNode("1");
        SimpleEdge<SimpleNode> e01 = new SimpleEdge<>(n0, n1, "e");
        fdmg.addEdge(e01, "x");
        assertThrows(EdgeAlreadyExistsException.class, () -> fdmg.addEdge(e01, "x"));
        fdmg.addEdge(e01, "y");
    }

    @Test
    void testHasNode() {
        FastDirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> fdmg = newGraph();
        SimpleNode n0 = new SimpleNode("0");
        SimpleNode n1 = new SimpleNode("1");
        assertFalse(fdmg.hasNode(n0));
        assertFalse(fdmg.hasNode(n1));
        fdmg.addNode(n0);
        assertTrue(fdmg.hasNode(n0));
        assertFalse(fdmg.hasNode(n1));
    }

    @Test
    void testHasEdge() {
        FastDirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> fdmg = newGraph();
        SimpleNode n0 = new SimpleNode("0");
        SimpleNode n1 = new SimpleNode("1");
        SimpleEdge<SimpleNode> e01 = new SimpleEdge<>(n0, n1, "e");
        assertFalse(fdmg.hasEdge(e01, "x"));
        assertFalse(fdmg.hasEdge(e01, "y"));
        fdmg.addEdge(e01, "x");
        assertTrue(fdmg.hasEdge(e01, "x"));
        assertFalse(fdmg.hasEdge(e01, "y"));
    }

    @Test
    void testRemoveNode() {
        FastDirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> fdmg = newGraph();
        SimpleNode n0 = new SimpleNode("0");
        assertFalse(fdmg.removeNode(n0));
        fdmg.addNode(n0);
        assertTrue(fdmg.hasNode(n0));
        assertTrue(fdmg.removeNode(n0));
        assertFalse(fdmg.hasNode(n0));
        assertFalse(fdmg.scanNode(n0));
    }

    @Test
    void testRemoveEdge() {
        FastDirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> fdmg = newGraph();
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x");
        SimpleEdge<SimpleNode> s0_t1 = new SimpleEdge<>(s0, t1, "y");

        assertFalse(fdmg.removeEdge(s0_t0, "x"));
        fdmg.addEdge(s0_t0, "x");
        fdmg.addEdge(s0_t1, "y");
        assertSame(s0_t0, fdmg.getEdge(s0, t0, "x"));
        assertTrue(fdmg.hasEdge(s0_t0, "x"));
        assertTrue(fdmg.hasEdge(s0_t1, "y"));
        assertTrue(fdmg.scanEdge(s0_t0, "x"));
        assertTrue(fdmg.removeEdge(s0_t0, "x"));
        assertNull(fdmg.getEdge(s0, t0, "x"));
        assertFalse(fdmg.scanEdge(s0_t0, "x"));
        assertFalse(fdmg.hasEdge(s0_t0, "x"));
        assertTrue(fdmg.hasEdge(s0_t1, "y"));
        assertFalse(fdmg.removeEdge(s0_t0, "x"));
    }

    @Test
    void testRemoveEdgeWithSameEdgeInstance() {
        FastDirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> fdmg = newGraph();
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x");

        fdmg.addEdge(s0_t0, "x");
        fdmg.addEdge(s0_t0, "y");

        assertTrue(fdmg.hasEdge(s0_t0, "x"));
        assertTrue(fdmg.hasEdge(s0_t0, "y"));
        assertTrue(fdmg.scanEdge(s0_t0, "x"));
        assertTrue(fdmg.scanEdge(s0_t0, "y"));
        assertTrue(fdmg.scanEdge(s0_t0));

        assertTrue(fdmg.removeEdge(s0_t0, "x"));

        assertFalse(fdmg.hasEdge(s0_t0, "x"));
        assertTrue(fdmg.hasEdge(s0_t0, "y"));
        assertFalse(fdmg.scanEdge(s0_t0, "x"));
        assertTrue(fdmg.scanEdge(s0_t0, "y"));
        assertTrue(fdmg.scanEdge(s0_t0));

        assertFalse(fdmg.removeEdge(s0_t0, "x"));
        assertTrue(fdmg.removeEdge(s0_t0, "y"));

        assertFalse(fdmg.hasEdge(s0_t0, "x"));
        assertFalse(fdmg.hasEdge(s0_t0, "y"));
        assertFalse(fdmg.scanEdge(s0_t0, "x"));
        assertFalse(fdmg.scanEdge(s0_t0, "y"));
        assertFalse(fdmg.scanEdge(s0_t0));
    }

    @Test
    void testRemoveNodeComplex() {
        FastDirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> fdmg = newGraph();
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "y");
        fdmg.addEdge(s0_t0, "x");
        fdmg.addEdge(s1_t0, "y");
        assertTrue(fdmg.hasNode(t0));

        assertSame(s0_t0, fdmg.getEdge(s0, t0, "x"));
        assertTrue(fdmg.removeNode(t0));
        assertFalse(fdmg.hasNode(t0));
        assertFalse(fdmg.scanNode(t0));
        assertFalse(fdmg.hasEdge(s0_t0, "x"));
        assertFalse(fdmg.hasEdge(s1_t0, "y"));
        assertTrue(fdmg.hasNode(s0));
        assertTrue(fdmg.hasNode(s1));
        assertNull(fdmg.getEdge(s0, t0, "x"));
    }

    @Test
    void testDegree() {
        FastDirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> fdmg = newGraph();
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "x1");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "y");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "y");
        fdmg.addEdge(s0_t0, "x");
        fdmg.addEdge(s1_t0, "x");
        fdmg.addEdge(s1_t1, "y");
        fdmg.addEdge(t1_s1, "y");

        assertEquals(1, fdmg.getDegree(s0, "x")); //s0-t0
        assertEquals(1, fdmg.getDegree(s1, "x")); //s1-t0
        assertEquals(2, fdmg.getDegree(s1, "y")); //s1-t1, t1-s1
        assertEquals(2, fdmg.getDegree(t0, "x")); //s0-t0, s1-t0
        assertEquals(2, fdmg.getDegree(t1, "y")); //s1-t1, t1-s1
        assertEquals(0, fdmg.getDegree(t1, "x"));
        assertEquals(-1, fdmg.getDegree(new SimpleNode(""), "z")); //da note nicht existiert
    }

    @SuppressWarnings("unchecked")
    @Test
    void testInEdges() {
        FastDirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> fdmg = newGraph();
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "x1");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "y");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "y");
        fdmg.addEdge(s0_t0, "x");
        fdmg.addEdge(s1_t0, "x");
        fdmg.addEdge(s1_t1, "x");
        fdmg.addEdge(t1_s1, "x");

        assertEquals(
                CollectionUtil.hashSetOf(t1_s1),
                fdmg.getInEdges(s1, "x")
        );

        assertEquals(
                CollectionUtil.hashSetOf(s0_t0, s1_t0),
                fdmg.getInEdges(t0, "x")
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    void testOutEdges() {
        FastDirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> fdmg = newGraph();
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "x1");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "y");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "y");
        fdmg.addEdge(s0_t0, "x");
        fdmg.addEdge(s1_t0, "x");
        fdmg.addEdge(s1_t1, "x");
        fdmg.addEdge(t1_s1, "x");

        assertEquals(
                CollectionUtil.hashSetOf(s1_t0, s1_t1),
                fdmg.getOutEdges(s1, "x")
        );

        assertEquals(
                CollectionUtil.hashSetOf(t1_s1),
                fdmg.getOutEdges(t1, "x")
        );
    }

    @Test
    void testGetNodes() {
        FastDirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> fdmg = newGraph();
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "x1");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "y");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "y");
        fdmg.addEdge(s0_t0, "x");
        fdmg.addEdge(s1_t0, "x");
        fdmg.addEdge(s1_t1, "x");
        fdmg.addEdge(t1_s1, "x");

        assertEquals(
                CollectionUtil.hashSetOf(s0, s1, t0, t1),
                fdmg.getNodes()
        );
    }

    @Test
    void testGetEdge() {
        FastDirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> fdmg = newGraph();
        SimpleNode a = new SimpleNode("a");
        SimpleNode b = new SimpleNode("b");
        SimpleEdge<SimpleNode> ab = new SimpleEdge<>(a, b, "ab");

        assertNull(fdmg.getEdge(a, b, "x"));
        fdmg.addEdge(ab, "x");
        assertSame(ab, fdmg.getEdge(a, b, "x"));
        assertNull(fdmg.getEdge(a, b, "y"));
        assertNull(fdmg.getEdge(b, a, "x"));

        assertTrue(fdmg.removeEdge(ab, "x"));
        assertNull(fdmg.getEdge(a, b, "x"));
    }

    @Test
    void testGetSourceNodes() {
        FastDirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> fdmg = newGraph();
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "s0t0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "s1t0");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "s1t1");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "t1s1");
        SimpleEdge<SimpleNode> t1_t0 = new SimpleEdge<>(t1, t0, "t1t0");
        fdmg.addEdge(s0_t0, "x");
        fdmg.addEdge(s1_t0, "x");
        fdmg.addEdge(s1_t1, "x");
        fdmg.addEdge(t1_s1, "x");
        fdmg.addEdge(t1_t0, "y");

        assertEquals(
                CollectionUtil.hashSetOf(s0, s1),
                fdmg.getSourceNodes(t0, "x")
        );
    }

    @Test
    void testStreamSourceNodes() {
        FastDirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> fdmg = newGraph();
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "s0t0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "s1t0");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "s1t1");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "t1s1");
        SimpleEdge<SimpleNode> t1_t0 = new SimpleEdge<>(t1, t0, "t1t0");
        fdmg.addEdge(s0_t0, "x");
        fdmg.addEdge(s1_t0, "x");
        fdmg.addEdge(s1_t1, "x");
        fdmg.addEdge(t1_s1, "x");
        fdmg.addEdge(t1_t0, "y");

        assertEquals(
                CollectionUtil.hashSetOf(s0, s1),
                fdmg.streamSourceNodes(t0, "x").collect(Collectors.toSet())
        );
    }

    @Test
    void testGetTargetNodes() {
        FastDirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> fdmg = newGraph();
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "s0t0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "s1t0");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "s1t1");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "t1s1");
        SimpleEdge<SimpleNode> t1_t0 = new SimpleEdge<>(t1, t0, "t1t0");
        SimpleEdge<SimpleNode> s1_s0 = new SimpleEdge<>(s1, s0, "s1s0");
        fdmg.addEdge(s0_t0, "x");
        fdmg.addEdge(s1_t0, "x");
        fdmg.addEdge(s1_t1, "x");
        fdmg.addEdge(t1_s1, "x");
        fdmg.addEdge(t1_t0, "y");
        fdmg.addEdge(s1_s0, "y");

        assertEquals(
                CollectionUtil.hashSetOf(t0, t1),
                fdmg.getTargetNodes(s1, "x")
        );
    }
}