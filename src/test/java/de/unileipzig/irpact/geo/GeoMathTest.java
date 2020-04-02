package de.unileipzig.irpact.geo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class GeoMathTest {

    @Test
    void testHaversineDistMeter() {
        double hDist = GeoMath.haversineDistance(
                36.12, -86.67,
                33.94, -118.40,
                GeoMath.EARTH_RADIUS_METER
        );
        assertEquals(2886444, (int) hDist);
    }

    @Test
    void testHaversineDistKilometer() {
        double hDist = GeoMath.haversineDistance(
                36.12, -86.67,
                33.94, -118.40,
                GeoMath.EARTH_RADIUS_KILOMETER
        );
        assertEquals(2886, (int) hDist);
    }
}