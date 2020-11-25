package de.unileipzig.irpact.commons;

import de.unileipzig.irpact.v2.commons.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class PairTest {

    @Test
    void testCreation() {
        Pair<String, Integer> p = Pair.get("x", 1);
        assertEquals("x", p.first());
        assertEquals(1, p.second());
    }

    @Test
    void testHashCode() {
        Pair<String, Integer> p1 = Pair.get("x", 1);
        Pair<String, Integer> p2 = Pair.get("x", 1);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testEquals() {
        Pair<String, Integer> p = Pair.get("x", 1);
        assertEquals(p, Pair.get("x", 1));
        assertNotEquals(p, Pair.get(1, "x"));
        assertNotSame(p, Pair.get("x", 1));
    }

    @Test
    void testSortAfterSecondValue() {
        List<Pair<String, Integer>> list = new ArrayList<>();
        list.add(Pair.get("a", 1));
        list.add(Pair.get("b", 5));
        list.add(Pair.get("c", 3));
        list.add(Pair.get("d", 8));
        list.sort(Comparator.comparingInt(Pair::second));
        assertEquals(list.get(0), Pair.get("a", 1));
        assertEquals(list.get(1), Pair.get("c", 3));
        assertEquals(list.get(2), Pair.get("b", 5));
        assertEquals(list.get(3), Pair.get("d", 8));
    }

    @Test
    void testSortAfterFirstValue() {
        List<Pair<Integer, String>> list = new ArrayList<>();
        list.add(Pair.get(1, "a"));
        list.add(Pair.get(5, "b"));
        list.add(Pair.get(3, "c"));
        list.add(Pair.get(8, "d"));
        list.sort(Comparator.comparingInt(Pair::first));
        assertEquals(list.get(0), Pair.get(1, "a"));
        assertEquals(list.get(1), Pair.get(3, "c"));
        assertEquals(list.get(2), Pair.get(5, "b"));
        assertEquals(list.get(3), Pair.get(8, "d"));
    }
}