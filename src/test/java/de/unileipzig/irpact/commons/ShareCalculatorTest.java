package de.unileipzig.irpact.commons;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.ShareCalculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class ShareCalculatorTest {

    @Test
    void testShareCalculation() {
        ShareCalculator<String> sc = new ShareCalculator<>();
        sc.setSize("a", 10);
        sc.setSize("b", 15);
        sc.setSize("c", 20);
        sc.calculateShares(45);

        assertEquals(10.0/45.0, sc.getShare("a"));
        assertEquals(15.0/45.0, sc.getShare("b"));
        assertEquals(20.0/45.0, sc.getShare("c"));
    }

    @Test
    void testSetSize() {
        ShareCalculator<String> sc = new ShareCalculator<>();
        assertDoesNotThrow(() -> sc.setSize("a", 10));
        assertDoesNotThrow(() -> sc.setSize("b", 15));
        assertThrows(IllegalArgumentException.class, () -> sc.setSize("a", 10));
    }

    @Test
    void testReplace() {
        ShareCalculator<String> sc = new ShareCalculator<>();
        sc.setSize("a", 10);
        assertEquals(10, sc.getSize("a"));
        assertEquals(10, sc.replaceSize("a", 20));
        assertEquals(20, sc.getSize("a"));
    }

    @Test
    void testSumSizes() {
        ShareCalculator<String> sc = new ShareCalculator<>();
        sc.setSize("a", 10);
        sc.setSize("b", 15);
        sc.setSize("c", 20);

        assertEquals(45, sc.sumSizes(CollectionUtil.arrayListOf("a", "b", "c")));
    }

    @Test
    void testProportionalSize() {
        ShareCalculator<String> sc = new ShareCalculator<>();
        sc.setSize("a", 10);
        sc.setSize("b", 15);
        sc.setSize("c", 20);
        sc.calculateShares(45);

        assertEquals((int) ((10.0/45.0) * 100), sc.getProportionalSize("a", 100));
        assertEquals((int) ((15.0/45.0) * 200), sc.getProportionalSize("b", 200));
        assertEquals((int) ((20.0/45.0) * 300), sc.getProportionalSize("c", 300));
    }

    @Test
    void testKeyWithLargestShare() {
        ShareCalculator<String> sc = new ShareCalculator<>();
        sc.setSize("a", 10);
        sc.setSize("b", 15);
        sc.setSize("c", 20);
        sc.calculateShares(45);

        assertEquals("c", sc.getKeyWithLargestShare());
    }
}