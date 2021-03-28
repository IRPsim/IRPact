package de.unileipzig.irpact.commons;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.weighted.BasicWeightedMapping;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class BasicWeightedMappingTest {

    @Test
    void testWithDouble() {
        BasicWeightedMapping<String, String, Number> wm = new BasicWeightedMapping<>();
        wm.put("a", "x", 10);
        wm.put("a", "y", 5);
        wm.put("a", "z", 1);
        wm.put("z", "a", 1);
        wm.put("z", "b", 5);
        wm.put("z", "c", 10);

        int ax = 0;
        int ay = 0;
        int az = 0;
        Rnd rnd = new Rnd(123);
        for(int i = 0; i < 1000; i++) {
            String t = wm.getWeightedRandom("a", rnd);
            if("x".equals(t)) ax++;
            else if("y".equals(t)) ay++;
            else if("z".equals(t)) az++;
            else fail();
        }
        assertTrue(ax > ay);
        assertTrue(ay > az);
        assertTrue(ax > 500);
        assertEquals(1000, ax + ay + az);

        int za = 0;
        int zb = 0;
        int zc = 0;
        Rnd rnd2 = new Rnd(123);
        for(int i = 0; i < 1000; i++) {
            String t = wm.getWeightedRandom("z", rnd2);
            if("a".equals(t)) za++;
            else if("b".equals(t)) zb++;
            else if("c".equals(t)) zc++;
            else fail();
        }
        assertTrue(zc > zb);
        assertTrue(zb > za);
        assertTrue(zc > 500);
        assertEquals(1000, za + zb + zc);
    }
}