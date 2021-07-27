package de.unileipzig.irpact.core.process.modular.ca;

import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.modular.EvaluationResult;

/**
 * @author Daniel Abitz
 */
public enum AdoptionResult implements EvaluationResult {
    ADOPTED,
    IMPEDED,
    IN_PROCESS;

    public ProcessPlanResult toPlanResult() {
        switch (this) {
            case ADOPTED:
                return ProcessPlanResult.ADOPTED;
            case IMPEDED:
                return ProcessPlanResult.IMPEDED;
            case IN_PROCESS:
                return ProcessPlanResult.IN_PROCESS;

            default:
                throw new IllegalStateException("unsupported result: " + this);
        }
    }
}
