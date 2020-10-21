package de.unileipzig.irpact.v2.commons.graph;

import org.junit.jupiter.api.Test;

import java.util.Map;

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
}