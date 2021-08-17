package de.unileipzig.irpact.commons.util.table;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class SimpleTableTest {

    @Test
    void testAddColumns() {
        SimpleTable<String> t = new SimpleTable<>();
        assertEquals(0, t.columnCount());
        t.addColumn("a");
        assertEquals(1, t.columnCount());
        t.addColumns("b", "c");
        assertEquals(3, t.columnCount());
    }

    @Test
    void testAddRow() {
        SimpleTable<String> t = new SimpleTable<>();
        t.addColumns("a", "b");

        assertEquals(0, t.rowCount());
        t.addRow();
        assertEquals(1, t.rowCount());
        t.addRows(9);
        assertEquals(10, t.rowCount());
    }
}