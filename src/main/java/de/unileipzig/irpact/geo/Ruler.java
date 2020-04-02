package de.unileipzig.irpact.geo;

/**
 * @author Daniel Abitz
 */
//https://blog.mapbox.com/fast-geodesic-approximations-with-cheap-ruler-106f229ad016?gi=406872b0c745
//https://github.com/mapbox/cheap-ruler/blob/master/index.js
//kA ob wir das gebrauchen koennten
public class Ruler {

    public enum Unit {
        KILOMETER(1.0),
        MILE(1000.0 / 1609.344),
        METER(1000.0);

        private final double factor;

        Unit(double factor) {
            this.factor = factor;
        }

        public double getFactor() {
            return factor;
        }
    }

    private Unit unit;
    private double lat;
    private double cos;
    private double cos2;
    private double cos3;
    private double cos4;
    private double cos5;
    private double x;
    private double y;

    public Ruler(double lat, Unit unit) {
        this.unit = unit;
        this.lat = lat;
        cos = Math.cos(Math.toRadians(lat));
        cos2 = 2 * cos * cos - 1;
        cos3 = 2 * cos * cos2 - cos;
        cos4 = 2 * cos * cos3 - cos2;
        cos5 = 2 * cos * cos4 - cos3;

        double m = unit.getFactor();
        x = m * (111.41513 * cos - 0.09455 * cos3 + 0.00012 * cos5);
        y = m * (111.13209 - 0.56605 * cos2 + 0.0012 * cos4);
    }

    public double toX(double longitude) {
        return longitude * x;
    }

    public double toY(double latitude) {
        return latitude * y;
    }

    public double distance2(
            double latitude1, double longitude1,
            double latitude2, double longitude2) {
        double dx = (longitude2 - longitude1) * x;
        double dy = (latitude2 - latitude1) * y;
        return dx * dx + dy * dy;
    }

    public double distance(
            double latitude1, double longitude1,
            double latitude2, double longitude2) {
        double dist2 = distance2(latitude1, longitude1, latitude2, longitude2);
        return Math.sqrt(dist2);
    }
}
