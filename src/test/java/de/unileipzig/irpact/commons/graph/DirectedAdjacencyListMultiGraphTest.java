package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.CollectionUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class DirectedAdjacencyListMultiGraphTest {

    @Test
    void testAddVertex() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        assertTrue(g.addVertex("a"));
        assertFalse(g.addVertex("a"));
    }

    @Test
    void testHasVertex() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        assertFalse(g.hasVertex("a"));
        assertTrue(g.addVertex("a"));
        assertTrue(g.hasVertex("a"));
    }

    @Test
    void testRemoveVertex() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        assertFalse(g.removeVertex("a"));
        assertTrue(g.addVertex("a"));
        assertTrue(g.removeVertex("a"));
    }

    @Test
    void testRemoveVertex2() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        g.addEdge("a", "b", "x", "1");
        assertTrue(g.removeVertex("b"));
        assertFalse(g.hasEdge("1"));
        assertFalse(g.hasVertex("b"));
        assertTrue(g.hasVertex("a"));
    }

    @Test
    void testAddEdge() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        assertTrue(g.addEdge("a", "b", "x", "1"));
        assertFalse(g.addEdge("a", "b", "x", "1"));
        assertFalse(g.addEdge("a", "b", "x", "2"));
        assertFalse(g.addEdge("a", "b", "y", "1"));
    }

    @Test
    void testSetEdge() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        assertNull(g.setEdge("a", "b", "x", "1"));
        assertEquals("1", g.setEdge("a", "b", "x", "2"));
        assertEquals("2", g.getEdge("a", "b", "x"));
    }

    @Test
    void testHasEdge() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        assertFalse(g.hasEdge("1"));
        assertFalse(g.hasEdge("a", "b", "x"));
        assertTrue(g.addEdge("a", "b", "x", "1"));
        assertTrue(g.hasEdge("1"));
        assertTrue(g.hasEdge("a", "b", "x"));
        assertEquals(1, g.edgeCount());
    }

    @Test
    void testRemoveEdge() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        assertTrue(g.addEdge("a", "b", "x", "1"));
        assertTrue(g.removeEdge("a", "b", "x"));
        assertFalse(g.removeEdge("a", "b", "x"));
        assertFalse(g.hasEdge("a", "b", "x"));
    }

    @Test
    void testRemoveEdge2() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        assertTrue(g.addEdge("a", "b", "x", "1"));
        assertTrue(g.removeEdge("1"));
        assertFalse(g.removeEdge("1"));
        assertFalse(g.hasEdge("a", "b", "x"));
    }

    @Test
    void testRemoveEdge3() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        assertTrue(g.addEdge("a", "b", "x", "1"));
        assertTrue(g.addEdge("c", "d", "x", "2"));
        assertEquals(2, g.edgeCount());
        assertTrue(g.removeEdge("1"));
        assertTrue(g.hasEdge("2"));
        assertEquals(1, g.edgeCount());
    }

    @Test
    void testGetEdge() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        assertTrue(g.addEdge("a", "b", "x", "1"));
        assertEquals("1", g.getEdge("a", "b", "x"));
        assertEquals("1", g.setEdge("a", "b", "x", "2"));
        assertEquals("2", g.getEdge("a", "b", "x"));
    }

    @Test
    void testGetEdge2() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        assertTrue(g.addEdge("a", "b", "x", "1"));
        assertTrue(g.addEdge("a", "b", "y", "2"));
        Map<String, String> edges = g.getEdges("a", "b");
        assertEquals(2, edges.size());
        assertEquals("1", edges.get("x"));
        assertEquals("2", edges.get("y"));
    }

    @Test
    void testGetTargets() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        g.addEdge("a", "b0", "x", "x_a_b0");
        g.addEdge("c", "d", "x", "x_c_d");
        g.addEdge("a", "b1", "x", "x_a_b1");
        g.addEdge("c", "e", "x", "x_c_e");
        g.addEdge("a", "b2", "y", "y_a_b2");

        assertEquals(
                CollectionUtil.hashSetOf("b0", "b1"),
                g.getTargets("a", "x")
        );
        assertEquals(
                CollectionUtil.hashSetOf("d", "e"),
                g.getTargets("c", "x")
        );
        assertEquals(
                CollectionUtil.hashSetOf("b2"),
                g.getTargets("a", "y")
        );
    }

    @Test
    void testGetEdges() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        g.addEdge("a", "b0", "x", "x_a_b0");
        g.addEdge("c", "d", "x", "x_c_d");
        g.addEdge("a", "b1", "x", "x_a_b1");
        g.addEdge("c", "e", "x", "x_c_e");
        g.addEdge("a", "b2", "y", "y_a_b2");

        assertEquals(
                CollectionUtil.hashSetOf("x_a_b0", "x_c_d", "x_a_b1", "x_c_e"),
                g.getEdges("x")
        );
        assertEquals(
                CollectionUtil.hashSetOf("y_a_b2"),
                g.getEdges("y")
        );
    }

    @Test
    void testRemoveAllEdges() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        g.addEdge("a", "b0", "x", "x_a_b0");
        g.addEdge("c", "d", "x", "x_c_d");
        g.addEdge("a", "b1", "x", "x_a_b1");
        g.addEdge("c", "e", "x", "x_c_e");
        g.addEdge("a", "b2", "y", "y_a_b2");

        assertEquals(5, g.edgeCount());
        assertFalse(g.getEdges("x").isEmpty());
        assertEquals(
                CollectionUtil.hashSetOf("x_a_b0", "x_c_d", "x_a_b1", "x_c_e"),
                g.removeAllEdges("x")
        );
        assertTrue(g.getEdges("x").isEmpty());

        assertEquals(1, g.edgeCount());
        assertFalse(g.getEdges("y").isEmpty());
        assertEquals(
                CollectionUtil.hashSetOf("y_a_b2"),
                g.removeAllEdges("y")
        );
        assertTrue(g.getEdges("y").isEmpty());
        assertEquals(0, g.edgeCount());
    }

    @Test
    void testStreamVertexes() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        g.addEdge("a", "b0", "x", "x_a_b0");
        g.addEdge("c", "d", "x", "x_c_d");
        g.addEdge("a", "b1", "x", "x_a_b1");
        g.addEdge("c", "e", "x", "x_c_e");
        g.addEdge("a", "b2", "y", "y_a_b2");
        g.addEdge("c", "b1", "x", "x_c_b1");

        Set<String> set = g.streamVertices()
                .filter(v -> g.hasEdge(v, "b1", "x"))
                .collect(Collectors.toSet());

        assertEquals(
                CollectionUtil.hashSetOf("a", "c"),
                set
        );
    }

    @Test
    void testStreamNeighbours() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        g.addEdge("a", "b0", "x", "x_a_b0");
        g.addEdge("c", "d", "x", "x_c_d");
        g.addEdge("a", "b1", "x", "x_a_b1");
        g.addEdge("c", "e", "x", "x_c_e");
        g.addEdge("a", "b2", "y", "y_a_b2");
        g.addEdge("c", "b1", "x", "x_c_b1");

        Set<String> set = g.streamTargets("a", "x")
                .filter(t -> g.hasEdge("a", t, "x"))
                .collect(Collectors.toSet());

        assertEquals(
                CollectionUtil.hashSetOf("b0", "b1"),
                set
        );
    }

    @Test
    void testDegrees() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        assertTrue(g.addEdge("a", "x", "1", "a_x_1"));
        assertTrue(g.addEdge("b", "x", "1", "b_x_1"));
        assertTrue(g.addEdge("c", "x", "1", "c_x_1"));
        assertTrue(g.addEdge("d", "x", "1", "d_x_1"));
        assertTrue(g.addEdge("e", "x", "1", "e_x_1"));

        assertTrue(g.addEdge("a", "x", "2", "a_x_2"));
        assertTrue(g.addEdge("b", "x", "2", "b_x_2"));
        assertTrue(g.addEdge("c", "x", "2", "c_x_2"));

        assertTrue(g.addEdge("x", "a", "1", "x_a_1"));
        assertTrue(g.addEdge("x", "b", "1", "x_b_1"));
        assertTrue(g.addEdge("x", "c", "1", "x_c_1"));

        assertTrue(g.addEdge("x", "a", "2", "x_a_2"));
        assertTrue(g.addEdge("x", "b", "2", "x_b_2"));

        assertEquals(5, g.inDegree("x", "1"));
        assertEquals(3, g.inDegree("x", "2"));
        assertEquals(3, g.outDegree("x", "1"));
        assertEquals(2, g.outDegree("x", "2"));

        assertEquals(2, g.degree("a", "1"));
        assertEquals(1, g.degree("d", "1"));
    }

    @Test
    void testInOutStream() {
        DirectedAdjacencyListMultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        g.addEdge("a", "x", "1", "a_x_1");
        g.addEdge("b", "x", "1", "b_x_1");
        g.addEdge("c", "x", "1", "c_x_1");
        g.addEdge("d", "x", "1", "d_x_1");
        g.addEdge("e", "x", "1", "e_x_1");

        g.addEdge("a", "x", "2", "a_x_1");
        g.addEdge("b", "x", "2", "b_x_1");
        g.addEdge("c", "x", "2", "c_x_1");

        g.addEdge("x", "a", "1", "x_a_1");
        g.addEdge("x", "b", "1", "x_b_1");
        g.addEdge("x", "c", "1", "x_c_1");

        g.addEdge("x", "a", "2", "x_b_1");
        g.addEdge("x", "b", "2", "x_b_2");

        assertEquals(
                CollectionUtil.hashSetOf("a_x_1", "b_x_1", "c_x_1", "d_x_1", "e_x_1"),
                g.streamEdgesTo("x", "1").collect(Collectors.toSet())
        );

        assertEquals(
                CollectionUtil.hashSetOf("x_a_1", "x_b_1", "x_c_1"),
                g.streamEdgesFrom("x", "1").collect(Collectors.toSet())
        );
    }
}