package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.ChecksumComparable;

import java.util.Collection;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicAttributeAccess implements AttributeAccess {

    protected Map<? extends String, ? extends ValueAttribute> attributes;

    public BasicAttributeAccess(Map<? extends String, ? extends ValueAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    @Override
    public ValueAttribute getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Collection<? extends ValueAttribute> getAttributes() {
        return attributes.values();
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getCollChecksum(getAttributes());
    }
}
