package de.unileipzig.irpact.experimental;

import de.unileipzig.irpact.experimental.eval3.DynExponential;
import de.unileipzig.irpact.experimental.eval3.DynLinear;
import de.unileipzig.irpact.commons.graph.DirectedAdjacencyListMultiGraph;
import de.unileipzig.irpact.commons.graph.MultiGraph;
import de.unileipzig.irpact.commons.graph.topology.FreeMultiGraphTopology;
import de.unileipzig.irpact.commons.graph.topology.WattsStrogatzModel;
import de.unileipzig.irpact.commons.log.Logback;
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

    @Test
    void testIt() {
        DynLinear b1 = new DynLinear();
        b1.setXMax(20);
        b1.setM(-1);
        b1.init(2.0, 0.7);
        b1.setX1(5.0);
        double b1_y1 = b1.calculateY1();

        DynExponential b2 = new DynExponential();
        b2.setXMax(20);
        b2.init(5.0, b1_y1);
        b2.setLambda(-4);
        b2.setX1(20.0);

        System.out.println("b1_y1 " + b1_y1);
        System.out.println(b1.calculate(2));
        System.out.println(b2.calculate(5));
    }

    @Test
    void testIt2() {
        DynExponential b1 = new DynExponential();
        b1.setXMax(20);
        b1.init(2.0, 0.7);
        b1.setLambda(-4);
        b1.setX1(5);
        double b1_y1 = b1.calculateY1();

        DynLinear b2 = new DynLinear();
        b2.setXMax(20);
        b2.setM(-1);
        b2.init(5.0, b1_y1);
        b2.setX1(20);

        System.out.println("b1_y1 " + b1_y1);
        System.out.println(b1.calculate(2));
        System.out.println(b2.calculate(5));
    }
}