package de.unileipzig.irpact.core.spatial.dim2;

import de.unileipzig.irpact.commons.distribution.Distribution;
import de.unileipzig.irpact.core.spatial.Metric;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Daniel Abitz
 */
class SquareModelDistributionTest {

    @Test
    void testCreationWithoutSeed() {
        SquareModel model = new SquareModel("unit", Metric.EUCLIDEAN,  0, 0, 1, 1);
        Random rnd = new Random(123);
        SquareModelDistribution dist = new SquareModelDistribution("dist", model, rnd);
        assertEquals("dist", dist.getName());
        assertEquals(SquareModelDistribution.NO_SEED, dist.getSeed());
        assertEquals(rnd, dist.getRandom());
        assertEquals(model, dist.getModel());
    }

    @Test
    void testCreationWithSeed() {
        SquareModel model = new SquareModel("unit", Metric.EUCLIDEAN, 0, 0, 1, 1);
        SquareModelDistribution dist = new SquareModelDistribution("dist", model, 42);
        assertEquals("dist", dist.getName());
        assertEquals(42, dist.getSeed());
        assertNotNull(dist.getRandom());
        assertEquals(model, dist.getModel());

        Random testRnd = new Random(42);
        assertEquals(testRnd.nextDouble(), dist.getRandom().nextDouble());
        assertEquals(testRnd.nextDouble(), dist.getRandom().nextDouble());
        assertEquals(testRnd.nextDouble(), dist.getRandom().nextDouble());
    }

    @Test
    void testPointCreation() {
        SquareModel model = new SquareModel("unit", Metric.EUCLIDEAN, 0, 0, 1, 1);
        SquareModelDistribution dist = new SquareModelDistribution("dist", model, 42);
        Point2D p = dist.drawValue();
        assertNotNull(p);
    }
}