package de.unileipzig.irpact.commons.attribute.v2.access;

import de.unileipzig.irpact.commons.attribute.v2.simple.Attribute2;
import de.unileipzig.irpact.commons.checksum.ChecksumComparable;

/**
 * Allows read access to attributes of another entity.
 *
 * @author Daniel Abitz
 */
public interface AttributeAccess2 extends ChecksumComparable {

    Object getOwner();

    boolean hasAttribute(String name);

    Attribute2 getAttribute(String name);
}
