package de.unileipzig.irpact.util.scenarios.pvact.toymodels.util;

import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface DataModifier {

    DataModifier DO_NOTHING = row -> row;

    List<SpatialAttribute> modify(List<SpatialAttribute> row);
}
