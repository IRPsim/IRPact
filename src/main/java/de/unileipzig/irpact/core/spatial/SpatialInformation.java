package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.attribute.AttributeAccess;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface SpatialInformation {

    AttributeAccess getAttributeAccess();

    Collection<SpatialAttribute<?>> getAttributes();

    SpatialAttribute<?> getAttribute(String name);

    boolean hasAttribute(String name);

    void addAttribute(SpatialAttribute<?> attribute);

    SpatialInformation emptyCopy();

    SpatialInformation fullCopy();
}
