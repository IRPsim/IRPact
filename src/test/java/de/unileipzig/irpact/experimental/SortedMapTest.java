package de.unileipzig.irpact.experimental;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Daniel Abitz
 */
@Disabled
class SortedMapTest {

    @Test
    void runIt() {
        SortedMap<Double, String> map = new TreeMap<>();
        map.put(5.0, "5");
        map.put(3.0, "3");
        map.put(4.0, "4");
        map.put(1.0, "1");
        map.put(2.0, "2");
        System.out.println(map);
        System.out.println(map.headMap(3.0));
        map.headMap(3.0).clear();
        System.out.println(map);
    }
}
