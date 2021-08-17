package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.DataCounter;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class BasicMultiWeightedMappingTest {

    @Test
    void testWithBinary() {
        MultiWeightedMapping<String, Integer> mwm = new BasicMultiWeightedMapping<>((Supplier<WeightedMapping<Integer>>) BinarySearchWeightedMapping::new);
        mwm.set("a", 1, 0.8);
        mwm.set("a", 2, 0.15);
        mwm.set("a", 3, 0.05);
        mwm.set("b", 1, 0.05);
        mwm.set("b", 2, 0.15);
        mwm.set("b", 3, 0.8);

        //A
        DataCounter<Integer> counter = new DataCounter<>();
        Rnd rnd = new Rnd(123);
        for(int i = 0; i < 1000; i++) {
            Integer drawn = mwm.getWeightedRandom("a", rnd);
            counter.inc(drawn);
        }
        assertEquals(800, counter.get(1), 30);
        assertEquals(150, counter.get(2), 30);
        assertEquals(50, counter.get(3), 30);
        assertEquals(1000, counter.total());

        //B
        counter = new DataCounter<>();
        rnd = new Rnd(321);
        for(int i = 0; i < 1000; i++) {
            Integer drawn = mwm.getWeightedRandom("b", rnd);
            counter.inc(drawn);
        }
        assertEquals(800, counter.get(3), 30);
        assertEquals(150, counter.get(2), 30);
        assertEquals(50, counter.get(1), 30);
        assertEquals(1000, counter.total());
    }
}