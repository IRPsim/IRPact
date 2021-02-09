package de.unileipzig.irpact.experimental;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.Util;
import de.unileipzig.irpact.commons.util.IRPactBase32;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.experimental.eval3.DynExponential;
import de.unileipzig.irpact.experimental.eval3.DynLinear;
import de.unileipzig.irpact.commons.graph.DirectedAdjacencyListMultiGraph;
import de.unileipzig.irpact.commons.graph.MultiGraph;
import de.unileipzig.irpact.commons.graph.topology.FreeMultiGraphTopology;
import de.unileipzig.irpact.commons.graph.topology.WattsStrogatzModel;
import de.unileipzig.irpact.commons.log.Logback;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;
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
        System.out.println(b1);

        DynExponential b2 = new DynExponential();
        b2.setXMax(20);
        b2.init(5.0, b1_y1);
        b2.setLambda(-4);
        b2.setX1(20.0);
        System.out.println(b2);

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

    @Test
    void testBase32Stuff() {
        System.out.println(IRPactBase32.utf8ToBase32("X"));
        System.out.println(IRPactBase32.base32ToUtf8("B0ZZZZZZ"));
    }

    @Test
    void funWithSmile() throws IOException {
        ObjectNode root = IRPactJson.SMILE.createObjectNode();
        root.put("a", 1);
        root.put("b", "x");
        root.put("c", false);
        byte[] b = IRPactJson.toBytes(IRPactJson.SMILE, root);
        String b32 = IRPactBase32.encodeToString(b);
        System.out.println(b32);
        byte[] bb = IRPactBase32.decodeString(b32);
        ObjectNode root2 = (ObjectNode) IRPactJson.fromBytes(IRPactJson.SMILE, bb);
        System.out.println(Objects.equals(root, root2));
        System.out.println(root2);
    }
}