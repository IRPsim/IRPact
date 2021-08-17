package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.DataCounter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class RemoveOnDrawUnweightListMappingTest {


    protected List<String> getRandomDrawList(Rnd rnd) {
        RemoveOnDrawUnweightListMapping<String> wm = new RemoveOnDrawUnweightListMapping<>();
        wm.add("a");
        wm.add("b");
        wm.add("c");
        wm.add("d");
        List<String> out = new ArrayList<>();
        assertTrue(wm.getAll(rnd, out));
        assertEquals(4, out.size());
        assertThrows(IllegalStateException.class, () -> wm.getRandom(rnd));
        return out;
    }

    @Test
    void testIt() {
        Rnd rnd = new Rnd(123);
        DataCounter<String> pos0 = new DataCounter<>();
        DataCounter<String> pos1 = new DataCounter<>();
        DataCounter<String> pos2 = new DataCounter<>();
        DataCounter<String> pos3 = new DataCounter<>();
        for(int i = 0; i < 1000; i++) {
            List<String> list = getRandomDrawList(rnd.deriveInstance());
            pos0.inc(list.get(0));
            pos1.inc(list.get(1));
            pos2.inc(list.get(2));
            pos3.inc(list.get(3));
        }
        assertEquals(250, pos0.get("a"), 20);
        assertEquals(250, pos1.get("b"), 20);
        assertEquals(250, pos2.get("c"), 20);
        assertEquals(250, pos3.get("d"), 20);
    }
}