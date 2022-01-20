package de.unileipzig.irpact.commons.attribute.v2.access;

import de.unileipzig.irpact.commons.attribute.v2.simple.Attribute2;
import de.unileipzig.irpact.commons.checksum.Checksums;

import java.util.Collection;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicAttributeAccess2 implements AttributeAccess2 {

    protected Object owner;
    protected Map<? extends String, ? extends Attribute2> attributes;

    public BasicAttributeAccess2(Object owner, Map<? extends String, ? extends Attribute2> attributes) {
        this.attributes = attributes;
        this.owner = owner;
    }

    @Override
    public Object getOwner() {
        return owner;
    }

    @Override
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    @Override
    public Attribute2 getAttribute(String name) {
        return attributes.get(name);
    }

    public Collection<? extends Attribute2> getAttributes() {
        return attributes.values();
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(attributes);
    }
}
