package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.ChecksumComparable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class WeightedValueTest {

    @Test
    void testCreation() {
        WeightedValue<String> wv = new WeightedValue<>("a", 1);
        assertEquals("a", wv.getValue());
        assertEquals(1, wv.getWeight());
    }

    @Test
    void testNormalize() {
        WeightedValue<String> wv = new WeightedValue<>("a", 5);
        WeightedValue<String> nwv = wv.normalize(10);
        assertNotSame(wv, nwv);
        assertNotEquals(wv, nwv);
        assertEquals("a", nwv.getValue());
        assertEquals(0.5, nwv.getWeight());
    }

    @Test
    void testChecksum() {
        WeightedValue<String> wv = new WeightedValue<>("a", 1.0);
        assertEquals(
                ChecksumComparable.getChecksum("a", 1.0),
                wv.getChecksum()
        );
    }
}