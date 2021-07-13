package de.unileipzig.irpact;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class HelloTest {

    @Test
    void testHello() {
        String a = "a";
        String b = "b";
        assertNotEquals(a, b);
        assertEquals("a", a);
        assertEquals("b", b);
    }
}
