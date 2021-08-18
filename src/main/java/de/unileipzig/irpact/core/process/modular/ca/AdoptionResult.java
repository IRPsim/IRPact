package de.unileipzig.irpact.core.process.modular.ca;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.modular.EvaluationResult;

/**
 * @author Daniel Abitz
 */
public enum AdoptionResult implements EvaluationResult, ChecksumComparable {
    ADOPTED(0),
    IMPEDED(1),
    IN_PROCESS(2);

    private final int ID;

    AdoptionResult(int id) {
        ID = id;
    }

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

    @Override
    public int getChecksum() {
        return ID;
    }
}
