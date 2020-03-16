package de.unileipzig.irpact.core.spatial.dim2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Daniel Abitz
 */
class Point2DTest {

    @Test
    void testCreation() {
        Point2D p = new Point2D(1, 2);
        assertEquals(1, p.getX());
        assertEquals(2, p.getY());
    }
}