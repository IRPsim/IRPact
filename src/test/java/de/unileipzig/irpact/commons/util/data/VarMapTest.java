package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class VarMapTest {

    @Test
    void testCount2() {
        VarMap map = new VarMap(Integer.class, String.class);

        map.put(1, "a");
        map.put(2, "b");
        map.put(3, "c");

        assertEquals(3, map.count());
    }

    @Test
    void testCount3() {
        VarMap map = new VarMap(Integer.class, String.class, Boolean.class);

        map.put(1, "a", true);
        map.put(2, "b", false);
        map.put(3, "c", true);
        map.put(3, "c", false);

        assertEquals(4, map.count());
    }

    @Test
    void testStream2() {
        VarMap map = new VarMap(Integer.class, String.class);

        map.put(1, "a");
        map.put(2, "b");
        map.put(3, "c");

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
        VarMap map = new VarMap(Integer.class, String.class, Boolean.class);

        map.put(1, "a", true);
        map.put(2, "b", false);
        map.put(3, "c", true);
        map.put(3, "c", false);

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
        VarMap map = new VarMap(Integer.class, String.class, Boolean.class);

        map.put(1, "a", true);
        map.put(2, "b", false);
        map.put(3, "c", true);
        map.put(3, "c", false);

        assertArrayEquals(
                new Object[]{3, "c", true},
                map.get(2)
        );
    }

    @Test
    void testIterable3() {
        VarMap map = new VarMap(Integer.class, String.class, Boolean.class);

        map.put(1, "a", true);
        map.put(2, "b", false);
        map.put(3, "c", true);
        map.put(4, "d", false);

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
        VarMap map = new VarMap(Integer.class, String.class, Boolean.class);

        map.put(1, "a", true);
        map.put(2, "b", false);
        map.put(3, "c", true);
        map.put(4, "d", false);

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
        VarMap map = new VarMap(Integer.class, String.class, Boolean.class);

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
        VarMap map = new VarMap(Integer.class, String.class, Boolean.class);

        map.put(1, "a", true);
        map.put(2, "b", false);
        map.put(3, "c", true);
        map.put(4, "d", false);

        assertEquals(1, map.get(0, 0));
        assertEquals("c", map.getAs(2, 1));
        assertEquals(Boolean.FALSE, map.getAs(3, 2, Boolean.class));
    }

    @Test
    void testFilterIndex3() {
        VarMap map = new VarMap(Integer.class, String.class, Boolean.class);

        map.put(1, "a", true);
        map.put(2, "b", false);
        map.put(3, "c", true);
        map.put(3, "c", false);

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
        VarMap map = new VarMap(Integer.class, String.class, Boolean.class);

        map.put(1, "a", true);
        map.put(2, "b", false);
        map.put(3, "c", true);
        map.put(3, "c", false);

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
        VarMap map = new VarMap(Integer.class, String.class, Boolean.class);

        map.put(1, "a", true);
        map.put(1, "a", false);
        map.put(1, "c", false);
        map.put(2, "a", true);
        map.put(2, "a", false);
        map.put(2, "b", true);
        map.put(2, "b", false);
        map.put(2, "c", true);
        map.put(2, "c", false);
        map.put(3, "a", true);
        map.put(3, "a", false);
        map.put(3, "c", true);
        map.put(3, "c", false);

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
        VarMap map = new VarMap(Integer.class, String.class, Boolean.class);

        map.put(1, "a", true);
        map.put(1, "a", false);
        map.put(1, "c", false);
        map.put(2, "a", true);
        map.put(2, "a", false);
        map.put(2, "b", true);
        map.put(2, "b", false);
        map.put(2, "c", true);
        map.put(2, "c", false);
        map.put(3, "a", true);
        map.put(3, "a", false);
        map.put(3, "c", true);
        map.put(3, "c", false);

        assertEquals(7, map.indexStream(1).count());

        Set<Object> streamed = map.indexStream(1).collect(Collectors.toSet());
        List<Object> streamed2 = map.indexStream(1).distinct().sorted().collect(Collectors.toList());

        assertEquals(CollectionUtil.hashSetOf("a", "b", "c"), streamed);
        assertEquals(CollectionUtil.arrayListOf("a", "b", "c"), streamed2);
    }

    @Test
    void testContains() {
        VarMap map = new VarMap(Integer.class, String.class, Boolean.class);
        map.setAllowNull(true);

        map.put(1, "a", true);
        map.put(1, "a", false);
        map.put(1, "c", false);
        map.put(2, "a", true);
        map.put(2, "a", false);
        map.put(2, "b", true);
        map.put(2, "b", false);
        map.put(2, "c", true);
        map.put(2, "c", false);
        map.put(3, "a", true);
        map.put(3, "a", false);
        map.put(3, "c", true);
        map.put(3, "c", false);

        assertTrue(map.contains(3, "c", false));

        assertFalse(map.contains(4, "c", false));
        assertFalse(map.contains(3, "d", false));
        assertFalse(map.contains(3, "c", null));
    }

    @Test
    void testRemove() {
        VarMap map = new VarMap(Integer.class, String.class, Boolean.class);
        map.setAllowNull(true);

        map.put(1, "a", true);
        map.put(1, "a", false);
        map.put(1, "c", false);
        map.put(2, "a", true);
        map.put(2, "a", false);
        map.put(2, "b", true);
        map.put(2, "b", false);
        map.put(2, "c", true);
        map.put(2, "c", false);
        map.put(3, "a", true);
        map.put(3, "a", false);
        map.put(3, "c", true);
        map.put(3, "c", false);

        assertEquals(13, map.count());

        assertTrue(map.contains(1, "a", true));
        assertTrue(map.remove(1, "a", true));
        assertFalse(map.contains(1, "a", true));
        assertEquals(12, map.count());

        assertTrue(map.contains(3, "a", true));
        assertTrue(map.contains(3, "a", false));
        assertTrue(map.remove(3, "a"));
        assertFalse(map.contains(3, "a", true));
        assertFalse(map.contains(3, "a", false));
        assertEquals(10, map.count());

        assertTrue(map.contains(2, "a", true));
        assertTrue(map.contains(2, "a", false));
        assertTrue(map.contains(2, "b", true));
        assertTrue(map.contains(2, "b", false));
        assertTrue(map.contains(2, "c", true));
        assertTrue(map.contains(2, "c", false));
        assertTrue(map.remove(2));
        assertFalse(map.contains(2, "a", true));
        assertFalse(map.contains(2, "a", false));
        assertFalse(map.contains(2, "b", true));
        assertFalse(map.contains(2, "b", false));
        assertFalse(map.contains(2, "c", true));
        assertFalse(map.contains(2, "c", false));
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
        VarMap map = new VarMap(1);
        map.put("a");
        map.put("b");
        map.put("c");
        map.put("d");

        assertEquals(4, map.count());

        List<Object> list = CollectionUtil.arrayListOf("a", "b", "c", "d");
        List<Object> streamed = map.stream().map(arr -> arr[0]).collect(Collectors.toList());
        assertEquals(list, streamed);
    }

    @Test
    void testFilteredIndexStream() {
        VarMap map = new VarMap(Integer.class, String.class, Boolean.class);

        map.put(1, "a", true);
        map.put(1, "a", false);
        map.put(1, "c", false);
        map.put(2, "a", true);
        map.put(2, "a", false);
        map.put(2, "b", true);
        map.put(2, "b", false);
        map.put(2, "c", true);
        map.put(2, "c", false);
        map.put(3, "a", true);
        map.put(3, "a", false);
        map.put(3, "c", true);
        map.put(3, "c", false);

        Set<String> set = map.filteredIndexStream(1, str -> !str.equals("b"))
                .map(o -> (String) o)
                .collect(Collectors.toSet());
        assertEquals(2, set.size());
        assertTrue(set.contains("a"));
        assertTrue(set.contains("c"));
    }

    @Test
    void testEquals() {
        VarMap map = new VarMap(Integer.class, String.class, Boolean.class);

        map.put(1, "a", true);
        map.put(1, "a", false);
        map.put(1, "c", false);
        map.put(2, "a", true);
        map.put(2, "a", false);
        map.put(2, "b", true);
        map.put(2, "b", false);
        map.put(2, "c", true);
        map.put(2, "c", false);
        map.put(3, "a", true);
        map.put(3, "a", false);
        map.put(3, "c", true);
        map.put(3, "c", false);

        VarMap copy = map.copy();
        assertNotSame(map, copy);

        assertEquals(map, copy);

        assertTrue(map.remove(1));
        assertNotEquals(map, copy);

        assertTrue(copy.remove(1));
        assertEquals(map, copy);
    }
}