package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.DataCounter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class BasicWeightedMappingTest {

    @Test
    void testNormalize() {
        BasicWeightedMapping<String> wm = new BasicWeightedMapping<>();
        wm.set("a", 1.6);
        wm.set("b", 0.3);
        wm.set("c", 0.1);
        assertFalse(wm.isNormalized());
        wm.normalize();
        assertTrue(wm.isNormalized());
        assertEquals(0.8, wm.getWeight("a"));
        assertEquals(0.15, wm.getWeight("b"));
        assertEquals(0.05, wm.getWeight("c"));
    }

    @Test
    void testDraw() {
        BasicWeightedMapping<String> wm = new BasicWeightedMapping<>();
        wm.set("a", 0.8);
        wm.set("b", 0.15);
        wm.set("c", 0.05);
        DataCounter<String> counter = new DataCounter<>();
        Rnd rnd = new Rnd(123);
        for(int i = 0; i < 1000; i++) {
            String drawn = wm.getRandom(rnd);
            counter.inc(drawn);
        }
        assertEquals(333, counter.get("a"), 20);
        assertEquals(333, counter.get("b"), 20);
        assertEquals(333, counter.get("c"), 20);
        assertEquals(1000, counter.total());
    }

    @Test
    void testDrawWeighted() {
        BasicWeightedMapping<String> wm = new BasicWeightedMapping<>();
        wm.set("a", 0.8);
        wm.set("b", 0.15);
        wm.set("c", 0.05);
        DataCounter<String> counter = new DataCounter<>();
        Rnd rnd = new Rnd(123);
        for(int i = 0; i < 1000; i++) {
            String drawn = wm.getWeightedRandom(rnd);
            counter.inc(drawn);
        }
        assertEquals(800, counter.get("a"), 20);
        assertEquals(150, counter.get("b"), 20);
        assertEquals(50, counter.get("c"), 20);
        assertEquals(1000, counter.total());
    }
}