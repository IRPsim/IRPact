package de.unileipzig.irpact.commons;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class HistogramTest {

    @Test
    void testUpdate() {
        Histogram h = new Histogram();
        h.setBins(new double[] {-3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5});
        assertEquals(7, h.getNumberOfBins());
        h.setHeight(-3, 3);
        h.updateHeight(-3.3, 5);
        h.updateHeight(-2.9, -1);
        assertEquals(7, h.getBin(0).getHeight());
        assertEquals(7, h.getBin(-3.0).getHeight());
        assertEquals(-3.5, h.getMin());
        assertEquals(3.5, h.getMax());
    }

    @Test
    void testError() {
        Histogram h = new Histogram();
        assertThrows(IllegalStateException.class, () -> h.setBins(new double[]{4, -2}));
        h.setBins(new double[]{-3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5});
        assertThrows(NoSuchElementException.class, () -> h.findBin(-6));
    }
}