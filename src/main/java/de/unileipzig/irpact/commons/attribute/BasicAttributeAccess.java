package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.ChecksumComparable;

import java.util.Collection;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicAttributeAccess implements AttributeAccess {

    protected Map<? extends String, ? extends Attribute<?>> attributes;

    public BasicAttributeAccess(Map<? extends String, ? extends Attribute<?>> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    @Override
    public Attribute<?> getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Collection<? extends Attribute<?>> getAttributes() {
        return attributes.values();
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getCollChecksum(getAttributes());
    }
}
