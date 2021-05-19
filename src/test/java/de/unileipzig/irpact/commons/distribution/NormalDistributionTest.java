package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.util.Rnd;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class NormalDistributionTest {

    @Test
    void testWithRandom() {
        Random r = new Random(123);
        NormalDistribution norm = new NormalDistribution("x", new Rnd(123));
        assertEquals(0, norm.getMean());
        assertEquals(1, norm.getStandardDeviation());
        assertEquals(r.nextGaussian(), norm.drawDoubleValue());
        assertEquals(r.nextGaussian(), norm.drawDoubleValue());
    }

    @Test
    void testWithRandomAndInterval() {
        double m = 5;
        double sd = 10;
        Random r = new Random(123);
        NormalDistribution norm = new NormalDistribution("x", new Rnd(123), sd, m);
        assertEquals(m, norm.getMean());
        assertEquals(sd, norm.getStandardDeviation());
        assertEquals(r.nextGaussian() * sd + m, norm.drawDoubleValue());
        assertEquals(r.nextGaussian() * sd + m, norm.drawDoubleValue());
    }
}