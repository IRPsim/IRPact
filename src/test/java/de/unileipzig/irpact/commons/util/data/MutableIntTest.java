package de.unileipzig.irpact.commons.util.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class MutableIntTest {

    @Test
    void testCreationAndGet() {
        assertEquals(42, new MutableInt(42).get());
        assertEquals(42, MutableInt.wrap(42).get());
    }

    @Test
    void testEmpty() {
        assertTrue(MutableInt.empty().isEmpty());
        assertFalse(MutableInt.one().isEmpty());
    }

    @Test
    void testChangeEmptyState() {
        MutableInt mi = MutableInt.empty();
        assertTrue(mi.isEmpty());
        assertFalse(mi.hasValue());

        mi.set(1);
        assertFalse(mi.isEmpty());
        assertTrue(mi.hasValue());

        mi.clear();
        assertTrue(mi.isEmpty());
        assertFalse(mi.hasValue());
    }

    @Test
    void testSet() {
        MutableInt mi = MutableInt.zero();
        assertEquals(0, mi.get());
        mi.set(42);
        assertEquals(42, mi.get());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void testEmptyGet() {
        MutableInt mi = MutableInt.empty();
        assertThrows(IllegalStateException.class, mi::get);
    }

    @Test
    void testEquals() {
        assertEquals(MutableInt.wrap(42), new MutableInt(42));
    }

    @Test
    void testSetDefaults() {
        MutableInt mi = MutableInt.empty();

        mi.setZero();
        assertEquals(0, mi.get());

        mi.setOne();
        assertEquals(1, mi.get());

        mi.setMaxValue();
        assertEquals(Integer.MAX_VALUE, mi.get());

        mi.setMinValue();
        assertEquals(Integer.MIN_VALUE, mi.get());
    }

    @Test
    void testSetMax() {
        MutableInt mi = MutableInt.wrap(10);
        assertTrue(mi.setMax(12));
        assertEquals(12, mi.get());

        assertFalse(mi.setMax(8));
        assertEquals(12, mi.get());
    }

    @Test
    void testSetMin() {
        MutableInt mi = MutableInt.wrap(10);
        assertFalse(mi.setMin(12));
        assertEquals(10, mi.get());

        assertTrue(mi.setMin(8));
        assertEquals(8, mi.get());
    }

    @Test
    void testInc() {
        MutableInt mi = MutableInt.wrap(10);
        mi.inc();
        assertEquals(11, mi.get());
    }

    @Test
    void testDec() {
        MutableInt mi = MutableInt.wrap(10);
        mi.dec();
        assertEquals(9, mi.get());
    }

    @Test
    void testIncConsumer() {
        MutableInt mi = MutableInt.wrap(10);
        mi.getIncConsumer().accept(null);
        assertEquals(11, mi.get());
    }

    @Test
    void testAdd() {
        MutableInt mi = MutableInt.wrap(10);
        mi.add(5);
        assertEquals(15, mi.get());
    }

    @Test
    void testSub() {
        MutableInt mi = MutableInt.wrap(10);
        mi.sub(5);
        assertEquals(5, mi.get());
    }

    @Test
    void testUpdate() {
        MutableInt mi = MutableInt.wrap(10);
        mi.update(5);
        assertEquals(15, mi.get());
    }

    @Test
    void testSyncUpdate() {
        MutableInt mi = MutableInt.wrap(10);
        mi.syncUpdate(5);
        assertEquals(15, mi.get());
    }
}