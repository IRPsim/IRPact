package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface SpatialInformationLoader {

    List<List<SpatialAttribute<?>>> getAllAttributes();
}
