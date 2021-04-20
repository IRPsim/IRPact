package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.DataCounter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class UnweightListMappingTest {

    @Test
    void testShuffle() {
        UnweightListMapping<String> wm = new UnweightListMapping<>();
        wm.set("a", 0.8);
        wm.set("b", 0.15);
        wm.set("c", 0.05);

        assertEquals(3, wm.getValues().size());

        DataCounter<String> counter = new DataCounter<>();
        Rnd rnd = new Rnd(123);
        for(int i = 0; i < 1000; i++) {
            String drawn = wm.getRandom(rnd);
            counter.inc(drawn);
        }
        wm.shuffle(rnd);

        assertEquals(333, counter.get("a"), 20);
        assertEquals(333, counter.get("b"), 20);
        assertEquals(333, counter.get("c"), 20);
        assertEquals(1000, counter.total());
    }
}