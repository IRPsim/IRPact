package de.unileipzig.irpact.commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class DoublePairTest {

    @Test
    void testCreation() {
        DoublePair p = DoublePair.get(1.0, 4.0);
        assertEquals(1.0, p.first());
        assertEquals(4.0, p.second());
    }

    @Test
    void testHashCode() {
        DoublePair p1 = DoublePair.get(1.0, 4.0);
        DoublePair p2 = DoublePair.get(1.0, 4.0);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testEquals() {
        DoublePair p = DoublePair.get(1.0, 4.0);
        assertEquals(p, DoublePair.get(1.0, 4.0));
        assertNotEquals(p, DoublePair.get(4.0, 1.0));
        assertNotSame(p, DoublePair.get(1.0, 4.0));
    }

    @Test
    void testSortAfterSecondValue() {
        List<DoublePair> list = new ArrayList<>();
        list.add(DoublePair.get(1.0, 5.0));
        list.add(DoublePair.get(2.0, 3.0));
        list.add(DoublePair.get(3.0, 7.0));
        list.add(DoublePair.get(4.0, -1.0));
        list.sort(Comparator.comparingDouble(DoublePair::second));
        assertEquals(list.get(0), DoublePair.get(4.0, -1.0));
        assertEquals(list.get(1), DoublePair.get(2.0, 3.0));
        assertEquals(list.get(2), DoublePair.get(1.0, 5.0));
        assertEquals(list.get(3), DoublePair.get(3.0, 7.0));
    }

    @Test
    void testSortAfterFirstValue() {
        List<DoublePair> list = new ArrayList<>();
        list.add(DoublePair.get(5.0, 1.0));
        list.add(DoublePair.get(3.0, 2.0));
        list.add(DoublePair.get(7.0, 3.0));
        list.add(DoublePair.get(-1.0, 4.0));
        list.sort(Comparator.comparingDouble(DoublePair::first));
        assertEquals(list.get(0), DoublePair.get(-1.0, 4.0));
        assertEquals(list.get(1), DoublePair.get(3.0, 2.0));
        assertEquals(list.get(2), DoublePair.get(5.0, 1.0));
        assertEquals(list.get(3), DoublePair.get(7.0, 3.0));
    }
}