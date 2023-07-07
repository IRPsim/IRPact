package de.unileipzig.irpact.commons.util.data.count;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class CountMap1DTest {

    @Test
    void testCumulate() {
        CountMap1D<Integer> c = new CountMap1D<>();
        c.set(2000, 10);
        c.set(2010, 5);
        c.set(2020, 15);
        CountMap1D<Integer> cumulated = c.cumulate(Integer::compareTo);
        assertEquals(10, cumulated.getCount(2000));
        assertEquals(15, cumulated.getCount(2010));
        assertEquals(30, cumulated.getCount(2020));
    }

    @Test
    void testUncumulate() {
        CountMap1D<Integer> c = new CountMap1D<>();
        c.set(2000, 10);
        c.set(2010, 15);
        c.set(2020, 30);
        CountMap1D<Integer> uncumulated = c.uncumulate(Integer::compareTo);
        assertEquals(10, uncumulated.getCount(2000));
        assertEquals(5, uncumulated.getCount(2010));
        assertEquals(15, uncumulated.getCount(2020));
    }

    @Test
    void testAdd() {
        CountMap1D<Integer> c0 = new CountMap1D<>();
        c0.set(2000, 10);
        c0.set(2010, 5);
        c0.set(2020, 15);
        CountMap1D<Integer> c1 = new CountMap1D<>();
        c1.set(2000, 10);
        c1.set(2010, 15);
        c1.set(2020, 30);

        c0.add(c1);
        assertEquals(20, c0.getCount(2000));
        assertEquals(20, c0.getCount(2010));
        assertEquals(45, c0.getCount(2020));
        assertEquals(10, c1.getCount(2000));
        assertEquals(15, c1.getCount(2010));
        assertEquals(30, c1.getCount(2020));
    }
}