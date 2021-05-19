package de.unileipzig.irpact.core.spatial.filter;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.function.Predicate;

/**
 * @author Daniel Abitz
 */
public interface SpatialInformationFilter extends Nameable, Predicate<SpatialInformation> {
}
