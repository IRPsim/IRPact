package de.unileipzig.irpact.core.spatial.dim2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void testManhattenMetrik() {
        Point2D p0 = new Point2D(5, 8);
        Point2D p1 = new Point2D(7, 3);
        assertEquals(7, Point2D.distance(CartesianMetric.MANHATTEN, p0, p1));
        assertEquals(7, Point2D.distance(CartesianMetric.MANHATTEN, p1, p0));
    }

    @Test
    void testEuclideanMetrik() {
        Point2D p0 = new Point2D(2, 3);
        Point2D p1 = new Point2D(5, 7);
        assertEquals(5, Point2D.distance(CartesianMetric.EUCLIDEAN, p0, p1));
        assertEquals(5, Point2D.distance(CartesianMetric.EUCLIDEAN, p1, p0));
    }

    @Test
    void testMaximumMetrik() {
        Point2D p0 = new Point2D(2, 3);
        Point2D p1 = new Point2D(5, 7);
        assertEquals(4, Point2D.distance(CartesianMetric.MAXIMUM, p0, p1));
        assertEquals(4, Point2D.distance(CartesianMetric.MAXIMUM, p1, p0));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void testNullMetric() {
        assertThrows(NullPointerException.class, () -> Point2D.distance(null, new Point2D(0, 0), new Point2D(1, 1)));
    }
}