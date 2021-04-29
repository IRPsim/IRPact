package de.unileipzig.irpact.util.gis;

/**
 * @author Daniel Abitz
 */
public class Point3D {

    private double x;
    private double y;
    private double z;

    public Point3D() {
    }

    public Point3D(double xyz) {
        this(xyz, xyz, xyz);
    }

    public Point3D(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public Point2D to2D() {
        return new Point2D(getX(), getY());
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

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public boolean isNotEquals(Point3D other) {
        return !isEquals(other);
    }

    public boolean isEquals(Point3D other) {
        return getX() == other.getX()
                && getY() == other.getY()
                && getZ() == other.getZ();
    }

    @Override
    public String toString() {
        return "Point3D{"
                + x +
                ", " + y +
                ", " + z +
                '}';
    }
}
