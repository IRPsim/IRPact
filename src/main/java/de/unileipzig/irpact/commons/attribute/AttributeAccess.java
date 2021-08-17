package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;

import java.util.Collection;

/**
 * Allows read access to attributes of another entity.
 *
 * @author Daniel Abitz
 */
public interface AttributeAccess extends ChecksumComparable {

    Object getOwner();

    boolean hasAttribute(String name);

    Attribute getAttribute(String name);

    Collection<? extends Attribute> getAttributes();
}
