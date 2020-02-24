package de.unileipzig.irpact;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 * @since 0.0
 */
class ThisTestIsALieTest {

    @Test
    void testTruth() {
        assertEquals(2, 1 + 1);
    }

    @Test
    void testLie() {
        assertEquals(42, 1 + 1);
    }
}
