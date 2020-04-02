package de.unileipzig.irpact.core.spatial.dim2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class SquareModelTest {

    @Test
    void testCreation() {
        SquareModel model = new SquareModel("test", CartesianMetric.EUCLIDEAN, 0, 1, 2, 3);
        assertEquals("test", model.getName());
        assertSame(CartesianMetric.EUCLIDEAN, model.getMetric());
        assertEquals(0, model.getX0());
        assertEquals(1, model.getY0());
        assertEquals(2, model.getX1());
        assertEquals(3, model.getY1());
    }

    @Test
    void testErrorCreation() {
        assertThrows(IllegalArgumentException.class, () -> new SquareModel("test", CartesianMetric.EUCLIDEAN, 0, 1, -1, 1));
        assertThrows(IllegalArgumentException.class, () -> new SquareModel("test", CartesianMetric.EUCLIDEAN, 0, 1, 1, -11));
    }

    @Test
    void testEuclideanDistance() {
        SquareModel model = new SquareModel("test", CartesianMetric.EUCLIDEAN, 0, 0, 1, 1);
        Point2D p0 = new Point2D(2, 3);
        Point2D p1 = new Point2D(5, 7);
        assertEquals(5, model.distance(p0, p1));
        assertEquals(5, model.distance(p1, p0));
    }

    @Test
    void testGetKNearestEuclidean() {
        SquareModel model = new SquareModel("test", CartesianMetric.EUCLIDEAN, 0, 0, 1, 1);
        Point2D p0 = new Point2D(0, 0);
        Point2D p100 = new Point2D(100, 100);
        List<Point2D> pList = new ArrayList<>();
        for(int i = 1; i < 10; i++) {
            pList.add(new Point2D(i, i));
        }
        Collections.shuffle(pList, new Random(123));

        List<Point2D> p0nearest4 = model.getKNearest(p0, pList, 4);
        assertEquals(4, p0nearest4.size());
        assertEquals(new Point2D(1, 1), p0nearest4.get(0));
        assertEquals(new Point2D(2, 2), p0nearest4.get(1));
        assertEquals(new Point2D(3, 3), p0nearest4.get(2));
        assertEquals(new Point2D(4, 4), p0nearest4.get(3));

        List<Point2D> p100nearest5 = model.getKNearest(p100, pList, 5);
        assertEquals(new Point2D(9, 9), p100nearest5.get(0));
        assertEquals(new Point2D(8, 8), p100nearest5.get(1));
        assertEquals(new Point2D(7, 7), p100nearest5.get(2));
        assertEquals(new Point2D(6, 6), p100nearest5.get(3));
        assertEquals(new Point2D(5, 5), p100nearest5.get(4));
    }
}