package de.unileipzig.irpact.core.process;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;

/**
 * @author Daniel Abitz
 */
public interface ProcessPlan extends ChecksumComparable {

    boolean isModel(ProcessModel model);

    ProcessPlanResult execute() throws Throwable;
}
