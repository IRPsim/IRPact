package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.Checksums;

import java.util.Collection;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicAttributeAccess implements AttributeAccess {

    protected Object owner;
    protected Map<? extends String, ? extends Attribute> attributes;

    public BasicAttributeAccess(Object owner, Map<? extends String, ? extends Attribute> attributes) {
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
    public Attribute getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Collection<? extends Attribute> getAttributes() {
        return attributes.values();
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(attributes);
    }
}
