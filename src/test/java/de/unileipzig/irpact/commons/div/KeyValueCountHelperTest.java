package de.unileipzig.irpact.commons.div;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class KeyValueCountHelperTest {

    @Test
    void testWithStrings() {
        KeyValueCountHelper<String, String> kvc = new KeyValueCountHelper<>(
                () -> new TreeMap<>(Integer::compareTo), LinkedHashSet::new, new HashMap<>()
        );
        kvc.initialize("SET#A", "a0", 0);
        kvc.initialize("SET#A", "a1", 0);
        kvc.initialize("SET#A", "a2", 0);
        kvc.initialize("SET#B", "b0", 0);
        kvc.initialize("SET#B", "b1", 0);
        kvc.initialize("SET#C", "c0", 0);

        assertEquals(
                Arrays.asList("a0", "a1", "a2"),
                kvc.listFirstValues("SET#A")
        );

        kvc.update("a0", 1);
        kvc.update("a1", 1);
        kvc.update("a2", 2);

        assertEquals(
                Arrays.asList("a0", "a1"),
                kvc.listFirstValues("SET#A")
        );

        assertEquals(2, kvc.getCount("a2"));

        kvc.update("c0", -1);

        assertEquals(
                Collections.singletonList("c0"),
                kvc.listFirstValues("SET#C")
        );
        assertEquals(-1, kvc.getCount("c0"));
    }
}