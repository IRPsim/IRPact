package de.unileipzig.irpact.commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class TripleMappingTest {

    @Test
    void testPutAndGet() {
        TripleMapping<String, String, Integer> tm = new TripleMapping<>();
        tm.put("a", "x", 1);
        assertEquals(1, tm.get("a", "x"));
        assertEquals(2, tm.get("a", "y", 2));
    }

    @Test
    void testSize() {
        TripleMapping<String, String, Integer> tm = new TripleMapping<>();
        tm.put("a", "x", 1);
        assertEquals(1, tm.size("a"));
        assertEquals(0, tm.size("b"));
        tm.put("b", "x", 2);
        assertEquals(1, tm.size("a"));
        assertEquals(1, tm.size("b"));
        assertEquals(2, tm.size());
    }
}