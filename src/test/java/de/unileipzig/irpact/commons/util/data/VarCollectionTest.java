package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class VarCollectionTest {

    @Test
    void testCount2() {
        VarCollection map = new VarCollection(Integer.class, String.class);

        map.varAdd(1, "a");
        map.varAdd(2, "b");
        map.varAdd(3, "c");

        assertEquals(3, map.count());
    }

    @Test
    void testCount3() {
        VarCollection map = new VarCollection(Integer.class, String.class, Boolean.class);

        map.varAdd(1, "a", true);
        map.varAdd(2, "b", false);
        map.varAdd(3, "c", true);
        map.varAdd(3, "c", false);

        assertEquals(4, map.count());
    }

    @Test
    void testStream2() {
        VarCollection map = new VarCollection(Integer.class, String.class);

        map.varAdd(1, "a");
        map.varAdd(2, "b");
        map.varAdd(3, "c");

        List<List<Object>> list = CollectionUtil.arrayListOf(
                CollectionUtil.arrayListOf(1, "a"),
                CollectionUtil.arrayListOf(2, "b"),
                CollectionUtil.arrayListOf(3, "c")
        );

        List<List<Object>> streamed = map.stream()
                .map(Arrays::asList)
                .collect(Collectors.toList());

        assertEquals(list, streamed);
    }

    @Test
    void testStream3() {
        VarCollection map = new VarCollection(Integer.class, String.class, Boolean.class);

        map.varAdd(1, "a", true);
        map.varAdd(2, "b", false);
        map.varAdd(3, "c", true);
        map.varAdd(3, "c", false);

        List<List<Object>> list = CollectionUtil.arrayListOf(
                CollectionUtil.arrayListOf(1, "a", true),
                CollectionUtil.arrayListOf(2, "b", false),
                CollectionUtil.arrayListOf(3, "c", true),
                CollectionUtil.arrayListOf(3, "c", false)
        );

        List<List<Object>> streamed = map.stream()
                .map(Arrays::asList)
                .collect(Collectors.toList());

        assertEquals(list, streamed);
    }

    @Test
    void testGet3() {
        VarCollection map = new VarCollection(Integer.class, String.class, Boolean.class);

        map.varAdd(1, "a", true);
        map.varAdd(2, "b", false);
        map.varAdd(3, "c", true);
        map.varAdd(3, "c", false);

        assertArrayEquals(
                new Object[]{3, "c", true},
                map.get(2)
        );
    }

    @Test
    void testIterable3() {
        VarCollection map = new VarCollection(Integer.class, String.class, Boolean.class);

        map.varAdd(1, "a", true);
        map.varAdd(2, "b", false);
        map.varAdd(3, "c", true);
        map.varAdd(4, "d", false);

        List<List<Object>> list = CollectionUtil.arrayListOf(
                CollectionUtil.arrayListOf(1, "a", true),
                CollectionUtil.arrayListOf(2, "b", false),
                CollectionUtil.arrayListOf(3, "c", true),
                CollectionUtil.arrayListOf(4, "d", false)
        );

        int index = 0;
        for(Object[] entry: map.iterable()) {
            assertEquals(list.get(index), Arrays.asList(entry), "@ index: " + index);
            index++;
        }
    }

    @Test
    void testRemoveIndex() {
        VarCollection map = new VarCollection(Integer.class, String.class, Boolean.class);

        map.varAdd(1, "a", true);
        map.varAdd(2, "b", false);
        map.varAdd(3, "c", true);
        map.varAdd(4, "d", false);

        assertArrayEquals(
                new Object[]{3, "c", true},
                map.removeIndex(2)
        );

        List<List<Object>> list = CollectionUtil.arrayListOf(
                CollectionUtil.arrayListOf(1, "a", true),
                CollectionUtil.arrayListOf(2, "b", false),
                CollectionUtil.arrayListOf(4, "d", false)
        );

        int index = 0;
        for(Object[] entry: map.iterable()) {
            assertEquals(list.get(index), Arrays.asList(entry), "@ index: " + index);
            index++;
        }
    }

    @Test
    void testGetParameters() {
        VarCollection map = new VarCollection(Integer.class, String.class, Boolean.class);

        assertEquals(3, map.getParameters().length);
        assertArrayEquals(
                new Object[]{Integer.class, String.class, Boolean.class},
                map.getParameters()
        );
        assertEquals(Integer.class, map.getParameter(0));
        assertEquals(String.class, map.getParameter(1));
        assertEquals(Boolean.class, map.getParameter(2));
    }

    @Test
    void testGetPosition() {
        VarCollection map = new VarCollection(Integer.class, String.class, Boolean.class);

        map.varAdd(1, "a", true);
        map.varAdd(2, "b", false);
        map.varAdd(3, "c", true);
        map.varAdd(4, "d", false);

        assertEquals(1, map.get(0, 0));
        assertEquals("c", map.getAs(2, 1));
        assertEquals(Boolean.FALSE, map.getAs(3, 2, Boolean.class));
    }

    @Test
    void testFilterIndex3() {
        VarCollection map = new VarCollection(Integer.class, String.class, Boolean.class);

        map.varAdd(1, "a", true);
        map.varAdd(2, "b", false);
        map.varAdd(3, "c", true);
        map.varAdd(3, "c", false);

        List<List<Object>> list = CollectionUtil.arrayListOf(
                CollectionUtil.arrayListOf(3, "c", true),
                CollectionUtil.arrayListOf(3, "c", false)
        );

        List<List<Object>> streamed = map.filteredStream(1, "c"::equals)
                .map(Arrays::asList)
                .collect(Collectors.toList());

        assertEquals(list, streamed);
    }

    @Test
    void testFilterIndex3V2() {
        VarCollection map = new VarCollection(Integer.class, String.class, Boolean.class);

        map.varAdd(1, "a", true);
        map.varAdd(2, "b", false);
        map.varAdd(3, "c", true);
        map.varAdd(3, "c", false);

        List<List<Object>> list = CollectionUtil.arrayListOf(
                CollectionUtil.arrayListOf(1, "a", true),
                CollectionUtil.arrayListOf(3, "c", true)
        );

        List<List<Object>> streamed = map.filteredStream(2, Boolean.TRUE::equals)
                .map(Arrays::asList)
                .collect(Collectors.toList());

        assertEquals(list, streamed);
    }

    @Test
    void testFilterAll3() {
        VarCollection map = new VarCollection(Integer.class, String.class, Boolean.class);

        map.varAdd(1, "a", true);
        map.varAdd(1, "a", false);
        map.varAdd(1, "c", false);
        map.varAdd(2, "a", true);
        map.varAdd(2, "a", false);
        map.varAdd(2, "b", true);
        map.varAdd(2, "b", false);
        map.varAdd(2, "c", true);
        map.varAdd(2, "c", false);
        map.varAdd(3, "a", true);
        map.varAdd(3, "a", false);
        map.varAdd(3, "c", true);
        map.varAdd(3, "c", false);

        assertEquals(13, map.count());

        List<List<Object>> list = CollectionUtil.arrayListOf(
                CollectionUtil.arrayListOf(3, "c", true)
        );

        List<List<Object>> streamed = map.filteredStream(
                x0 -> ((int) x0) % 2 == 1,
                x1 -> "b".equals(x1) || "c".equals(x1),
                Boolean.TRUE::equals
        )
                .map(Arrays::asList)
                .collect(Collectors.toList());

        assertEquals(list, streamed);
    }

    @Test
    void testStreamIndex() {
        VarCollection map = new VarCollection(Integer.class, String.class, Boolean.class);

        map.varAdd(1, "a", true);
        map.varAdd(1, "a", false);
        map.varAdd(1, "c", false);
        map.varAdd(2, "a", true);
        map.varAdd(2, "a", false);
        map.varAdd(2, "b", true);
        map.varAdd(2, "b", false);
        map.varAdd(2, "c", true);
        map.varAdd(2, "c", false);
        map.varAdd(3, "a", true);
        map.varAdd(3, "a", false);
        map.varAdd(3, "c", true);
        map.varAdd(3, "c", false);

        assertEquals(7, map.indexStream(1).count());

        Set<Object> streamed = map.indexStream(1).collect(Collectors.toSet());
        List<Object> streamed2 = map.indexStream(1).distinct().sorted().collect(Collectors.toList());

        assertEquals(CollectionUtil.hashSetOf("a", "b", "c"), streamed);
        assertEquals(CollectionUtil.arrayListOf("a", "b", "c"), streamed2);
    }

    @Test
    void testContains() {
        VarCollection map = new VarCollection(Integer.class, String.class, Boolean.class);
        map.setAllowNull(true);

        map.varAdd(1, "a", true);
        map.varAdd(1, "a", false);
        map.varAdd(1, "c", false);
        map.varAdd(2, "a", true);
        map.varAdd(2, "a", false);
        map.varAdd(2, "b", true);
        map.varAdd(2, "b", false);
        map.varAdd(2, "c", true);
        map.varAdd(2, "c", false);
        map.varAdd(3, "a", true);
        map.varAdd(3, "a", false);
        map.varAdd(3, "c", true);
        map.varAdd(3, "c", false);

        assertTrue(map.varContains(3, "c", false));

        assertFalse(map.varContains(4, "c", false));
        assertFalse(map.varContains(3, "d", false));
        assertFalse(map.varContains(3, "c", null));
    }

    @Test
    void testRemove() {
        VarCollection map = new VarCollection(Integer.class, String.class, Boolean.class);
        map.setAllowNull(true);

        map.varAdd(1, "a", true);
        map.varAdd(1, "a", false);
        map.varAdd(1, "c", false);
        map.varAdd(2, "a", true);
        map.varAdd(2, "a", false);
        map.varAdd(2, "b", true);
        map.varAdd(2, "b", false);
        map.varAdd(2, "c", true);
        map.varAdd(2, "c", false);
        map.varAdd(3, "a", true);
        map.varAdd(3, "a", false);
        map.varAdd(3, "c", true);
        map.varAdd(3, "c", false);

        assertEquals(13, map.count());

        assertTrue(map.varContains(1, "a", true));
        assertTrue(map.varRemove(1, "a", true));
        assertFalse(map.varContains(1, "a", true));
        assertEquals(12, map.count());

        assertTrue(map.varContains(3, "a", true));
        assertTrue(map.varContains(3, "a", false));
        assertTrue(map.varRemove(3, "a"));
        assertFalse(map.varContains(3, "a", true));
        assertFalse(map.varContains(3, "a", false));
        assertEquals(10, map.count());

        assertTrue(map.varContains(2, "a", true));
        assertTrue(map.varContains(2, "a", false));
        assertTrue(map.varContains(2, "b", true));
        assertTrue(map.varContains(2, "b", false));
        assertTrue(map.varContains(2, "c", true));
        assertTrue(map.varContains(2, "c", false));
        assertTrue(map.varRemove(2));
        assertFalse(map.varContains(2, "a", true));
        assertFalse(map.varContains(2, "a", false));
        assertFalse(map.varContains(2, "b", true));
        assertFalse(map.varContains(2, "b", false));
        assertFalse(map.varContains(2, "c", true));
        assertFalse(map.varContains(2, "c", false));
        assertEquals(4, map.count());


        List<List<Object>> list = CollectionUtil.arrayListOf(
                CollectionUtil.arrayListOf(1, "a", false),
                CollectionUtil.arrayListOf(1, "c", false),
                CollectionUtil.arrayListOf(3, "c", true),
                CollectionUtil.arrayListOf(3, "c", false)
        );

        List<List<Object>> streamed = map.stream()
                .map(Arrays::asList)
                .collect(Collectors.toList());

        assertEquals(list, streamed);
    }

    @Test
    void testSingleType() {
        VarCollection map = new VarCollection(1);
        map.varAdd("a");
        map.varAdd("b");
        map.varAdd("c");
        map.varAdd("d");

        assertEquals(4, map.count());

        List<Object> list = CollectionUtil.arrayListOf("a", "b", "c", "d");
        List<Object> streamed = map.stream().map(arr -> arr[0]).collect(Collectors.toList());
        assertEquals(list, streamed);
    }

    @Test
    void testFilteredIndexStream() {
        VarCollection map = new VarCollection(Integer.class, String.class, Boolean.class);

        map.varAdd(1, "a", true);
        map.varAdd(1, "a", false);
        map.varAdd(1, "c", false);
        map.varAdd(2, "a", true);
        map.varAdd(2, "a", false);
        map.varAdd(2, "b", true);
        map.varAdd(2, "b", false);
        map.varAdd(2, "c", true);
        map.varAdd(2, "c", false);
        map.varAdd(3, "a", true);
        map.varAdd(3, "a", false);
        map.varAdd(3, "c", true);
        map.varAdd(3, "c", false);

        Set<String> set = map.filteredIndexStream(1, str -> !str.equals("b"))
                .map(o -> (String) o)
                .collect(Collectors.toSet());
        assertEquals(2, set.size());
        assertTrue(set.contains("a"));
        assertTrue(set.contains("c"));
    }

    @Test
    void testEquals() {
        VarCollection map = new VarCollection(Integer.class, String.class, Boolean.class);

        map.varAdd(1, "a", true);
        map.varAdd(1, "a", false);
        map.varAdd(1, "c", false);
        map.varAdd(2, "a", true);
        map.varAdd(2, "a", false);
        map.varAdd(2, "b", true);
        map.varAdd(2, "b", false);
        map.varAdd(2, "c", true);
        map.varAdd(2, "c", false);
        map.varAdd(3, "a", true);
        map.varAdd(3, "a", false);
        map.varAdd(3, "c", true);
        map.varAdd(3, "c", false);

        VarCollection copy = map.copy();
        assertNotSame(map, copy);

        assertEquals(map, copy);

        assertTrue(map.varRemove(1));
        assertNotEquals(map, copy);

        assertTrue(copy.varRemove(1));
        assertEquals(map, copy);
    }

    @Test
    void testUpdateLast() {
        Supplier<Integer> zero = () -> 0;
        BinaryOperator<Integer> add = Integer::sum;
        VarCollection map = new VarCollection(Integer.class, String.class, Integer.class);
        map.varUpdate(zero, add, 1, "a", 2);
        map.varUpdate(zero, add, 1, "a", 5);
        map.varUpdate(zero, add, 2, "a", 2);
        map.varUpdate(zero, add, 1, "b", 2);
        map.varUpdate(zero, add, 1, "a", 7);
        map.varUpdate(zero, add, 2, "a", -6);

        assertEquals(3, map.count());
        assertTrue(map.varContains(1, "a", 14));
        assertTrue(map.varContains(2, "a", -4));
        assertTrue(map.varContains(1, "b", 2));
    }

    @Test
    void testGet() {
        VarCollection map = new VarCollection(Integer.class, String.class, Double.class);
        map.varAdd(1, "c", 8.8);

        assertEquals(8.8, map.varGet(1, "c"));
    }

    @Test
    void testGetError() {
        VarCollection map = new VarCollection(Integer.class, String.class, Boolean.class);
        map.varAdd(1, "a", true);
        map.varAdd(1, "a", false);

        assertThrows(IllegalStateException.class, () -> map.varGet(1, "a"));
    }

    @Test
    void testTryGet() {
        VarCollection map = new VarCollection(Integer.class, String.class, Double.class);
        map.varAdd(1, "c", 8.8);

        Mutable<Double> mut = Mutable.empty();
        assertTrue(map.varTryGet(mut, 1, "c"));
        assertEquals(8.8, mut.get());
    }

    @Test
    void testTryGetFalse() {
        VarCollection map = new VarCollection(Integer.class, String.class, Double.class);
        map.varAdd(1, "c", 8.8);

        Mutable<Double> mut = Mutable.empty();
        assertFalse(map.varTryGet(mut, 1, "d"));
        assertTrue(mut.isEmpty());

        mut.set(1.0);
        assertFalse(map.varTryGet(mut, 1, "e"));
        assertEquals(1.0, mut.get());
    }

    @Test
    void testTryGetError() {
        VarCollection map = new VarCollection(Integer.class, String.class, Boolean.class);
        map.varAdd(1, "a", true);
        map.varAdd(1, "a", false);

        Mutable<Boolean> mut = Mutable.empty();

        assertThrows(IllegalStateException.class, () -> map.varTryGet(mut, 1, "a"));
    }

    @Test
    void testSet() {
        VarCollection map = new VarCollection(String.class, Integer.class, String.class);

        assertFalse(map.varContains("a", 1, "b"));
        assertNull(map.varSet("a", 1, "b"));
        assertTrue(map.varContains("a", 1, "b"));
        assertEquals("b", map.varSet("a", 1, "c"));
        assertFalse(map.varContains("a", 1, "b"));
        assertTrue(map.varContains("a", 1, "c"));
    }
}