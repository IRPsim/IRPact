package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.v2.commons.CollectionUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class DirectedGraphTest {

    @Test
    void testType() {
        DirectedGraph<Node, Edge<Node>> dg = new DirectedGraph<>(new HashMap<>());
        assertTrue(dg.isDirected());
        assertFalse(dg.isUndirected());
    }

    @Test
    void testScan() {
        SimpleNode n0 = new SimpleNode("0");
        SimpleNode n1 = new SimpleNode("1");
        SimpleNode n2 = new SimpleNode("2");
        SimpleNode n3 = new SimpleNode("3");
        SimpleEdge<SimpleNode> e01 = new SimpleEdge<>(n0, n1, "0-1");
        SimpleEdge<SimpleNode> e02 = new SimpleEdge<>(n0, n2, "0-2");
        SimpleEdge<SimpleNode> e03 = new SimpleEdge<>(n0, n3, "0-3");
        SimpleEdge<SimpleNode> e10 = new SimpleEdge<>(n1, n0, "0-1");
        SimpleEdge<SimpleNode> e20 = new SimpleEdge<>(n2, n0, "0-2");
        SimpleEdge<SimpleNode> e30 = new SimpleEdge<>(n3, n0, "0-3");
        DirectedGraph<SimpleNode, SimpleEdge<SimpleNode>> graph = new DirectedGraph<>(new HashMap<>());
        graph.addEdge(e01);
        graph.addEdge(e02);
        graph.addEdge(e03);
        graph.addEdge(e10);
        graph.addEdge(e20);
        graph.addEdge(e30);

        assertEquals(3, graph.graphData.get(n0).size());
        assertEquals(1, graph.graphData.get(n1).size());
        assertEquals(1, graph.graphData.get(n2).size());
        assertEquals(1, graph.graphData.get(n3).size());
        assertTrue(graph.scan(n0));
        assertTrue(graph.removeNode(n0));
        assertFalse(graph.scan(n0));
        assertEquals(0, graph.graphData.get(n1).size());
        assertEquals(0, graph.graphData.get(n2).size());
        assertEquals(0, graph.graphData.get(n3).size());
    }

    @Test
    void testDegree() {
        DirectedGraph<SimpleNode, SimpleEdge<SimpleNode>> dmg = new DirectedGraph<>(new HashMap<>());
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "x1");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "y");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "y");
        dmg.addEdge(s0_t0);
        dmg.addEdge(s1_t0);
        dmg.addEdge(s1_t1);
        dmg.addEdge(t1_s1);

        assertEquals(1, dmg.getDegree(s0));
        assertEquals(3, dmg.getDegree(s1));
        assertEquals(2, dmg.getDegree(t0));
        assertEquals(2, dmg.getDegree(t1));
        assertEquals(-1, dmg.getDegree(new SimpleNode(""))); //da note nicht existiert
    }

    @SuppressWarnings("unchecked")
    @Test
    void testInEdges() {
        DirectedGraph<SimpleNode, SimpleEdge<SimpleNode>> dmg = new DirectedGraph<>(new HashMap<>());
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "x1");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "y");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "y");
        dmg.addEdge(s0_t0);
        dmg.addEdge(s1_t0);
        dmg.addEdge(s1_t1);
        dmg.addEdge(t1_s1);

        assertEquals(
                CollectionUtil.hashSetOf(t1_s1),
                dmg.getInEdges(s1)
        );

        assertEquals(
                CollectionUtil.hashSetOf(s0_t0, s1_t0),
                dmg.getInEdges(t0)
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    void testOutEdges() {
        DirectedGraph<SimpleNode, SimpleEdge<SimpleNode>> dmg = new DirectedGraph<>(new HashMap<>());
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "x1");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "y");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "y");
        dmg.addEdge(s0_t0);
        dmg.addEdge(s1_t0);
        dmg.addEdge(s1_t1);
        dmg.addEdge(t1_s1);

        assertEquals(
                CollectionUtil.hashSetOf(s1_t0, s1_t1),
                dmg.getOutEdges(s1)
        );

        assertEquals(
                CollectionUtil.hashSetOf(t1_s1),
                dmg.getOutEdges(t1)
        );
    }

    @Test
    void testGetNodes() {
        DirectedGraph<SimpleNode, SimpleEdge<SimpleNode>> dmg = new DirectedGraph<>(new HashMap<>());
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "x1");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "y");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "y");
        dmg.addEdge(s0_t0);
        dmg.addEdge(s1_t0);
        dmg.addEdge(s1_t1);
        dmg.addEdge(t1_s1);

        assertEquals(
                CollectionUtil.hashSetOf(s0, s1, t0, t1),
                dmg.getNodes()
        );
    }

    @Test
    void testGetEdge2() {
        DirectedGraph<SimpleNode, SimpleEdge<SimpleNode>> dmg = new DirectedGraph<>(new HashMap<>());
        SimpleNode a = new SimpleNode("a");
        SimpleNode b = new SimpleNode("b");
        SimpleEdge<SimpleNode> ab = new SimpleEdge<>(a, b, "ab");

        assertNull(dmg.getEdge(a, b));
        dmg.addEdge(ab);
        assertSame(ab, dmg.getEdge(a, b));
        assertNull(dmg.getEdge(b, a));

        assertTrue(dmg.removeEdge(ab));
        assertNull(dmg.getEdge(a, b));
    }

    @Test
    void testGetSourceNodes() {
        DirectedGraph<SimpleNode, SimpleEdge<SimpleNode>> dmg = new DirectedGraph<>(new HashMap<>());
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "s0t0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "s1t0");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "s1t1");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "t1s1");
        SimpleEdge<SimpleNode> t1_t0 = new SimpleEdge<>(t1, t0, "t1t0");
        dmg.addEdge(s0_t0);
        dmg.addEdge(s1_t0);
        dmg.addEdge(s1_t1);
        dmg.addEdge(t1_s1);
        dmg.addEdge(t1_t0);

        assertEquals(
                CollectionUtil.hashSetOf(s0, s1, t1),
                dmg.getSourceNodes(t0)
        );
    }

    @Test
    void testStreamSourceNodes() {
        DirectedGraph<SimpleNode, SimpleEdge<SimpleNode>> dmg = new DirectedGraph<>(new HashMap<>());
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "s0t0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "s1t0");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "s1t1");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "t1s1");
        SimpleEdge<SimpleNode> t1_t0 = new SimpleEdge<>(t1, t0, "t1t0");
        dmg.addEdge(s0_t0);
        dmg.addEdge(s1_t0);
        dmg.addEdge(s1_t1);
        dmg.addEdge(t1_s1);
        dmg.addEdge(t1_t0);

        assertEquals(
                CollectionUtil.hashSetOf(s0, s1, t1),
                dmg.streamSourceNodes(t0).collect(Collectors.toSet())
        );
    }

    @Test
    void testGetTargetNodes() {
        DirectedGraph<SimpleNode, SimpleEdge<SimpleNode>> dmg = new DirectedGraph<>(new HashMap<>());
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
        dmg.addEdge(s0_t0);
        dmg.addEdge(s1_t0);
        dmg.addEdge(s1_t1);
        dmg.addEdge(t1_s1);
        dmg.addEdge(t1_t0);
        dmg.addEdge(s1_s0);

        assertEquals(
                CollectionUtil.hashSetOf(t0, t1, s0),
                dmg.getTargetNodes(s1)
        );
    }
}