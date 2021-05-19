package de.unileipzig.irpact.util.gis;

import org.locationtech.jts.geom.Coordinate;

/**
 * @author Daniel Abitz
 */
public class Point2D {

    protected double x;
    protected double y;

    public Point2D() {
    }

    public Point2D(double xy) {
        this(xy, xy);
    }

    public Point2D(double x, double y) {
        setX(x);
        setY(y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Point3D to3D(double z) {
        return new Point3D(getX(), getY(), z);
    }

    public boolean isNotEquals(Point2D other) {
        return !isEquals(other);
    }

    public boolean isEquals(Point2D other) {
        return getX() == other.getX()
                && getY() == other.getY();
    }

    public Coordinate toCoordinate() {
        return new Coordinate(getX(), getY());
    }

    @Override
    public String toString() {
        return "Point2D{"
                + x +
                ", " + y +
                '}';
    }
}
