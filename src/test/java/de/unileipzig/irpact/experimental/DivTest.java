package de.unileipzig.irpact.experimental;

import de.unileipzig.irpact.commons.distribution.ConstantDistribution;
import de.unileipzig.irpact.start.IRPact;
import de.unileipzig.irpact.v2.commons.graph.DirectedAdjacencyListMultiGraph;
import de.unileipzig.irpact.v2.commons.graph.MultiGraph;
import de.unileipzig.irpact.v2.commons.graph.topology.FreeMultiGraphTopology;
import de.unileipzig.irpact.v2.commons.graph.topology.WattsStrogatzModel;
import de.unileipzig.irpact.v2.commons.log.Logback;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Daniel Abitz
 */
@Disabled
class DivTest {

    @Test
    void testStuff() {
        fail("disabled?");
    }

    @Test
    void name() {
        System.out.println(ConstantDistribution.NAME);
    }

    private int calc(int i, int j, int n, int k) {
        return Math.abs(i - j) % (n- 1 - k / 2);
    }

    @Test
    void asd() {
        int n = 11;
        int k = 4;

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                int x = calc(i, j, n, k);
                boolean isOk = 0 < x && x <= k/2;
                System.out.println(isOk + " " + i + " " + j);
            }
        }
    }

    @Test
    void asd2() {
        Logback.setupSystemOutAndErr();
        MultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        for(int i = 0; i < 11; i++) {
            g.addVertex("a" + i);
        }
        WattsStrogatzModel<String, String, String> wsm = new WattsStrogatzModel<>();
        wsm.setMultiEdgeCreatorFunction((from, to, type) -> from + "|" + to + "|" + type);
        wsm.setEdgeType("x");
        wsm.setK(4);
        wsm.setSelfReferential(false);
        wsm.setRandom(new Random(123));
        wsm.setBeta(0.3);

        wsm.initalizeEdges(g);
    }

    @Test
    void asd3() {
        Logback.setupSystemOutAndErr();
        MultiGraph<String, String, String> g = new DirectedAdjacencyListMultiGraph<>();
        for(int i = 0; i < 11; i++) {
            g.addVertex("a" + i);
        }
        FreeMultiGraphTopology<String, String, String> top = new FreeMultiGraphTopology<>();
        top.setMultiEdgeCreatorFunction((from, to, type) -> from + "|" + to + "|" + type);
        top.setEdgeCount(3);
        top.setEdgeType("x");
        top.setSelfReferential(false);
        top.setRandom(new Random(123));

        top.initalizeEdges(g);

        System.out.println("edges: " + g.getEdges("x").size());
    }
}