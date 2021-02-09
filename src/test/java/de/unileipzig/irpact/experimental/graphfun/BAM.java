package de.unileipzig.irpact.experimental.graphfun;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.start.IRPact;
import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.commons.graph.DirectedAdjacencyListMultiGraph;
import de.unileipzig.irpact.commons.graph.FastDirectedMultiGraph;
import de.unileipzig.irpact.commons.graph.topology.BarabasiAlbertModel;
import de.unileipzig.irpact.commons.graph.topology.FastBarabasiAlbertModel;
import de.unileipzig.irpact.commons.graph.topology.HeterogeneousSmallWorldTopology;
import de.unileipzig.irpact.commons.graph.topology.WattsStrogatzModel;
import de.unileipzig.irptools.util.DataCounter;
import de.unileipzig.irptools.util.Pair;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.Comparator;
import java.util.Random;

/**
 * @author Daniel Abitz
 */
@Disabled
class BAM {

    private static final Logger logger = IRPLogging.getLogger(BAM.class);

    private static class Node {
        private final String label;

        private Node(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public String toString() {
            return "Node{" + label + "}";
        }
    }

    private static class Link {
        private final Node source;
        private final Node target;

        private Link(Node source, Node target) {
            this.source = source;
            this.target = target;
        }

        private static Link create(Node source, Node target, String type) {
            return new Link(source, target);
        }

        public Node getSource() {
            return source;
        }

        public Node getTarget() {
            return target;
        }
    }

    @Test
    void testBig() {
        DirectedAdjacencyListMultiGraph<Node, Link, String> g = new DirectedAdjacencyListMultiGraph<>();
        for(int i = 0; i < 500; i++) {
            g.addVertex(new Node("Node#" + i));
        }

        //IRPact.disableUtilLogging();
        BarabasiAlbertModel<Node, Link, String> bam = new BarabasiAlbertModel<>();
        bam.setM(10);
        bam.setM0(10);
        bam.setEdgeType("x");
        bam.setRandom(new Random(123));
        bam.setMultiEdgeCreatorFunction(Link::create);
        bam.initalizeEdges(g);
        //IRPact.enableUtilLogging();

        System.out.println("nodes: " + g.vertexCount());
        System.out.println("edges: " + g.edgeCount());

        DataCounter<Integer> dc2 = new DataCounter<>();
        for(Node n: g.getVertices()) {
            int d = g.degree(n, "x");
            dc2.inc(d, 1.0);
        }

        System.out.println("===");
        dc2.getMap().entrySet()
                .stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingDouble(Pair::getValue))
                .forEach(p -> System.out.println(p.getKey() + ": " + p.getValue()));
    }
    /*
110: 1.0
115: 1.0
109: 1.0
136: 1.0
127: 1.0
94: 1.0
137: 1.0
116: 1.0
81: 1.0
77: 1.0
86: 1.0
65: 1.0
92: 1.0
51: 1.0
79: 1.0
43: 1.0
67: 1.0
59: 1.0
48: 1.0
72: 1.0
42: 1.0
33: 1.0
39: 1.0
37: 1.0
118: 2.0
54: 2.0
36: 2.0
45: 2.0
60: 2.0
41: 2.0
27: 3.0
47: 4.0
63: 4.0
40: 4.0
35: 4.0
31: 4.0
26: 4.0
29: 4.0
30: 5.0
28: 6.0
23: 6.0
22: 8.0
24: 8.0
20: 8.0
19: 9.0
17: 11.0
21: 13.0
25: 14.0
16: 19.0
18: 22.0
15: 33.0
14: 34.0
13: 40.0
12: 51.0
11: 66.0
10: 80.0
     */

    @Test
    void testBigFast() {
        //DirectedAdjacencyListMultiGraph<Node, Link, String> g = new DirectedAdjacencyListMultiGraph<>();
        FastDirectedMultiGraph<Node, Link, String> g = new FastDirectedMultiGraph<>();
        for(int i = 0; i < 5000; i++) {
            g.addVertex(new Node("Node#" + i));
        }

        //IRPact.disableUtilLogging();
        FastBarabasiAlbertModel<Node, Link, String> bam = new FastBarabasiAlbertModel<>();
        bam.setM(2);
        bam.setM0(10);
        bam.setEdgeType("x");
        bam.setRandom(new Random(123));
        bam.setMultiEdgeCreatorFunction(Link::create);
        bam.initalizeEdges(g);
        //IRPact.enableUtilLogging();

        System.out.println("nodes: " + g.vertexCount());
        System.out.println("edges: " + g.edgeCount());

//        DataCounter<Integer> dc2 = new DataCounter<>();
//        for(Node n: g.getVertices()) {
//            int d = g.degree(n, "x");
//            dc2.inc(d, 1.0);
//        }
//
//        System.out.println("===");
//        dc2.getMap().entrySet()
//                .stream()
//                .map(e -> new Pair<>(e.getKey(), e.getValue()))
//                .sorted(Comparator.comparingDouble(Pair::getValue))
//                .forEach(p -> System.out.println(p.getKey() + ": " + p.getValue()));
    }

    @Test
    void testBigFast2() {
        //DirectedAdjacencyListMultiGraph<Node, Link, String> g = new DirectedAdjacencyListMultiGraph<>();
        FastDirectedMultiGraph<Node, Link, String> g = new FastDirectedMultiGraph<>();
        for(int i = 0; i < 50000; i++) {
            g.addVertex(new Node("Node#" + i));
        }

        //IRPact.disableUtilLogging();
        WattsStrogatzModel<Node, Link, String> wsm = new WattsStrogatzModel<>();
        wsm.setK(10);
        wsm.setBeta(0.1);
        wsm.setEdgeType("x");
        wsm.setRandom(new Random(123));
        wsm.setMultiEdgeCreatorFunction(Link::create);
        wsm.initalizeEdges(g);
        //IRPact.enableUtilLogging();

        System.out.println("nodes: " + g.vertexCount());
        System.out.println("edges: " + g.edgeCount());

//        DataCounter<Integer> dc2 = new DataCounter<>();
//        for(Node n: g.getVertices()) {
//            int d = g.degree(n, "x");
//            dc2.inc(d, 1.0);
//        }
//
//        System.out.println("===");
//        dc2.getMap().entrySet()
//                .stream()
//                .map(e -> new Pair<>(e.getKey(), e.getValue()))
//                .sorted(Comparator.comparingDouble(Pair::getValue))
//                .forEach(p -> System.out.println(p.getKey() + ": " + p.getValue()));
    }

    @Test
    void testBigFast3() {
        //DirectedAdjacencyListMultiGraph<Node, Link, String> g = new DirectedAdjacencyListMultiGraph<>();
        FastDirectedMultiGraph<Node, Link, String> g = new FastDirectedMultiGraph<>();
        for(int i = 0; i < 50000; i++) {
            g.addVertex(new Node("Node#" + i));
        }

        //IRPact.disableUtilLogging();
        HeterogeneousSmallWorldTopology<Node, Link, String, String> wsm = new HeterogeneousSmallWorldTopology<>();
        wsm.setGrpZMapping(CollectionUtil.hashMapOf("xxx", 10));
        wsm.setGrpBetaMapping(CollectionUtil.hashMapOf("xxx", 0.1));
        wsm.setSourceFunction(Link::getSource);
        wsm.setTargetFunction(Link::getTarget);
        wsm.setGetGroupFunction(n -> "xxx");
        wsm.setEdgeType("x");
        wsm.setRandom(new Random(123));
        wsm.setMultiEdgeCreatorFunction(Link::create);
        wsm.setDrawWeightedGroupFunction((grp, r) -> "xxx");
        wsm.initalizeEdges(g);
        //IRPact.enableUtilLogging();

        System.out.println("nodes: " + g.vertexCount());
        System.out.println("edges: " + g.edgeCount());

//        DataCounter<Integer> dc2 = new DataCounter<>();
//        for(Node n: g.getVertices()) {
//            int d = g.degree(n, "x");
//            dc2.inc(d, 1.0);
//        }
//
//        System.out.println("===");
//        dc2.getMap().entrySet()
//                .stream()
//                .map(e -> new Pair<>(e.getKey(), e.getValue()))
//                .sorted(Comparator.comparingDouble(Pair::getValue))
//                .forEach(p -> System.out.println(p.getKey() + ": " + p.getValue()));
    }
}
