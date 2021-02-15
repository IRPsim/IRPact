package de.unileipzig.irpact.core.spatial;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface SpatialInformation {

    Collection<SpatialAttribute> getAttributes();

    SpatialAttribute getAttribute(String name);

    boolean hasAttribute(String name);

    void addAttribute(SpatialAttribute attribute);
}
