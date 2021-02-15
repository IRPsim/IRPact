package de.unileipzig.irpact.core.spatial.twodim;

import de.unileipzig.irpact.core.spatial.SpatialAttribute;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicPoint2D implements Point2D {

    protected Map<String, SpatialAttribute> attributes;
    protected double x;
    protected double y;

    public BasicPoint2D() {
        this(0, 0);
    }

    public BasicPoint2D(double x, double y) {
        this(new HashMap<>(), x, y);
    }

    public BasicPoint2D(Map<String, SpatialAttribute> attributes, double x, double y) {
        this.attributes = attributes;
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
    public void addAttribute(SpatialAttribute attribute) {
        if(attributes.containsKey(attribute.getName())) {
            throw new IllegalArgumentException("name '" + attribute.getName() + "' already exists");
        }
        attributes.put(attribute.getName(), attribute);
    }

    @Override
    public Collection<SpatialAttribute> getAttributes() {
        return attributes.values();
    }

    @Override
    public SpatialAttribute getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
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
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point2D[" + x + "," + y + "]";
    }
}
