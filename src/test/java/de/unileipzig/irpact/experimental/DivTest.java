package de.unileipzig.irpact.experimental;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.*;
import de.unileipzig.irpact.commons.res.BasicResourceLoader;
import de.unileipzig.irpact.commons.util.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.spatial.attribute.SpatialDoubleAttributeBase;
import de.unileipzig.irpact.core.spatial.attribute.SpatialStringAttributeBase;
import de.unileipzig.irpact.experimental.eval3.DynExponential;
import de.unileipzig.irpact.experimental.eval3.DynLinear;
import de.unileipzig.irpact.commons.graph.DirectedAdjacencyListMultiGraph;
import de.unileipzig.irpact.commons.graph.MultiGraph;
import de.unileipzig.irpact.commons.graph.topology.FreeMultiGraphTopology;
import de.unileipzig.irpact.commons.graph.topology.WattsStrogatzModel;
import de.unileipzig.irpact.commons.log.Logback;
import de.unileipzig.irpact.start.IRPact;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Daniel Abitz
 */
@Disabled
class DivTest {

    @Test
    void testMyHashStuff() {
        List<SpatialAttribute<?>> list0 = CollectionUtil.arrayListOf(
                new SpatialDoubleAttributeBase("a", 1),
                new SpatialStringAttributeBase("b", "x1")
        );
        List<SpatialAttribute<?>> list00 = CollectionUtil.arrayListOf(
                new SpatialDoubleAttributeBase("a", 1),
                new SpatialStringAttributeBase("b", "x1")
        );
        List<SpatialAttribute<?>> list1 = CollectionUtil.arrayListOf(
                new SpatialDoubleAttributeBase("c", 2),
                new SpatialStringAttributeBase("d", "x2")
        );
        Set<SpatialAttribute<?>> set0 = CollectionUtil.hashSetOf(
                new SpatialDoubleAttributeBase("e", 3),
                new SpatialStringAttributeBase("f", "x3")
        );
        Set<SpatialAttribute<?>> set00 = CollectionUtil.hashSetOf(
                new SpatialDoubleAttributeBase("e", 3),
                new SpatialStringAttributeBase("f", "x3")
        );
        Set<SpatialAttribute<?>> set1 = CollectionUtil.hashSetOf(
                new SpatialDoubleAttributeBase("g", 4),
                new SpatialStringAttributeBase("h", "x4")
        );

        List<Collection<SpatialAttribute<?>>> listColl0 = CollectionUtil.arrayListOf(
                list0,
                list1,
                set0,
                set1
        );
        List<Collection<SpatialAttribute<?>>> listColl1 = CollectionUtil.arrayListOf(
                list00,
                list1,
                set00,
                set1
        );

        Set<Collection<SpatialAttribute<?>>> setColl0 = CollectionUtil.hashSetOf(
                list0,
                list1,
                set0,
                set1
        );
        Set<Collection<SpatialAttribute<?>>> setColl1 = CollectionUtil.hashSetOf(
                list00,
                list1,
                set00,
                set1
        );

        System.out.println(ChecksumComparable.getCollChecksum(list0)
                + " "
                + ChecksumComparable.getCollChecksum(list00)
        );
        System.out.println(ChecksumComparable.getCollChecksum(list0)
                + " "
                + ChecksumComparable.getCollChecksum(list1)
        );
        System.out.println();
        System.out.println(ChecksumComparable.getCollChecksum(set0)
                + " "
                + ChecksumComparable.getCollChecksum(set00)
        );
        System.out.println(ChecksumComparable.getCollChecksum(set0)
                + " "
                + ChecksumComparable.getCollChecksum(set1)
        );
        System.out.println();
        System.out.println(ChecksumComparable.getCollCollChecksum(listColl0)
                + " "
                + ChecksumComparable.getCollCollChecksum(listColl1)
        );
        System.out.println(ChecksumComparable.getCollCollChecksum(setColl0)
                + " "
                + ChecksumComparable.getCollCollChecksum(setColl1)
        );
    }

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
        Logback.setupConsole();
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
        Logback.setupConsole();
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

    @Test
    void resTest() throws Exception {
        IRPLogging.initConsole();
        IRPact irpact = new IRPact(null, Collections.emptyList(), new BasicResourceLoader());
        irpact.start((ObjectNode) null);
    }

    @Test
    void lol() {
        System.out.println(IRPactBase32.decodeStringToUTF8("EDQ62SJK78G68QBI41HMGPB3DC568QBI78G2S2HU40N5OBJ7D5Q0KFH05PE2SPR9EHKMERJFE9IGKFH05PE2SPRIC5I6OP8A7OG2SN1ED5I6AO8A7OG2SN32ELKMOP0A7OG2SN32ELKMOP1ECTP62P3CCK53S81EBHHNASRKDTMMOQB2EC53S81EBHIM8TRFDTI2QS335PPMAT3KD5N6ESPEF1MMO2HU40N5OPRIC5I6OP8A7OG2SN37E9GM8R35ES53S81EBHJN4OB4DHINEBJ2C5Q0KFH05PE6IRJ6DTPISQJJDTN0KFH05PE6UTBK18V20BISE1M62T36DTP6QNR1ELQ6UBJ3DTN6C2HU40N5OSJ5C5I6QP9EDLI0KFH05PE76PBKEHKMSPRJ5PJN4OB4DHIGKFH05PE76PBKEHKMSPRJBTIM8TRFDTI2QS3318V20BISEDIN8T39DPJN6NRID5HMMSR8C5RJ42HU40N5OSRICC53S81EBHQ6ASRKCPKMOPBJ19IMSP3578G68QBI41HMGPB3DC50ZZZZ"));
    }

    @Test
    void testRndReducer() {
        List<String> list = CollectionUtil.arrayListOf("a", "b", "c", "d");
        System.out.println(list.stream().reduce(new RndElementReducer<>(0)));
        System.out.println(list.stream().reduce(new RndElementReducer<>(1)));
        System.out.println(list.stream().reduce(new RndElementReducer<>(2)));
        System.out.println(list.stream().reduce(new RndElementReducer<>(3)));
        System.out.println(list.stream().reduce(new RndElementReducer<>(4)));
    }

    @Test
    void testWeightedRndReducer() {
        List<String> list = CollectionUtil.arrayListOf("a", "b", "c", "d");
        System.out.println(list.stream().reduce(new WeightedRndElementReducer<>(4 * 0, x -> 1)));
        System.out.println(list.stream().reduce(new WeightedRndElementReducer<>(4 * 0.25, x -> 1)));
        System.out.println(list.stream().reduce(new WeightedRndElementReducer<>(4 * 0.5, x -> 1)));
        System.out.println(list.stream().reduce(new WeightedRndElementReducer<>(4 * 0.75, x -> 1)));
        System.out.println(list.stream().reduce(new WeightedRndElementReducer<>(4 * 1, x -> 1)));
    }

    @Test
    void logFun() {
        IRPLogging.initConsole();
        IRPLogging.getLogger(DivTest.class).trace("HALLO");
        IRPLogging.getClearLogger().trace("HALLO");
    }
}