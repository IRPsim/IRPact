package de.unileipzig.irpact.v2.core.spatial.twodim;

import de.unileipzig.irpact.v2.commons.Util;

/**
 * @author Daniel Abitz
 */
public class BasicPoint2D implements Point2D {

    protected double x;
    protected double y;

    public BasicPoint2D() {
    }

    public BasicPoint2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj instanceof BasicPoint2D) {
            BasicPoint2D other = (BasicPoint2D) obj;
            return x == other.x
                    && y == other.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Util.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point2D[" + x + "," + y + "]";
    }
}
