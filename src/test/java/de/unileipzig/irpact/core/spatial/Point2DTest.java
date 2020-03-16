package de.unileipzig.irpact.core.spatial;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

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
    void testDistance() {
        Point2D p0 = new Point2D(2, 3);
        Point2D p1 = new Point2D(5, 7);
        assertEquals(5, p0.distance(p1));
        assertEquals(5, p1.distance(p0));
    }

    @Test
    void testGetKNearest() {
        Point2D p0 = new Point2D(0, 0);
        Point2D p100 = new Point2D(100, 100);
        List<Point2D> pList = new ArrayList<>();
        for(int i = 1; i < 10; i++) {
            pList.add(new Point2D(i, i));
        }
        Collections.shuffle(pList, new Random(123));

        List<Point2D> p0nearest4 = p0.getKNearest(pList, 4);
        assertEquals(4, p0nearest4.size());
        assertEquals(new Point2D(1, 1), p0nearest4.get(0));
        assertEquals(new Point2D(2, 2), p0nearest4.get(1));
        assertEquals(new Point2D(3, 3), p0nearest4.get(2));
        assertEquals(new Point2D(4, 4), p0nearest4.get(3));

        List<Point2D> p100nearest5 = p100.getKNearest(pList, 5);
        assertEquals(new Point2D(9, 9), p100nearest5.get(0));
        assertEquals(new Point2D(8, 8), p100nearest5.get(1));
        assertEquals(new Point2D(7, 7), p100nearest5.get(2));
        assertEquals(new Point2D(6, 6), p100nearest5.get(3));
        assertEquals(new Point2D(5, 5), p100nearest5.get(4));
    }
}