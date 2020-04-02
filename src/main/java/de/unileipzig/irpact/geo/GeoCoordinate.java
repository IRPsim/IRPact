package de.unileipzig.irpact.geo;

import de.unileipzig.irpact.core.spatial.SpatialInformation;

/**
 * @author Daniel Abitz
 */
public class GeoCoordinate implements SpatialInformation {

    private double latitude;
    private double longitude;

    public GeoCoordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    private static double haversineDistance(GeoCoordinate c0, GeoCoordinate c1) {
        return GeoMath.haversineDistance(
                c0.latitude, c0.longitude,
                c1.latitude, c1.longitude,
                GeoMath.EARTH_RADIUS_METER
        );
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public static double distance(GeoMetric metric, GeoCoordinate c0, GeoCoordinate c1) {
        switch (metric) {
            case HAVERSINE:
                return haversineDistance(c0, c1);
            default:
                throw new IllegalArgumentException("Unsupported Metric: " + metric);
        }
    }
}
