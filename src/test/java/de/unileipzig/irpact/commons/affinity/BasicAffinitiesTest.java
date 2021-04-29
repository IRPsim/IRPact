package de.unileipzig.irpact.commons.affinity;

import de.unileipzig.irpact.commons.util.Rnd;
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
}