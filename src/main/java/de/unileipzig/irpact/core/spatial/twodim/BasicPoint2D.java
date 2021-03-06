package de.unileipzig.irpact.core.spatial.twodim;

import de.unileipzig.irpact.commons.attribute.AttributeAccess;
import de.unileipzig.irpact.commons.attribute.BasicAttributeAccess;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.data.MutableLong;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicPoint2D implements Point2D {

    protected final AttributeAccess ACCESS;
    protected final MutableLong id = MutableLong.empty();
    protected Map<String, SpatialAttribute> attributes;
    protected double x;
    protected double y;

    public BasicPoint2D() {
        this(0, 0);
    }

    public BasicPoint2D(double xy) {
        this(xy, xy);
    }

    public BasicPoint2D(double x, double y) {
        this(new LinkedHashMap<>(), x, y);
    }

    public BasicPoint2D(long id, double x, double y) {
        this(new LinkedHashMap<>(), x, y);
        setId(id);
    }

    public BasicPoint2D(Map<String, SpatialAttribute> attributes, double x, double y) {
        this.attributes = attributes;
        this.x = x;
        this.y = y;
        ACCESS = new BasicAttributeAccess(this, attributes);
    }

    public BasicPoint2D reverse() {
        return new BasicPoint2D(getY(), getX());
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public void reverseThis() {
        double temp = x;
        x = y;
        y = temp;
    }

    @Override
    public boolean hasId() {
        return id.hasValue();
    }

    @Override
    public boolean isId(long id) {
        return getId() == id;
    }

    @Override
    public long getId() {
        if(hasId()) {
            return id.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void setId(long id) throws IllegalStateException {
        if(hasId()) {
            throw new IllegalStateException();
        }
        this.id.set(id);
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

    public void addAllAttributes(Collection<? extends SpatialAttribute> attributes) {
        for(SpatialAttribute attribute: attributes) {
            addAttribute(attribute);
        }
    }

    public void addAllAttributes(SpatialAttribute... attributes) {
        for(SpatialAttribute attribute: attributes) {
            addAttribute(attribute);
        }
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
    public BasicPoint2D emptyCopy() {
        return new BasicPoint2D(getX(), getY());
    }

    @Override
    public BasicPoint2D fullCopy() {
        BasicPoint2D copy = emptyCopy();
        for(SpatialAttribute attr: getAttributes()) {
            copy.addAttribute(attr.copy());
        }
        return copy;
    }

    @Override
    public String toString() {
        return hasId()
                ? "Point2D[" + getId() + "," + x + "," + y + "]"
                : "Point2D[" + x + "," + y + "]";
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(x, y, attributes.values());
    }

    @Override
    public boolean isEquals(SpatialInformation other) {
        if (this == other) return true;
        if (!(other instanceof BasicPoint2D)) return false;
        BasicPoint2D that = (BasicPoint2D) other;
        return Double.compare(that.x, x) == 0
                && Double.compare(that.y, y) == 0
                && Objects.equals(id, that.id)
                && Objects.equals(attributes, that.attributes);
    }
}
