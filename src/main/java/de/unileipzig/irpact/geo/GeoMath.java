package de.unileipzig.irpact.geo;

/**
 * @author Daniel Abitz
 */
public final class GeoMath {

    public static final double EARTH_RADIUS_METER = 6371000;
    public static final double EARTH_RADIUS_KILOMETER = 6371;

    private GeoMath() {
    }

    public static double haversineDistance(
            double latitude1, double longitude1,
            double latitude2, double longitude2,
            double radius) {
        double lat1Rad = Math.toRadians(latitude1);
        double lat2Rad = Math.toRadians(latitude2);
        double deltaLatRad = Math.toRadians(latitude2 - latitude1);
        double deltaLonRad = Math.toRadians(longitude2 - longitude1);
        double sinDeltaLatRad = Math.sin(deltaLatRad / 2.0);
        double sinDeltaLonRad = Math.sin(deltaLonRad  / 2.0);
        double a = sinDeltaLatRad * sinDeltaLatRad +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                sinDeltaLonRad * sinDeltaLonRad;
        double c = 2.0 * Math.asin(Math.sqrt(a));
        return radius * c;
    }
}