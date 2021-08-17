package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.DataCounter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class RemoveOnDrawNavigableMapWeightedMappingTest {

    @Test
    void testMapConstruction() {
        RemoveOnDrawNavigableMapWeightedMapping<String> mapping = new RemoveOnDrawNavigableMapWeightedMapping<>();
        mapping.set("a", 80);
        mapping.set("b", 15);
        mapping.set("c", 5);

        assertEquals(
                CollectionUtil.hashMapOf(80.0, "a", 95.0, "b", 100.0, "c"),
                mapping.mapping
        );
    }

    @Test
    void testMapAfterRemove() {
        RemoveOnDrawNavigableMapWeightedMapping<String> mapping = new RemoveOnDrawNavigableMapWeightedMapping<>();
        mapping.set("a", 80);
        mapping.set("b", 50);
        mapping.set("c", 30);
        mapping.set("d", 10);
        mapping.set("e", 5);

        assertTrue(mapping.remove("c"));

        assertEquals(
                CollectionUtil.hashMapOf(80.0, "a", 130.0, "b", 140.0, "d", 145.0, "e"),
                mapping.mapping
        );
    }

    @Test
    void testNormalizeThis() {
        RemoveOnDrawNavigableMapWeightedMapping<String> mapping = new RemoveOnDrawNavigableMapWeightedMapping<>();
        mapping.set("a", 80);
        mapping.set("b", 15);
        mapping.set("c", 5);

        assertEquals(100, mapping.totalWeight());
        mapping.normalizeThis();
        assertEquals(1, mapping.totalWeight());
    }

    @Test
    void testWeightedDraw() {
        DataCounter<String> pos0 = new DataCounter<>();
        DataCounter<String> pos1 = new DataCounter<>();
        DataCounter<String> pos2 = new DataCounter<>();

        Rnd master = new Rnd(123);
        for(int i = 0; i < 100; i++) {
            Rnd rnd = master.deriveInstance();
            RemoveOnDrawNavigableMapWeightedMapping<String> mapping = new RemoveOnDrawNavigableMapWeightedMapping<>();
            mapping.set("a", 0.60);
            mapping.set("b", 0.30);
            mapping.set("c", 0.10);

            pos0.inc(mapping.getWeightedRandom(rnd));
            pos1.inc(mapping.getWeightedRandom(rnd));
            pos2.inc(mapping.getWeightedRandom(rnd));

            assertTrue(mapping.isEmpty());
        }

        assertEquals(0.6, pos0.getShare("a"), 0.07);
        assertEquals(0.3, pos0.getShare("b"), 0.07);
        assertEquals(0.1, pos0.getShare("c"), 0.07);

        assertEquals(0.3, pos1.getShare("a"), 0.07);
        assertEquals(0.5, pos1.getShare("b"), 0.07);
        assertEquals(0.2, pos1.getShare("c"), 0.07);

        assertEquals(0.1, pos2.getShare("a"), 0.07);
        assertEquals(0.2, pos2.getShare("b"), 0.07);
        assertEquals(0.7, pos2.getShare("c"), 0.07);
    }

    @Test
    void testRandomDraw() {
        DataCounter<String> pos0 = new DataCounter<>();
        DataCounter<String> pos1 = new DataCounter<>();
        DataCounter<String> pos2 = new DataCounter<>();

        Rnd master = new Rnd(123);
        for(int i = 0; i < 100; i++) {
            Rnd rnd = master.deriveInstance();
            RemoveOnDrawNavigableMapWeightedMapping<String> mapping = new RemoveOnDrawNavigableMapWeightedMapping<>();
            mapping.set("a", 0.60);
            mapping.set("b", 0.30);
            mapping.set("c", 0.10);

            pos0.inc(mapping.getRandom(rnd));
            pos1.inc(mapping.getRandom(rnd));
            pos2.inc(mapping.getRandom(rnd));

            assertTrue(mapping.isEmpty());
        }

        assertEquals(0.33, pos0.getShare("a"), 0.07);
        assertEquals(0.33, pos0.getShare("b"), 0.07);
        assertEquals(0.33, pos0.getShare("c"), 0.07);

        assertEquals(0.33, pos1.getShare("a"), 0.07);
        assertEquals(0.33, pos1.getShare("b"), 0.07);
        assertEquals(0.33, pos1.getShare("c"), 0.07);

        assertEquals(0.33, pos2.getShare("a"), 0.07);
        assertEquals(0.33, pos2.getShare("b"), 0.07);
        assertEquals(0.33, pos2.getShare("c"), 0.07);
    }
}