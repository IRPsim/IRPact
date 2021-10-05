package de.unileipzig.irpact.core.process2;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface ProcessPlan2 {

    default ProcessPlanResult2 execute() {
        return execute(null);
    }

    ProcessPlanResult2 execute(List<PostAction2> postActions);
}
