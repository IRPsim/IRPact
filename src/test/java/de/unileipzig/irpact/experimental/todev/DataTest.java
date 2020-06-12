package de.unileipzig.irpact.experimental.todev;

import de.unileipzig.irpact.experimental.annotation.ToDevelop;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
@ToDevelop
class DataTest {

    @Test
    void testEmpty() {
        Data<Integer> d = new Data<>();
        assertFalse(d.has());
        assertTrue(d.isEmpty());
        assertThrows(NoSuchElementException.class, d::get);
        assertEquals(42, d.getOr(42));
    }

    @Test
    void testNotEmpty() {
        Data<Integer> d = new Data<>(1);
        assertTrue(d.has());
        assertFalse(d.isEmpty());
        assertEquals(1, d.get());
        assertEquals(1, d.getOr(42));
    }

    @Test
    void testSet() {
        Data<Integer> d = new Data<>();
        assertTrue(d.isEmpty());
        d.set(42);
        assertTrue(d.has());
        assertEquals(42, d.get());
    }

    @Test
    void testClear() {
        Data<Integer> d = new Data<>(1);
        assertTrue(d.has());
        d.clear();
        assertTrue(d.isEmpty());
    }
}