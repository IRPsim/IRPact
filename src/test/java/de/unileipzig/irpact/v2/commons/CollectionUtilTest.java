package de.unileipzig.irpact.v2.commons;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class CollectionUtilTest {

    @Test
    void testRemove() {
        Set<String> set = CollectionUtil.linkedHashSetOf("a", "b", "c", "d");
        assertEquals("c", CollectionUtil.remove(set, 2));
        assertEquals(CollectionUtil.hashSetOf("a", "b", "d"), set);
    }
}