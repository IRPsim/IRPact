package de.unileipzig.irpact.core.spatial.dim2;

import de.unileipzig.irpact.core.spatial.Metric;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class Point2D implements SpatialInformation {

    private final double x;
    private final double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    private static double manhattenDistance(Point2D p0, Point2D p1) {
        double mx = Math.abs(p0.x - p1.x);
        double my = Math.abs(p0.y - p1.y);
        return mx + my;
    }

    private static double euclideanDistance(Point2D p0, Point2D p1) {
        double x2 = (p0.x - p1.x) * (p0.x - p1.x);
        double y2 = (p0.y - p1.y) * (p0.y - p1.y);
        return Math.sqrt(x2 + y2);
    }

    private static double maximumDistance(Point2D p0, Point2D p1) {
        double mx = Math.abs(p0.x - p1.x);
        double my = Math.abs(p0.y - p1.y);
        return Math.max(mx, my);
    }

    public static double distance(Metric metric, Point2D p0, Point2D p1) {
        switch (metric) {
            case MANHATTEN:
                return manhattenDistance(p0, p1);
            case EUCLIDEAN:
                return euclideanDistance(p0, p1);
            case MAXIMUM:
                return maximumDistance(p0, p1);
            default:
                throw new IllegalArgumentException("Unsupported Metric: " + metric);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj instanceof Point2D) {
            Point2D other = (Point2D) obj;
            return x == other.x
                    && y == other.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point2D[" + x + "," + y + "]";
    }
}
