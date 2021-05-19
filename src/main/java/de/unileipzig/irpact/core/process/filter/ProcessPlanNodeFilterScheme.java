package de.unileipzig.irpact.core.process.filter;

import de.unileipzig.irpact.core.misc.Scheme;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.process.ProcessPlan;

/**
 * @author Daniel Abitz
 */
public interface ProcessPlanNodeFilterScheme extends Scheme {

    NodeFilter createFilter(ProcessPlan plan);
}
