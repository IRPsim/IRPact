package de.unileipzig.irpact.commons.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class CounterTest {

    @Test
    void testSetZeroOnUpdate() {
        Counter c = new Counter(-1L, true);
        assertEquals(-1L, c.get());
        c.inc(5);
        assertEquals(5L, c.get());
    }

    @Test
    void testSetZeroOnUpdateWithoutFlag() {
        Counter c = new Counter(-1L, false);
        assertEquals(-1L, c.get());
        c.inc(5L);
        assertEquals(4L, c.get());
    }
}