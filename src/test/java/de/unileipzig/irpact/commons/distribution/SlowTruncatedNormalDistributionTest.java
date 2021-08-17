package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.util.Rnd;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * @author Daniel Abitz
 */
class SlowTruncatedNormalDistributionTest {

    @Disabled
    @Test
    void testSlow() {
        SlowTruncatedNormalDistribution stnd = new SlowTruncatedNormalDistribution();
        stnd.setRandom(new Rnd(42));
        stnd.setMean(0);
        stnd.setStandardDeviation(1);
        stnd.setLowerBound(5.5);
        stnd.setUpperBound(10);

        long start = System.currentTimeMillis();
        double x = assertDoesNotThrow(stnd::drawDoubleValue);
        long end = System.currentTimeMillis();
        System.out.println(x + " -> " + (end - start));
    }

    @Disabled
    @Test
    void testFast() {
        TruncatedNormalDistribution tnd = new TruncatedNormalDistribution();
        tnd.setRandom(new Rnd(42));
        tnd.setMu(0);
        tnd.setSigma(1);
        tnd.setSupportLowerBound(5.5);
        tnd.setSupportUpperBound(10);
        tnd.initalize();

        long start = System.currentTimeMillis();
        double x = assertDoesNotThrow(tnd::drawDoubleValue);
        long end = System.currentTimeMillis();
        System.out.println(x + " -> " + (end - start));
    }
}