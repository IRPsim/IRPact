package de.unileipzig.irpact.commons.geo;

import de.unileipzig.irpact.commons.util.Rnd;

/**
 * @author Daniel Abitz
 */
public final class GeoMath {

    public static final double EARTH_RADIUS_METER = 6371000;
    public static final double EARTH_RADIUS_KILOMETER = 6371;

    private GeoMath() {
    }

    public static double randomLatitude(Rnd rnd) {
        return rnd.nextDouble(-90, 90);
    }

    public static double randomLongitude(Rnd rnd) {
        return rnd.nextDouble(-180, 180);
    }

    public static double haversineDistance(
            double latitude1, double longitude1,
            double latitude2, double longitude2,
            double radius) {
        if(latitude1 == latitude2 && longitude1 == longitude2) {
            return 0.0;
        }
        double lat1Rad = Math.toRadians(latitude1);
        double lat2Rad = Math.toRadians(latitude2);
        double deltaLatRad = Math.toRadians(latitude2 - latitude1);
        double deltaLonRad = Math.toRadians(longitude2 - longitude1);
        double sinDeltaLatRad = Math.sin(deltaLatRad / 2.0);
        double sinDeltaLonRad = Math.sin(deltaLonRad  / 2.0);
        double a = sinDeltaLatRad * sinDeltaLatRad +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) * sinDeltaLonRad * sinDeltaLonRad;
        return 2.0 * radius * Math.asin(Math.sqrt(a));
    }

    public static double cosPhi(
            double topLeftLat,
            double bottomRightLat) {
//        double topLeftLatRad = Math.toRadians(topLeftLat);
//        double bottomRightLatRad = Math.toRadians(bottomRightLat);
//        return Math.cos((topLeftLatRad + bottomRightLatRad) / 2.0);
        return Math.cos((topLeftLat + bottomRightLat) / 2.0);
    }

    public static double latlng2globalX(
            double lng,
            double topLeftLat,
            double bottomRightLat,
            double radius) {
        return latlng2globalX(lng, radius, cosPhi(topLeftLat, bottomRightLat));
    }

    public static double latlng2globalX(
            double lng,
            double radius,
            double cosPhi) {
        return radius * lng * cosPhi;
    }

    public static double latlng2globalY(
            double lat,
            double radius) {
        return radius * lat;
    }

    public static double latlng2screenX(
            double globalX,
            double topLeftX,
            double topLeftGlobalX,
            double bottomRightX,
            double bottomRightGlobalX) {
        double perX = (globalX - topLeftGlobalX) / (bottomRightGlobalX - topLeftGlobalX);
        return topLeftX + (bottomRightX - topLeftX) * perX;
    }

    public static double latlng2screenY(
            double globalY,
            double topLeftY,
            double topLeftGlobalY,
            double bottomRightY,
            double bottomRightGlobalY) {
        double perY = (globalY - topLeftGlobalY) / (bottomRightGlobalY - topLeftGlobalY);
        return topLeftY + (bottomRightY - topLeftY) * perY;
    }
}
