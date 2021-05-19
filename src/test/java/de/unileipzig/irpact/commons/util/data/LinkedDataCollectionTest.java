package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class LinkedDataCollectionTest {

    @Test
    void testViewParent() {
        LinkedDataCollection<String> coll = new LinkedDataCollection<>();
        LinkedDataCollection.View<String> aStart = coll.createView(null);
        assertSame(coll, aStart.getCollection());
    }

    @Test
    void testNullView() {
        LinkedDataCollection<String> coll = new LinkedDataCollection<>();
        assertTrue(coll.addAll("aa", "ab", "ba", "bb"));
        LinkedDataCollection.LinkedView<String> view = coll.createView(null);
        assertEquals(coll.toList(), view.toList());
    }

    @Test
    void testViewSize() {
        LinkedDataCollection<String> coll = new LinkedDataCollection<>();
        assertTrue(coll.addAll("aa", "ab", "ba", "bb"));
        LinkedDataCollection.LinkedView<String> aStart = coll.createView(str -> str.startsWith("a"));
        assertEquals(2, aStart.size());
    }

    @Test
    void testViewContains() {
        LinkedDataCollection<String> coll = new LinkedDataCollection<>();
        assertTrue(coll.addAll("aa", "ab", "ba", "bb"));
        LinkedDataCollection.LinkedView<String> aStart = coll.createView(str -> str.startsWith("a"));
        assertTrue(aStart.contains("aa"));
        assertTrue(aStart.contains("ab"));
        assertFalse(aStart.contains("ba"));
        assertFalse(aStart.contains("bb"));
    }

    @Test
    void testViewAdd() {
        LinkedDataCollection<String> coll = new LinkedDataCollection<>();
        assertTrue(coll.addAll("aa", "ab", "ba", "bb"));
        LinkedDataCollection.LinkedView<String> aStart = coll.createView(str -> str.startsWith("a"));
        assertTrue(aStart.add("ac"));
        assertFalse(aStart.add("ca"));
        assertEquals(3, aStart.size());
        assertTrue(aStart.contains("ac"));
    }

    @Test
    void testViewRemoveParent() {
        LinkedDataCollection<String> coll = new LinkedDataCollection<>();
        assertTrue(coll.addAll("aa", "ab", "ba", "bb"));
        LinkedDataCollection.LinkedView<String> aStart = coll.createView(str -> str.startsWith("a"));
        assertEquals(2, aStart.size());
        assertEquals(4, coll.size());
        assertTrue(coll.remove("ba"));
        assertEquals(2, aStart.size());
        assertEquals(3, coll.size());
    }

    @Test
    void testWith2Views() {
        LinkedDataCollection<String> coll = new LinkedDataCollection<>();
        assertTrue(coll.addAll("aa", "ab", "ba", "bb"));
        LinkedDataCollection.LinkedView<String> aStart = coll.createView(str -> str.startsWith("a"));
        LinkedDataCollection.LinkedView<String> bEnd = coll.createView(str -> str.endsWith("b"));

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

    @Test
    void testSubSubView() {
        DataCollection<String> coll = new LinkedDataCollection<>();
        assertTrue(coll.addAll("aa", "ab", "ba", "bb"));
        DataCollection.View<String> aStart = coll.createView(str -> str.startsWith("a"));
        DataCollection.View<String> aStartbEnd = aStart.createView(str -> str.endsWith("b"));

        assertEquals(1, aStartbEnd.size());
        assertTrue(aStartbEnd.contains("ab"));
        assertEquals("ab", aStartbEnd.remove(0));
        assertTrue(aStartbEnd.isEmpty());

        assertEquals(1, aStart.size());
        assertEquals(3, coll.size());

        assertEquals(CollectionUtil.arrayListOf("aa"), aStart.toList());
        assertEquals(CollectionUtil.arrayListOf("aa", "ba", "bb"), coll.toList());
    }

    @Test
    void testInitialLock() {
        LinkedDataCollection<String> coll = new LinkedDataCollection<>();
        assertFalse(coll.hasLock());
    }

    @Test
    void testEnableLock() {
        LinkedDataCollection<String> coll = new LinkedDataCollection<>();
        assertNull(coll.lock);
        coll.enableLock();
        assertTrue(coll.hasLock());
        assertNotNull(coll.lock);
        coll.disableLock();
        assertFalse(coll.hasLock());
    }

    @Disabled
    @Test
    void testWithoutLocking() {
        LinkedDataCollection<String> coll = new LinkedDataCollection<>();
        assertFalse(coll.hasLock());
        coll.add("a");

        Thread t = new Thread(() -> {
            coll.lock();
            try {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                assertTrue(coll.remove("a"));
            } finally {
                coll.unlock();
            }
        });
        t.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        coll.lock();
        assertTrue(coll.contains("a"));
        coll.unlock();

        assertDoesNotThrow((Executable) t::join);
    }

    @Disabled
    @Test
    void testWithLocking() {
        LinkedDataCollection<String> coll = new LinkedDataCollection<>();
        coll.enableLock();
        assertTrue(coll.hasLock());
        coll.add("a");

        Thread t = new Thread(() -> {
            coll.lock();
            try {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                assertTrue(coll.remove("a"));
            } finally {
                coll.unlock();
            }
        });
        t.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        coll.lock();
        assertFalse(coll.contains("a"));
        coll.unlock();

        assertDoesNotThrow((Executable) t::join);
    }

    @Test
    void testRemoveFirst() {
        LinkedDataCollection<String> coll = new LinkedDataCollection<>();
        assertTrue(coll.addAll("aa", "ab", "ba", "bb"));
        LinkedDataCollection.LinkedView<String> aStart = coll.createView(str -> str.startsWith("a"));
        LinkedDataCollection.LinkedView<String> bEnd = coll.createView(str -> str.endsWith("b"));

        assertTrue(aStart.contains("aa"));
        assertTrue(bEnd.contains("bb"));

        assertEquals("aa", aStart.removeFirst(str -> str.endsWith("a")));
        assertEquals("bb", bEnd.removeFirst(str -> str.startsWith("b")));

        assertFalse(aStart.contains("aa"));
        assertFalse(bEnd.contains("bb"));

        assertEquals(CollectionUtil.arrayListOf("ab", "ba"), coll.toList());
        assertEquals(CollectionUtil.arrayListOf("ab"), aStart.toList());
        assertEquals(CollectionUtil.arrayListOf("ab"), bEnd.toList());

        assertEquals("ab", coll.removeFirst("ab"::equals));
        assertEquals(CollectionUtil.arrayListOf("ba"), coll.toList());
        assertTrue(aStart.isEmpty());
        assertTrue(bEnd.isEmpty());
    }
}