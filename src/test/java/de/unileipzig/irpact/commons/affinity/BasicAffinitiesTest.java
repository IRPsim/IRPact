package de.unileipzig.irpact.commons.affinity;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.DataCounter;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class BasicAffinitiesTest {

    @Test
    void testSetGetValue() {
        BasicAffinities<String> aff = new BasicAffinities<>();
        assertFalse(aff.hasValue("a"));
        assertEquals(0, aff.getValue("a"));
        aff.setValue("a", 1);
        assertTrue(aff.hasValue("a"));
        assertEquals(1, aff.getValue("a"));
        aff.setValue("a", 2);
        assertEquals(2, aff.getValue("a"));
    }

    @Test
    void testSum() {
        BasicAffinities<String> aff = new BasicAffinities<>();
        aff.setValue("a", 1);
        aff.setValue("b", 2);
        aff.setValue("c", 3);
        assertEquals(6, aff.sum());
    }

    @Test
    void testWeightedDraw() {
        BasicAffinities<String> aff = new BasicAffinities<>();
        aff.setValue("b", 0.01);
        aff.setValue("c", 0.99);
        String v = aff.getWeightedRandom(new Rnd(123));
        assertEquals("c", v);
    }

    @Test
    void testWeightedDraw2() {
        BasicAffinities<String> aff = new BasicAffinities<>();
        aff.setValue("b", 0.5);
        aff.setValue("c", 0.5);
        Rnd rnd = new Rnd(123);
        Set<String> set = new HashSet<>();
        int i = 0;
        while(set.size() != 2) {
            String v = aff.getWeightedRandom(rnd);
            set.add(v);
            i++;
        }
        assertTrue(i > 1);
        assertTrue(i < 10);
        assertTrue(set.contains("b"));
        assertTrue(set.contains("c"));
    }

    @Test
    void testWeightedDraw_25_25_25_25() {
        BasicAffinities<String> aff = new BasicAffinities<>();
        aff.setValue("a", 0.25);
        aff.setValue("b", 0.25);
        aff.setValue("c", 0.25);
        aff.setValue("d", 0.25);
        Rnd rnd = new Rnd(123);
        DataCounter<String> counter = new DataCounter<>();

        for(int i = 0; i < 100; i++) {
            String s = aff.getWeightedRandom(rnd);
            counter.inc(s);
        }

        assertEquals(0.25, counter.getShare("a"), 0.06);
        assertEquals(0.25, counter.getShare("b"), 0.06);
        assertEquals(0.25, counter.getShare("c"), 0.06);
        assertEquals(0.25, counter.getShare("d"), 0.06);
    }

    @Test
    void testWeightedDraw_70_10_10_10() {
        BasicAffinities<String> aff = new BasicAffinities<>();
        aff.setValue("a", 0.70);
        aff.setValue("b", 0.10);
        aff.setValue("c", 0.10);
        aff.setValue("d", 0.10);
        Rnd rnd = new Rnd(123);
        DataCounter<String> counter = new DataCounter<>();

        for(int i = 0; i < 100; i++) {
            String s = aff.getWeightedRandom(rnd);
            counter.inc(s);
        }

        assertEquals(0.70, counter.getShare("a"), 0.06);
        assertEquals(0.10, counter.getShare("b"), 0.06);
        assertEquals(0.10, counter.getShare("c"), 0.06);
        assertEquals(0.10, counter.getShare("d"), 0.06);
    }

    @Test
    void testWeightedDraw_40_30_20_10() {
        BasicAffinities<String> aff = new BasicAffinities<>();
        aff.setValue("a", 0.40);
        aff.setValue("b", 0.30);
        aff.setValue("c", 0.20);
        aff.setValue("d", 0.10);
        Rnd rnd = new Rnd(123);
        DataCounter<String> counter = new DataCounter<>();

        for(int i = 0; i < 100; i++) {
            String s = aff.getWeightedRandom(rnd);
            counter.inc(s);
        }

        assertEquals(0.40, counter.getShare("a"), 0.06);
        assertEquals(0.30, counter.getShare("b"), 0.06);
        assertEquals(0.20, counter.getShare("c"), 0.06);
        assertEquals(0.10, counter.getShare("d"), 0.06);
    }
}