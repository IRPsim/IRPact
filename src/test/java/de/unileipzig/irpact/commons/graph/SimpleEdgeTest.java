package de.unileipzig.irpact.commons.graph;

import de.unileipzig.irpact.commons.graph.Edge;
import de.unileipzig.irpact.commons.graph.Node;
import de.unileipzig.irpact.commons.graph.SimpleEdge;
import de.unileipzig.irpact.commons.graph.SimpleNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class SimpleEdgeTest {

    @Test
    void testGet() {
        Node source = new SimpleNode("a");
        Node target = new SimpleNode("b");
        Edge<Node> edge = new SimpleEdge<>(
                source,
                target,
                "c"
        );
        assertSame(source, edge.getSource());
        assertSame(target, edge.getTarget());
        assertEquals("c", edge.getLabel());
    }

}