package de.unileipzig.irpact.commons.graph.structure;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.commons.exception.EdgeAlreadyExistsException;
import de.unileipzig.irpact.commons.exception.NodeAlreadyExistsException;
import de.unileipzig.irpact.commons.graph.SimpleEdge;
import de.unileipzig.irpact.commons.graph.SimpleNode;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class SimpleDirectedMultiGraphStructureTest {

    private static SimpleDirectedMultiGraphStructure<SimpleNode, SimpleEdge<SimpleNode>, String> newGraphStructure() {
        return new SimpleDirectedMultiGraphStructure<>(
                HashMap::new,
                HashMap::new,
                new HashMap<>()
        );
    }

    @Test
    void testAddNode() {
        SimpleDirectedMultiGraphStructure<SimpleNode, SimpleEdge<SimpleNode>, String> gs = newGraphStructure();
        SimpleNode n0 = new SimpleNode("0");
        SimpleNode n1 = new SimpleNode("1");
        gs.addNode(n0);
        assertThrows(NodeAlreadyExistsException.class, () -> gs.addNode(n0));
        gs.addNode(n1);
    }

    @Test
    void testAddEdge() {
        SimpleDirectedMultiGraphStructure<SimpleNode, SimpleEdge<SimpleNode>, String> gs = newGraphStructure();
        SimpleNode n0 = new SimpleNode("0");
        SimpleNode n1 = new SimpleNode("1");
        SimpleEdge<SimpleNode> e01 = new SimpleEdge<>(n0, n1, "e");
        gs.addEdge(e01, "x");
        assertThrows(EdgeAlreadyExistsException.class, () -> gs.addEdge(e01, "x"));
        gs.addEdge(e01, "y");
    }

    @Test
    void testHasNode() {
        SimpleDirectedMultiGraphStructure<SimpleNode, SimpleEdge<SimpleNode>, String> gs = newGraphStructure();
        SimpleNode n0 = new SimpleNode("0");
        SimpleNode n1 = new SimpleNode("1");
        assertFalse(gs.hasNode(n0));
        assertFalse(gs.hasNode(n1));
        gs.addNode(n0);
        assertTrue(gs.hasNode(n0));
        assertFalse(gs.hasNode(n1));
    }

    @Test
    void testHasEdge() {
        SimpleDirectedMultiGraphStructure<SimpleNode, SimpleEdge<SimpleNode>, String> gs = newGraphStructure();
        SimpleNode n0 = new SimpleNode("0");
        SimpleNode n1 = new SimpleNode("1");
        SimpleEdge<SimpleNode> e01 = new SimpleEdge<>(n0, n1, "e");
        assertFalse(gs.hasEdge(e01, "x"));
        assertFalse(gs.hasEdge(e01, "y"));
        gs.addEdge(e01, "x");
        assertTrue(gs.hasEdge(e01, "x"));
        assertFalse(gs.hasEdge(e01, "y"));
    }

    @Test
    void testRemoveNode() {
        SimpleDirectedMultiGraphStructure<SimpleNode, SimpleEdge<SimpleNode>, String> gs = newGraphStructure();
        SimpleNode n0 = new SimpleNode("0");
        assertFalse(gs.removeNode(n0));
        gs.addNode(n0);
        assertTrue(gs.hasNode(n0));
        assertTrue(gs.removeNode(n0));
        assertFalse(gs.hasNode(n0));
        assertFalse(gs.scanNode(n0));
    }

    @Test
    void testRemoveEdge() {
        SimpleDirectedMultiGraphStructure<SimpleNode, SimpleEdge<SimpleNode>, String> gs = newGraphStructure();
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x");
        SimpleEdge<SimpleNode> s0_t1 = new SimpleEdge<>(s0, t1, "y");

        assertFalse(gs.removeEdge(s0_t0, "x"));
        gs.addEdge(s0_t0, "x");
        gs.addEdge(s0_t1, "y");
        assertSame(s0_t0, gs.getEdge(s0, t0, "x"));
        assertTrue(gs.hasEdge(s0_t0, "x"));
        assertTrue(gs.hasEdge(s0_t1, "y"));
        assertTrue(gs.scanEdge(s0_t0, "x"));
        assertTrue(gs.removeEdge(s0_t0, "x"));
        assertNull(gs.getEdge(s0, t0, "x"));
        assertFalse(gs.scanEdge(s0_t0, "x"));
        assertFalse(gs.hasEdge(s0_t0, "x"));
        assertTrue(gs.hasEdge(s0_t1, "y"));
        assertFalse(gs.removeEdge(s0_t0, "x"));
    }

    @Test
    void testRemoveEdgeWithSameEdgeInstance() {
        SimpleDirectedMultiGraphStructure<SimpleNode, SimpleEdge<SimpleNode>, String> gs = newGraphStructure();
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x");

        gs.addEdge(s0_t0, "x");
        gs.addEdge(s0_t0, "y");

        assertTrue(gs.hasEdge(s0_t0, "x"));
        assertTrue(gs.hasEdge(s0_t0, "y"));
        assertTrue(gs.scanEdge(s0_t0, "x"));
        assertTrue(gs.scanEdge(s0_t0, "y"));
        assertTrue(gs.scanEdge(s0_t0));

        assertTrue(gs.removeEdge(s0_t0, "x"));

        assertFalse(gs.hasEdge(s0_t0, "x"));
        assertTrue(gs.hasEdge(s0_t0, "y"));
        assertFalse(gs.scanEdge(s0_t0, "x"));
        assertTrue(gs.scanEdge(s0_t0, "y"));
        assertTrue(gs.scanEdge(s0_t0));

        assertFalse(gs.removeEdge(s0_t0, "x"));
        assertTrue(gs.removeEdge(s0_t0, "y"));

        assertFalse(gs.hasEdge(s0_t0, "x"));
        assertFalse(gs.hasEdge(s0_t0, "y"));
        assertFalse(gs.scanEdge(s0_t0, "x"));
        assertFalse(gs.scanEdge(s0_t0, "y"));
        assertFalse(gs.scanEdge(s0_t0));
    }

    @Test
    void testRemoveNodeComplex() {
        SimpleDirectedMultiGraphStructure<SimpleNode, SimpleEdge<SimpleNode>, String> gs = newGraphStructure();
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "y");
        gs.addEdge(s0_t0, "x");
        gs.addEdge(s1_t0, "y");
        assertTrue(gs.hasNode(t0));

        assertSame(s0_t0, gs.getEdge(s0, t0, "x"));
        assertTrue(gs.removeNode(t0));
        assertFalse(gs.hasNode(t0));
        assertFalse(gs.scanNode(t0));
        assertFalse(gs.hasEdge(s0_t0, "x"));
        assertFalse(gs.hasEdge(s1_t0, "y"));
        assertTrue(gs.hasNode(s0));
        assertTrue(gs.hasNode(s1));
        assertNull(gs.getEdge(s0, t0, "x"));
    }

    @Test
    void testDegree() {
        SimpleDirectedMultiGraphStructure<SimpleNode, SimpleEdge<SimpleNode>, String> gs = newGraphStructure();
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "x1");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "y");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "y");
        gs.addEdge(s0_t0, "x");
        gs.addEdge(s1_t0, "x");
        gs.addEdge(s1_t1, "y");
        gs.addEdge(t1_s1, "y");

        assertEquals(1, gs.getDegree(s0, "x")); //s0-t0
        assertEquals(1, gs.getDegree(s1, "x")); //s1-t0
        assertEquals(2, gs.getDegree(s1, "y")); //s1-t1, t1-s1
        assertEquals(2, gs.getDegree(t0, "x")); //s0-t0, s1-t0
        assertEquals(2, gs.getDegree(t1, "y")); //s1-t1, t1-s1
        assertEquals(0, gs.getDegree(t1, "x"));
        assertEquals(-1, gs.getDegree(new SimpleNode(""), "z")); //da note nicht existiert
    }

    @SuppressWarnings("unchecked")
    @Test
    void testInEdges() {
        SimpleDirectedMultiGraphStructure<SimpleNode, SimpleEdge<SimpleNode>, String> gs = newGraphStructure();
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "x1");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "y");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "y");
        gs.addEdge(s0_t0, "x");
        gs.addEdge(s1_t0, "x");
        gs.addEdge(s1_t1, "x");
        gs.addEdge(t1_s1, "x");

        assertEquals(
                CollectionUtil.hashSetOf(t1_s1),
                gs.getInEdges(s1, "x")
        );

        assertEquals(
                CollectionUtil.hashSetOf(s0_t0, s1_t0),
                gs.getInEdges(t0, "x")
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    void testOutEdges() {
        SimpleDirectedMultiGraphStructure<SimpleNode, SimpleEdge<SimpleNode>, String> gs = newGraphStructure();
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "x1");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "y");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "y");
        gs.addEdge(s0_t0, "x");
        gs.addEdge(s1_t0, "x");
        gs.addEdge(s1_t1, "x");
        gs.addEdge(t1_s1, "x");

        assertEquals(
                CollectionUtil.hashSetOf(s1_t0, s1_t1),
                gs.getOutEdges(s1, "x")
        );

        assertEquals(
                CollectionUtil.hashSetOf(t1_s1),
                gs.getOutEdges(t1, "x")
        );
    }

    @Test
    void testGetNodes() {
        SimpleDirectedMultiGraphStructure<SimpleNode, SimpleEdge<SimpleNode>, String> gs = newGraphStructure();
        SimpleNode s0 = new SimpleNode("s0");
        SimpleNode s1 = new SimpleNode("s1");
        SimpleNode t0 = new SimpleNode("t0");
        SimpleNode t1 = new SimpleNode("t1");
        SimpleEdge<SimpleNode> s0_t0 = new SimpleEdge<>(s0, t0, "x0");
        SimpleEdge<SimpleNode> s1_t0 = new SimpleEdge<>(s1, t0, "x1");
        SimpleEdge<SimpleNode> s1_t1 = new SimpleEdge<>(s1, t1, "y");
        SimpleEdge<SimpleNode> t1_s1 = new SimpleEdge<>(t1, s1, "y");
        gs.addEdge(s0_t0, "x");
        gs.addEdge(s1_t0, "x");
        gs.addEdge(s1_t1, "x");
        gs.addEdge(t1_s1, "x");

        assertEquals(
                CollectionUtil.hashSetOf(s0, s1, t0, t1),
                gs.getNodes()
        );
    }

    @Test
    void testGetEdge() {
        SimpleDirectedMultiGraphStructure<SimpleNode, SimpleEdge<SimpleNode>, String> gs = newGraphStructure();
        SimpleNode a = new SimpleNode("a");
        SimpleNode b = new SimpleNode("b");
        SimpleEdge<SimpleNode> ab = new SimpleEdge<>(a, b, "ab");

        assertNull(gs.getEdge(a, b, "x"));
        gs.addEdge(ab, "x");
        assertSame(ab, gs.getEdge(a, b, "x"));
        assertNull(gs.getEdge(a, b, "y"));
        assertNull(gs.getEdge(b, a, "x"));

        assertTrue(gs.removeEdge(ab, "x"));
        assertNull(gs.getEdge(a, b, "x"));
    }
}