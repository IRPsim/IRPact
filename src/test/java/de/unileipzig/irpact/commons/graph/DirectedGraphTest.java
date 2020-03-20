package de.unileipzig.irpact.commons.graph;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

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
}