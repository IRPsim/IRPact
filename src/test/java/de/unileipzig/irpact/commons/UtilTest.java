package de.unileipzig.irpact.commons;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class UtilTest {

    @Test
    void testHash2Doubles() {
        assertEquals(
                Objects.hash(42.0, 1337.0),
                Util.hash(42.0, 1337.0)
        );
    }

    @Test
    void testHash2Values() {
        assertEquals(
                Objects.hash("a", "b"),
                Util.hash("a", "b")
        );
    }

    @Test
    void testHash3Values() {
        assertEquals(
                Objects.hash("a", "b", "c"),
                Util.hash("a", "b", "c")
        );
    }

    @Test
    void testHashValueDouble() {
        assertEquals(
                Objects.hash("a", 42.0),
                Util.hash("a", 42.0)
        );
    }

    @Test
    void testHashValueLong() {
        assertEquals(
                Objects.hash("a", 42L),
                Util.hash("a", 42L)
        );
    }
}