package de.unileipzig.irpact.experimental.todev.graph;

import de.unileipzig.irpact.v2.commons.CollectionUtil;
import de.unileipzig.irpact.experimental.annotation.ToDevelop;
import de.unileipzig.irpact.experimental.todev.Data;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
@ToDevelop
class DirectedAdjacencyListGraphTest {

    @Test
    void testAddVertex() {
        DirectedAdjacencyListGraph<String, Double> g = new DirectedAdjacencyListGraph<>();

        assertFalse(g.hasVertex("a"));
        g.addVertex("a");
        assertTrue(g.hasVertex("a"));

        assertFalse(g.hasVertex("b"));
        g.addVertex("b");
        assertTrue(g.hasVertex("b"));
    }

    @Test
    void testAddEdge() {
        DirectedAdjacencyListGraph<String, Double> g = new DirectedAdjacencyListGraph<>();

        assertFalse(g.hasEdge("a", "b"));
        assertTrue(g.addEdge("a", "b", 3.0));
        assertFalse(g.addEdge("a", "b", 3.0));
        assertTrue(g.hasEdge("a", "b"));
        assertFalse(g.hasEdge("b", "a"));
    }

    @Test
    void testRemoveEdge() {
        DirectedAdjacencyListGraph<String, Double> g = new DirectedAdjacencyListGraph<>();

        assertFalse(g.hasEdge("a", "b"));
        assertTrue(g.addEdge("a", "b", 3.0));
        assertTrue(g.hasEdge("a", "b"));

        assertEquals(3.0, g.removeEdge("a", "b"));
        assertFalse(g.hasEdge("a", "b"));
    }

    @Test
    void testHasVertexIfAddEdge() {
        DirectedAdjacencyListGraph<String, Double> g = new DirectedAdjacencyListGraph<>();

        assertFalse(g.hasVertex("a"));
        assertFalse(g.hasVertex("b"));
        assertTrue(g.addEdge("a", "b", 3.0));
        assertTrue(g.hasVertex("a"));
        assertTrue(g.hasVertex("b"));
    }

    @Test
    void testSetEdge() {
        DirectedAdjacencyListGraph<String, Double> g = new DirectedAdjacencyListGraph<>();

        assertTrue(g.addEdge("a", "b", 3.0));
        assertEquals(3.0, g.setEdge("a", "b", 5.0));
        assertEquals(5.0, g.getWeight("a", "b"));
    }

    @Test
    void testIterateNeighboursDirected() {
        DirectedAdjacencyListGraph<String, Double> g = new DirectedAdjacencyListGraph<>();
        g.addEdge("a", "b", 1.0);
        g.addEdge("c", "a", 1.0);
        g.addEdge("d", "a", 1.0);
        g.addEdge("a", "e", 1.0);

        assertEquals(
                CollectionUtil.hashSetOf("b", "e"),
                CollectionUtil.toSet(g.iterateNeighbours("a", true, false))
        );
    }

    @Test
    void testIterateNeighboursUndirected() {
        DirectedAdjacencyListGraph<String, Double> g = new DirectedAdjacencyListGraph<>();
        g.addEdge("a", "b", 1.0);
        g.addEdge("c", "a", 1.0);
        g.addEdge("d", "a", 1.0);
        g.addEdge("a", "e", 1.0);

        assertEquals(
                CollectionUtil.hashSetOf("b", "c", "d", "e"),
                CollectionUtil.toSet(g.iterateNeighbours("a", false, false))
        );
    }

    @Test
    void testIteratorThrowsErrorOnNextIfChange() {
        DirectedAdjacencyListGraph<String, Double> g = new DirectedAdjacencyListGraph<>();
        g.addEdge("a", "b", 1.0);
        g.addEdge("c", "a", 1.0);
        g.addEdge("d", "a", 1.0);
        g.addEdge("a", "e", 1.0);

        Iterator<String> nIter = g.iterateNeighbours("a", true, false);
        assertDoesNotThrow(nIter::next);
        g.addVertex("x");
        assertThrows(ConcurrentModificationException.class, nIter::next);
    }

    @Test
    void testCopy() {
        DirectedAdjacencyListGraph<String, Double> g = new DirectedAdjacencyListGraph<>();
        g.addVertex("a");
        g.addVertex("b");

        DirectedAdjacencyListGraph<String, Double> c = g.copy();

        g.removeVertex("a");
        assertFalse(g.hasVertex("a"));
        assertTrue(c.hasVertex("a"));
    }

    @Test
    void testRemoveVertex() {
        DirectedAdjacencyListGraph<String, Double> g = new DirectedAdjacencyListGraph<>();
        g.addEdge("a", "x", 1.0);
        g.addEdge("b", "x", 1.0);
        g.addEdge("x", "z", 1.0);
        assertFalse(g.removeVertex("_"));
        assertEquals(4, g.vertexCount());
        assertTrue(g.removeVertex("x"));
        assertFalse(g.hasEdge("a", "x"));
        assertFalse(g.hasEdge("b", "x"));
        assertFalse(g.hasEdge("x", "z"));
        assertEquals(3, g.vertexCount());
    }

    @Test
    void testEdgeCount() {
        DirectedAdjacencyListGraph<String, Double> g = new DirectedAdjacencyListGraph<>();
        g.addEdge("a", "x", 1.0);
        g.addEdge("b", "x", 1.0);
        g.addEdge("x", "z", 1.0);
        assertEquals(4, g.vertexCount());
        assertEquals(3, g.edgeCount());
        assertTrue(g.removeVertex("x"));
        assertEquals(3, g.vertexCount());
        assertEquals(0, g.edgeCount());
    }

    @Test
    void testHasPath() {
        DirectedAdjacencyListGraph<String, Double> g = new DirectedAdjacencyListGraph<>();
        g.addEdge("a", "b", 1.0);
        g.addEdge("b", "c", 1.0);
        g.addEdge("c", "d", 1.0);

        assertTrue(g.hasPath("a", "b", "c", "d"));
        assertFalse(g.hasPath("a", "b", "c", "e"));
        g.removeVertex("d");
        assertFalse(g.hasPath("a", "b", "c", "d"));
    }

    @Test
    void testDistinct() {
        DirectedAdjacencyListGraph<String, Double> g = new DirectedAdjacencyListGraph<>();
        g.addEdge("a", "b", 1.0);
        g.addEdge("b", "a", 1.0);
        assertEquals(
                CollectionUtil.arrayListOf("b", "b"),
                CollectionUtil.toList(g.iterateNeighbours("a", false, false))
        );
        assertEquals(
                CollectionUtil.arrayListOf("b"),
                CollectionUtil.toList(g.iterateNeighbours("a", false, true))
        );
    }

    @Test
    void testStream() {
        DirectedAdjacencyListGraph<String, Double> g = new DirectedAdjacencyListGraph<>();
        g.addEdge("a", "b", 1.0);
        g.addEdge("a", "c", 2.0);
        g.addEdge("a", "d", 3.0);
        g.addEdge("a", "e", 4.0);

        Set<String> nSet = g.streamNeighbours("a", true, true)
                .filter(v -> g.getWeight("a", v) < 3.0)
                .collect(Collectors.toSet());
        assertEquals(CollectionUtil.hashSetOf("b", "c"), nSet);
    }

    @Test
    void testChangeWeight() {
        DirectedAdjacencyListGraph<String, Double> g = new DirectedAdjacencyListGraph<>();

        assertThrows(NoSuchElementException.class, () -> g.changeWeight("a", "b", 3.0));
        g.addEdge("a", "b", 2.0);
        assertEquals(2.0, g.getWeight("a", "b"));
        assertDoesNotThrow(() -> g.changeWeight("a", "b", 3.0));
        assertEquals(3.0, g.getWeight("a", "b"));
    }

    @Test
    void testForEachNode() {
        DirectedAdjacencyListGraph<String, Double> g = new DirectedAdjacencyListGraph<>();
        g.addEdge("a", "b", 1.0);
        g.addEdge("a", "c", 2.0);
        g.addEdge("a", "d", 3.0);
        g.addEdge("a", "e", 4.0);

        Data<Double> sum = Data.of(0.0);
        g.forEachEdge((f, t, w) -> sum.set(sum.get() + w));
        assertEquals(10.0, sum.get());
    }
}