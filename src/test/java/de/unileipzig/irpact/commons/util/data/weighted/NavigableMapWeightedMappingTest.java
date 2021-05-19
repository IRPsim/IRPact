package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.DataCounter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class NavigableMapWeightedMappingTest {

    @Test
    void testDraw() {
        NavigableMapWeightedMapping<String> wm = new NavigableMapWeightedMapping<>();
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
        NavigableMapWeightedMapping<String> wm = new NavigableMapWeightedMapping<>();
        wm.set("a", 0.8);
        wm.set("b", 0.15);
        wm.set("c", 0.05);
        DataCounter<String> counter = new DataCounter<>();
        Rnd rnd = new Rnd(123);
        for(int i = 0; i < 1000; i++) {
            String drawn = wm.getWeightedRandom(rnd);
            counter.inc(drawn);
        }
        assertEquals(800, counter.get("a"), 30);
        assertEquals(150, counter.get("b"), 30);
        assertEquals(50, counter.get("c"), 30);
        assertEquals(1000, counter.total());
    }

    @Test
    void testDisableWeight() {
        NavigableMapWeightedMapping<String> wm = new NavigableMapWeightedMapping<>();
        wm.set("a", 0.8);
        wm.set("b", 0.15);
        wm.set("c", 0.05);

        assertFalse(wm.isDisableWeights());
        wm.setDisableWeights(true);
        assertTrue(wm.isDisableWeights());

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
    void testZeroWeight() {
        NavigableMapWeightedMapping<String> wm = new NavigableMapWeightedMapping<>();
        assertFalse(wm.allowsZeroWeight());
        assertThrows(IllegalArgumentException.class, () -> wm.set("x", 0));
    }

    @Test
    void testRemoveCopy() {
        NavigableMapWeightedMapping<String> abc = new NavigableMapWeightedMapping<>();
        abc.set("a", 0.8);
        abc.set("b", 0.15);
        abc.set("c", 0.05);
        assertEquals(3, abc.size());

        DataCounter<String> abcCounter = new DataCounter<>();
        Rnd abcRnd = new Rnd(123);
        for(int i = 0; i < 1000; i++) {
            String drawn = abc.getWeightedRandom(abcRnd);
            abcCounter.inc(drawn);
        }

        assertEquals(800, abcCounter.get("a"), 30);
        assertEquals(150, abcCounter.get("b"), 30);
        assertEquals(50, abcCounter.get("c"), 30);
        assertEquals(1000, abcCounter.total());

        //===

        NavigableMapWeightedMapping<String> ac = abc.copyWithout("b");
        assertEquals(2, ac.size());

        DataCounter<String> acCounter = new DataCounter<>();
        Rnd acRnd = new Rnd(123);
        for(int i = 0; i < 1000; i++) {
            String drawn = ac.getWeightedRandom(acRnd);
            acCounter.inc(drawn);
        }

        assertEquals(940, acCounter.get("a"), 20);
        assertEquals(60, acCounter.get("c"), 20);
        assertEquals(1000, acCounter.total());

        //===

        NavigableMapWeightedMapping<String> c = ac.copyWithout("a");
        assertEquals(1, c.size());

        DataCounter<String> cCounter = new DataCounter<>();
        Rnd cRnd = new Rnd(123);
        for(int i = 0; i < 1000; i++) {
            String drawn = c.getWeightedRandom(cRnd);
            cCounter.inc(drawn);
        }

        assertEquals(1000, cCounter.get("c"));
        assertEquals(1000, cCounter.total());

        //===

        NavigableMapWeightedMapping<String> x = c.copyWithout("c");
        assertEquals(0, x.size());
        assertTrue(x.isEmpty());
        assertThrows(IllegalStateException.class, () -> x.getWeightedRandom(new Rnd()));
    }

    @Test
    void testUnreachableValue() {
        NavigableMapWeightedMapping<String> abc = new NavigableMapWeightedMapping<>();
        abc.set("a", 1);
        assertEquals("a", abc.getWeightedRandom(0.9999));
        assertThrows(NullPointerException.class, () -> abc.getWeightedRandom(1.0));
    }
}