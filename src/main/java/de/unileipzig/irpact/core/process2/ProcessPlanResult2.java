package de.unileipzig.irpact.core.process2;

import de.unileipzig.irpact.core.process.ProcessPlanResult;

/**
 * @author Daniel Abitz
 */
public enum ProcessPlanResult2 {
    CONTINUE,
    FINISHED;

    public ProcessPlanResult toLegacy() {
        switch (this) {
            case CONTINUE:
                return ProcessPlanResult.IN_PROCESS;

            case FINISHED:
                return ProcessPlanResult.ADOPTED;

            default:
                throw new IllegalStateException("unsupported: " + this);
        }
    }
}
