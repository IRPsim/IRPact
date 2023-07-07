package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.util.Rnd;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class TruncatedNormalDistributionTest {

    @Test
    void testInitalized() {
        TruncatedNormalDistribution tnd = new TruncatedNormalDistribution();
        assertTrue(tnd.isNotInitalized());
        tnd.setRandom(new Rnd(123));
        tnd.setMu(0);
        tnd.setSigma(1);
        tnd.setSupportUpperBound(-1);
        tnd.setSupportUpperBound(1);
        tnd.initalize();
        assertTrue(tnd.isInitalized());
        tnd.setRandom(new Rnd(42));
        assertFalse(tnd.isInitalized());
        tnd.initalize();
        assertTrue(tnd.isInitalized());
    }

    @Test
    void testCreation() {
        TruncatedNormalDistribution tnd = new TruncatedNormalDistribution(
                new Rnd(123),
                0,
                1,
                2,
                3
        );
        assertTrue(tnd.isInitalized());
        assertEquals(0, tnd.getMu());
        assertEquals(1, tnd.getSigma());
        assertEquals(2, tnd.getSupportLowerBound());
        assertEquals(3, tnd.getSupportUpperBound());
        assertEquals(123, tnd.getRandom().getInitialSeed());
    }

    @Test
    void testRange42Range23() {
        TruncatedNormalDistribution tnd = new TruncatedNormalDistribution(
                new Rnd(42),
                0,
                1,
                2,
                3
        );
        for(int i = 0; i < 10; i++) {
            double s = tnd.sample();
            assertTrue(tnd.getLowerBound() <= s);
            assertTrue(s <= tnd.getUpperBound());
        }
    }

    @Test
    void testRange42Range05() {
        TruncatedNormalDistribution tnd = new TruncatedNormalDistribution(
                new Rnd(42),
                0,
                1,
                -5,
                0
        );
        for(int i = 0; i < 10; i++) {
            double s = tnd.sample();
            assertTrue(tnd.getLowerBound() <= s);
            assertTrue(s <= tnd.getUpperBound());
        }
    }

    @Test
    void testRangeX() {
        TruncatedNormalDistribution tnd = new TruncatedNormalDistribution(
                new Rnd(),
                0,
                1,
                2,
                3
        );
        for(int i = 0; i < 10; i++) {
            double s = tnd.sample();
            assertTrue(tnd.getLowerBound() <= s, "seed: " + tnd.getRandom().getInitialSeed());
            assertTrue(s <= tnd.getUpperBound(), "seed: " + tnd.getRandom().getInitialSeed());
        }
    }

    @Test
    void testRoundMode() {
        TruncatedNormalDistribution tnd0 = new TruncatedNormalDistribution(
                new Rnd(1),
                5,
                1,
                2,
                8
        );
        TruncatedNormalDistribution tnd1 = new TruncatedNormalDistribution(
                new Rnd(1),
                5,
                1,
                2,
                8
        );
        TruncatedNormalDistribution tnd2 = new TruncatedNormalDistribution(
                new Rnd(1),
                5,
                1,
                2,
                8
        );
        TruncatedNormalDistribution tnd3 = new TruncatedNormalDistribution(
                new Rnd(1),
                5,
                1,
                2,
                8
        );

        tnd0.setRoundingMode(RoundingMode.NONE);
        tnd1.setRoundingMode(RoundingMode.FLOOR);
        tnd2.setRoundingMode(RoundingMode.CEIL);
        tnd3.setRoundingMode(RoundingMode.ROUND);

        assertEquals(5.6, tnd0.drawDoubleValue(), 0.1);
        assertEquals(5.0, tnd1.drawDoubleValue());
        assertEquals(6.0, tnd2.drawDoubleValue());
        assertEquals(6.0, tnd3.drawDoubleValue());
    }
}