package de.unileipzig.irpact.core.network.filter;

import de.unileipzig.irpact.core.agent.SpatialAgent;
import de.unileipzig.irpact.core.misc.Scheme;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.process.ProcessPlan;

/**
 * @author Daniel Abitz
 */
public interface NodeFilterScheme extends Scheme {

    NodeFilter createFilter(SpatialAgent agent);
}
