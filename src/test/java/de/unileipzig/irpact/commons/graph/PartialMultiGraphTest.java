package de.unileipzig.irpact.commons.graph;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class PartialMultiGraphTest {

    @Test
    void testPartial() {
        DirectedMultiGraph<SimpleNode, SimpleEdge<SimpleNode>, String> master = new DirectedMultiGraph<>(new HashMap<>());
        Graph<SimpleNode, SimpleEdge<SimpleNode>> slave1 = new PartialMultiGraph<>("x", master);
        Graph<SimpleNode, SimpleEdge<SimpleNode>> slave2 = new PartialMultiGraph<>("y", master);

        SimpleNode n0 = new SimpleNode("n0");
        SimpleNode n1 = new SimpleNode("n1");
        SimpleNode n2 = new SimpleNode("n2");
        SimpleEdge<SimpleNode> n0n1 = new SimpleEdge<>(n0, n1, "n0n1");
        SimpleEdge<SimpleNode> n2n0 = new SimpleEdge<>(n2, n0, "n2n0");

        slave1.addEdge(n0n1);
        slave2.addEdge(n2n0);

        assertTrue(master.hasEdge(n0n1, "x"));
        assertTrue(master.hasEdge(n2n0, "y"));
        assertFalse(master.hasEdge(n0n1, "y"));
        assertFalse(master.hasEdge(n2n0, "x"));
    }
}