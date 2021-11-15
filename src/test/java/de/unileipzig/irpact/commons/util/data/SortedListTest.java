package de.unileipzig.irpact.commons.util.data;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class SortedListTest {

    @Test
    void testSorted() {
        List<Double> input = new ArrayList<>(Arrays.asList(4.0, 1.0, 3.0, 9.0, 7.0, 2.0));

        List<Double> sorted = new ArrayList<>(input);
        sorted.sort(Double::compare);

        SortedList<Double> list = new SortedList<>();
        list.addAll(input);
        List<Double> sortedList = new ArrayList<>(list);

        assertEquals(sorted, sortedList);
    }

    @Test
    void testRemove() {
        SortedList<Double> list = new SortedList<>(Arrays.asList(4.0, 1.0, 3.0, 9.0, 7.0, 2.0));
        assertTrue(list.remove(3.0));
        assertEquals(Arrays.asList(1.0, 2.0, 4.0, 7.0, 9.0), list);
    }

    @Test
    void testDuplicates() {
        SortedList<Double> list = new SortedList<>(Arrays.asList(4.0, 1.0, 3.0, 9.0, 7.0, 2.0, 4.0));
        assertEquals(Arrays.asList(1.0, 2.0, 3.0, 4.0, 4.0, 7.0, 9.0), list);
    }

    @Test
    void testDuplicatesRemove() {
        SortedList<Double> list = new SortedList<>(Arrays.asList(4.0, 1.0, 3.0, 9.0, 7.0, 2.0, 4.0));
        assertEquals(Arrays.asList(1.0, 2.0, 3.0, 4.0, 4.0, 7.0, 9.0), list);
        assertTrue(list.remove(4.0));
        assertEquals(Arrays.asList(1.0, 2.0, 3.0, 4.0, 7.0, 9.0), list);
        assertTrue(list.remove(4.0));
        assertEquals(Arrays.asList(1.0, 2.0, 3.0, 7.0, 9.0), list);
    }

    @Test
    void testDuplicatesRemoveAll() {
        SortedList<Double> list = new SortedList<>(Arrays.asList(4.0, 1.0, 3.0, 4.0, 9.0, 7.0, 2.0, 4.0));
        assertEquals(Arrays.asList(1.0, 2.0, 3.0, 4.0, 4.0, 4.0, 7.0, 9.0), list);
        assertEquals(3, list.removeAll(4.0));
        assertEquals(Arrays.asList(1.0, 2.0, 3.0, 7.0, 9.0), list);
    }

    @Test
    void testChangeComparator() {
        SortedList<Double> list = new SortedList<>(Arrays.asList(4.0, 1.0, 3.0, 4.0, 9.0, 7.0, 2.0, 4.0));
        assertEquals(Arrays.asList(1.0, 2.0, 3.0, 4.0, 4.0, 4.0, 7.0, 9.0), list);
        list.setComparator((o1, o2) -> -Double.compare(o1, o2));
        assertEquals(Arrays.asList(9.0, 7.0, 4.0, 4.0, 4.0, 3.0, 2.0, 1.0), list);
    }

    @Test
    void testReverseComparator() {
        SortedList<Double> list = new SortedList<>(Arrays.asList(4.0, 1.0, 3.0, 4.0, 9.0, 7.0, 2.0, 4.0));
        assertEquals(Arrays.asList(1.0, 2.0, 3.0, 4.0, 4.0, 4.0, 7.0, 9.0), list);
        list.reverse();
        assertEquals(Arrays.asList(9.0, 7.0, 4.0, 4.0, 4.0, 3.0, 2.0, 1.0), list);
    }

    @Test
    void testIndexStuff() {
        SortedList<Double> list = new SortedList<>();
        list.add(2.0);
        list.add(1.0);
        list.add(2.0);
        list.add(3.0);
        list.add(2.0);
        assertEquals(1, list.indexOf(2.0));
        assertEquals(3, list.lastIndexOf(2.0));
    }

    @Test
    void testRetainAll() {
        SortedList<Double> list = new SortedList<>(Arrays.asList(4.0, 1.0, 3.0, 4.0, 9.0, 7.0, 2.0, 4.0));
        assertTrue(list.retainAll(Arrays.asList(4.0, 9.0, 7.0, 12.0)));
        assertEquals(Arrays.asList(4.0, 4.0, 4.0, 7.0, 9.0), list);
    }

    @Test
    void testRemoveAll() {
        SortedList<Double> list = new SortedList<>(Arrays.asList(4.0, 1.0, 3.0, 4.0, 9.0, 7.0, 2.0, 4.0));
        assertTrue(list.removeAll(Arrays.asList(4.0, 9.0, 7.0, 12.0)));
        assertEquals(Arrays.asList(1.0, 2.0, 3.0, 4.0, 4.0), list);
    }

    @Test
    void testRemoveAllWithDublicates() {
        SortedList<Double> list = new SortedList<>(Arrays.asList(4.0, 1.0, 3.0, 4.0, 9.0, 7.0, 2.0, 4.0));
        assertTrue(list.removeAllWithDublicates(Arrays.asList(4.0, 9.0, 7.0, 12.0)));
        assertEquals(Arrays.asList(1.0, 2.0, 3.0), list);
    }
}