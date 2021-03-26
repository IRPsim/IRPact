package de.unileipzig.irpact.core.process;

import de.unileipzig.irpact.commons.ChecksumComparable;

/**
 * @author Daniel Abitz
 */
public interface ProcessPlan extends ChecksumComparable {

    ProcessPlanResult execute();
}
