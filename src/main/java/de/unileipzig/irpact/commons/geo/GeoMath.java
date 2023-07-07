package de.unileipzig.irpact.commons.geo;

import de.unileipzig.irpact.commons.util.MathUtil;
import de.unileipzig.irpact.commons.util.Rnd;

import java.util.function.BiFunction;

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

    //https://jordinl.com/posts/2019-02-15-how-to-generate-random-geocoordinates-within-given-radius
    public static double[] randomCircularLongitudeLatitude(
            double lon, double lat,
            double maxd, double mind,
            double radius,
            Rnd rnd) {
        return randomCircularLongitudeLatitude(
                lon, lat,
                maxd, mind,
                radius,
                rnd,
                (rndLon, rndLat) -> {
                    double[] p = new double[2];
                    p[0] = rndLon;
                    p[1] = rndLat;
                    return p;
                }
        );
    }

    public static void randomCircularLongitudeLatitude(
            double lon, double lat,
            double maxd, double mind,
            double radius,
            Rnd rnd,
            double[] p) {
        randomCircularLongitudeLatitude(
                lon, lat,
                maxd, mind,
                radius,
                rnd,
                (rndLon, rndLat) -> {
                    p[0] = rndLon;
                    p[1] = rndLat;
                    return null;
                }
        );
    }

    public static <R> R randomCircularLongitudeLatitude(
            double lon, double lat,
            double maxd, double mind,
            double radius,
            Rnd rnd,
            BiFunction<? super Double, ? super Double, ? extends R> func) {
        double radlon = Math.toRadians(lon);
        double radlat = Math.toRadians(lat);

        double maxd2 = maxd * maxd;
        double mind2 = mind * mind;
        double d = Math.sqrt(rnd.nextDouble() * (maxd2 - mind2) + mind2);
        double dradius = d / radius;
        double deltaLat = Math.cos(rnd.nextDouble() * Math.PI) * dradius;

        //lon
        double sign = MathUtil.rndSign(rnd);
        double tempLon = Math.acos(
                ((Math.cos(dradius) - Math.cos(deltaLat)) /
                (Math.cos(radlat) * Math.cos(deltaLat + radlat))) +
                1
        );
        double deltaLon = sign * tempLon;
        double rndLon = Math.toDegrees(radlon + deltaLon);

        //lat
        double rndLat = Math.toDegrees(radlat + deltaLat);

        return func.apply(rndLon, rndLat);
    }

    public static double haversineDistance(
            double longitude1, double latitude1,
            double longitude2, double latitude2,
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
