package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class DataCollectionTest {

    @Test
    void testViewParent() {
        DataCollection<String> coll = new DataCollection<>();
        DataCollection.View<String> aStart = coll.createView(null);
        assertSame(coll, aStart.getCollection());
    }

    @Test
    void testNullView() {
        DataCollection<String> coll = new DataCollection<>();
        assertTrue(coll.addAll("aa", "ab", "ba", "bb"));
        DataCollection.View<String> view = coll.createView(null);
        assertEquals(coll.toList(), view.toList());
    }

    @Test
    void testViewSize() {
        DataCollection<String> coll = new DataCollection<>();
        assertTrue(coll.addAll("aa", "ab", "ba", "bb"));
        DataCollection.View<String> aStart = coll.createView(str -> str.startsWith("a"));
        assertEquals(2, aStart.size());
    }

    @Test
    void testViewContains() {
        DataCollection<String> coll = new DataCollection<>();
        assertTrue(coll.addAll("aa", "ab", "ba", "bb"));
        DataCollection.View<String> aStart = coll.createView(str -> str.startsWith("a"));
        assertTrue(aStart.contains("aa"));
        assertTrue(aStart.contains("ab"));
        assertFalse(aStart.contains("ba"));
        assertFalse(aStart.contains("bb"));
    }

    @Test
    void testViewAdd() {
        DataCollection<String> coll = new DataCollection<>();
        assertTrue(coll.addAll("aa", "ab", "ba", "bb"));
        DataCollection.View<String> aStart = coll.createView(str -> str.startsWith("a"));
        assertTrue(aStart.add("ac"));
        assertFalse(aStart.add("ca"));
        assertEquals(3, aStart.size());
        assertTrue(aStart.contains("ac"));
    }

    @Test
    void testViewRemoveParent() {
        DataCollection<String> coll = new DataCollection<>();
        assertTrue(coll.addAll("aa", "ab", "ba", "bb"));
        DataCollection.View<String> aStart = coll.createView(str -> str.startsWith("a"));
        assertEquals(2, aStart.size());
        assertEquals(4, coll.size());
        assertTrue(coll.remove("ba"));
        assertEquals(2, aStart.size());
        assertEquals(3, coll.size());
    }

    @Test
    void testWith2Views() {
        DataCollection<String> coll = new DataCollection<>();
        assertTrue(coll.addAll("aa", "ab", "ba", "bb"));
        DataCollection.View<String> aStart = coll.createView(str -> str.startsWith("a"));
        DataCollection.View<String> bEnd = coll.createView(str -> str.endsWith("b"));

        assertTrue(aStart.add("ac"));
        assertTrue(bEnd.add("cb"));

        assertEquals(CollectionUtil.arrayListOf("aa", "ab", "ac"), aStart.toList());
        assertEquals(CollectionUtil.arrayListOf("ab", "bb", "cb"), bEnd.toList());

        assertTrue(aStart.remove("ab"));
        assertEquals(CollectionUtil.arrayListOf("aa", "ac"), aStart.toList());
        assertEquals(CollectionUtil.arrayListOf("bb", "cb"), bEnd.toList());

        assertTrue(bEnd.add("ab"));
        assertEquals(CollectionUtil.arrayListOf("aa", "ac", "ab"), aStart.toList());
        assertEquals(CollectionUtil.arrayListOf("bb", "cb", "ab"), bEnd.toList());

        bEnd.clear();
        assertEquals(CollectionUtil.arrayListOf("aa", "ac"), aStart.toList());
        assertEquals(CollectionUtil.arrayListOf("aa", "ba", "ac"), coll.toList());
    }
}