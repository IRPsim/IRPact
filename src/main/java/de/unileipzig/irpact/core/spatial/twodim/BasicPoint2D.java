package de.unileipzig.irpact.core.spatial.twodim;

import de.unileipzig.irpact.commons.IsEquals;
import de.unileipzig.irpact.commons.attribute.AttributeAccess;
import de.unileipzig.irpact.commons.attribute.BasicAttributeAccess;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicPoint2D implements Point2D {

    protected final AttributeAccess ACCESS;
    protected Map<String, SpatialAttribute<?>> attributes;
    protected double x;
    protected double y;

    public BasicPoint2D() {
        this(0, 0);
    }

    public BasicPoint2D(double x, double y) {
        this(new LinkedHashMap<>(), x, y);
    }

    public BasicPoint2D(Map<String, SpatialAttribute<?>> attributes, double x, double y) {
        this.attributes = attributes;
        this.x = x;
        this.y = y;
        ACCESS = new BasicAttributeAccess(attributes);
    }

    @Override
    public AttributeAccess getAttributeAccess() {
        return ACCESS;
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

    public void addAllAttributes(Collection<? extends SpatialAttribute<?>> attributes) {
        for(SpatialAttribute<?> attribute: attributes) {
            addAttribute(attribute);
        }
    }

    public void addAllAttributes(SpatialAttribute<?>... attributes) {
        for(SpatialAttribute<?> attribute: attributes) {
            addAttribute(attribute);
        }
    }

    @Override
    public void addAttribute(SpatialAttribute<?> attribute) {
        if(attributes.containsKey(attribute.getName())) {
            throw new IllegalArgumentException("name '" + attribute.getName() + "' already exists");
        }
        attributes.put(attribute.getName(), attribute);
    }

    @Override
    public Collection<SpatialAttribute<?>> getAttributes() {
        return attributes.values();
    }

    @Override
    public SpatialAttribute<?> getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    @Override
    public BasicPoint2D emptyCopy() {
        return new BasicPoint2D(getX(), getY());
    }

    @Override
    public BasicPoint2D fullCopy() {
        BasicPoint2D copy = emptyCopy();
        for(SpatialAttribute<?> attr: getAttributes()) {
            copy.addAttribute(attr.copyAttribute());
        }
        return copy;
    }

    @Override
    public String toString() {
        return "Point2D[" + x + "," + y + "]";
    }

    @Override
    public int getHashCode() {
        return Objects.hash(x, y, IsEquals.getCollHashCode(attributes.values()));
    }
}
