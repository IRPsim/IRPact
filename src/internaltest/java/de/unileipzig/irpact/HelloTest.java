package de.unileipzig.irpact;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Daniel Abitz
 */
public class HelloTest {

    @Test
    void testHello() {
        String a = "a";
        String b = "b";
        assertNotEquals(a, b);
        assertEquals("a", a);
        assertEquals("b", b);
    }
}
