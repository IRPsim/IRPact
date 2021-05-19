package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.DataCounter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class RemoveOnDrawMapBasedWeightedMappingTest {

    @Test
    void testRemoveOnDraw() {
        RemoveOnDrawMapBasedWeightedMapping<String> wm = new RemoveOnDrawMapBasedWeightedMapping<>();
        wm.set("a", 0.7);
        wm.set("b", 0.25);
        assertEquals(2, wm.size());
        wm.getWeightedRandom(new Rnd());
        assertEquals(1, wm.size());
        wm.getWeightedRandom(new Rnd());
        assertEquals(0, wm.size());
        assertThrows(IllegalStateException.class, () -> wm.getWeightedRandom(new Rnd()));
    }

    protected List<String> getRandomDrawList(Rnd rnd) {
        RemoveOnDrawMapBasedWeightedMapping<String> wm = new RemoveOnDrawMapBasedWeightedMapping<>();
        wm.set("a", 0.7);
        wm.set("b", 0.25);
        wm.set("c", 0.04);
        wm.set("d", 0.01);
        return CollectionUtil.arrayListOf(
                wm.getWeightedRandom(rnd),
                wm.getWeightedRandom(rnd),
                wm.getWeightedRandom(rnd),
                wm.getWeightedRandom(rnd)
        );
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
        assertEquals(700, pos0.get("a"), 20);
        assertEquals(600, pos1.get("b"), 20);
        assertEquals(650, pos2.get("c"), 20);
        assertEquals(800, pos3.get("d"), 20);
    }
}