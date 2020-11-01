package de.unileipzig.irpact.v2.commons.graph;

import de.unileipzig.irpact.v2.commons.CollectionUtil;
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
        assertTrue(g.addEdge("a", "b", "y", "1"));
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
        assertTrue(g.addEdge("c", "d", "x", "1"));
        assertEquals(2, g.edgeCount());
        assertTrue(g.removeEdge("1"));
        assertTrue(g.hasEdge("1"));
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
        assertTrue(g.addEdge("a", "b", "y", "1"));
        Map<String, String> edges = g.getEdges("a", "b");
        assertEquals(2, edges.size());
        assertEquals("1", edges.get("x"));
        assertEquals("1", edges.get("y"));
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

        Set<String> set = g.streamVertexes()
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

        Set<String> set = g.streamNeighbours("a")
                .filter(t -> g.hasEdge("a", t, "x"))
                .collect(Collectors.toSet());

        assertEquals(
                CollectionUtil.hashSetOf("b0", "b1"),
                set
        );
    }
}