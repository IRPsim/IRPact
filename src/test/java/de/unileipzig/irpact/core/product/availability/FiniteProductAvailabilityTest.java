package de.unileipzig.irpact.core.product.availability;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class FiniteProductAvailabilityTest {

    @Test
    void testCreation() {
        FiniteProductAvailability ava = new FiniteProductAvailability(3);
        assertEquals(3, ava.getInitialCount());
        assertEquals(3, ava.getCount());
    }

    @Test
    void testDecrement() {
        FiniteProductAvailability ava = new FiniteProductAvailability(3);
        ava.decrement();
        assertEquals(3, ava.getInitialCount());
        assertEquals(2, ava.getCount());
    }

    @Test
    void testIsAvailable() {
        FiniteProductAvailability ava = new FiniteProductAvailability(1);
        assertTrue(ava.isAvailable());
        ava.decrement();
        assertFalse(ava.isAvailable());
        assertEquals(0, ava.getCount());
        ava.decrement();
        assertEquals(0, ava.getCount());
    }
}