package de.unileipzig.irpact.core.spatial;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class SquareModelTest {

    @Test
    void testCreation() {
        SquareModel model = new SquareModel("test", 0, 1, 2, 3);
        assertEquals("test", model.getName());
        assertEquals(0, model.getX0());
        assertEquals(1, model.getY0());
        assertEquals(2, model.getX1());
        assertEquals(3, model.getY1());
    }

    @Test
    void testErrorCreation() {
        assertThrows(IllegalArgumentException.class, () -> new SquareModel("test", 0, 1, -1, 1));
        assertThrows(IllegalArgumentException.class, () -> new SquareModel("test", 0, 1, 1, -11));
    }
}