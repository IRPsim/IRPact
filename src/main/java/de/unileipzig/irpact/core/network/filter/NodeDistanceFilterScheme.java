package de.unileipzig.irpact.core.network.filter;

import de.unileipzig.irpact.core.agent.SpatialAgent;
import de.unileipzig.irpact.core.misc.Scheme;

/**
 * @author Daniel Abitz
 */
public interface NodeDistanceFilterScheme extends NodeFilterScheme {

    NodeDistanceFilter createFilter(SpatialAgent agent);
}
