package de.unileipzig.irpact.core.process;

import de.unileipzig.irpact.commons.IsEquals;

/**
 * @author Daniel Abitz
 */
public interface ProcessPlan extends IsEquals {

    ProcessPlanResult execute();
}
