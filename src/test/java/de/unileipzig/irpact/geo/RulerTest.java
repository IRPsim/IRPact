package de.unileipzig.irpact.geo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class RulerTest {

    @Test
    void testXYMappingAndDistance2() {
        Ruler r = new Ruler(50, Ruler.Unit.METER);
        double lat1 = 50;
        double lon1 = 12;
        double lat2 = 50;
        double lon2 = -12;

        double x1 = r.toX(lon1);
        double y1 = r.toY(lat1);
        double x2 = r.toX(lon2);
        double y2 = r.toY(lat2);
        double dx = x2 - x1;
        double dy = y2 - y1;

        double dist2 = r.distance2(lat1, lon1, lat2, lon2);

        assertEquals(dx*dx + dy*dy, dist2);

        double dist = r.distance(lat1, lon1, lat2, lon2);
        double hDist = GeoMath.haversineDistance(lat1, lon1, lat2, lon2, GeoMath.EARTH_RADIUS_METER);
        assertEquals(hDist, dist, 20000);
    }
}