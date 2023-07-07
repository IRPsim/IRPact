package de.unileipzig.irpact.core.process2;

import de.unileipzig.irpact.core.process.PostAction;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.develop.Todo;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface ProcessPlan2 extends ProcessPlan {

    //legacy
    @Override
    default boolean isModel(ProcessModel model) {
        throw new UnsupportedOperationException("legacy");
    }

    //legacy
    @Override
    default ProcessPlanResult execute() throws Throwable {
        return execute2(null).toLegacy();
    }

    //legacy
    @SuppressWarnings("unchecked")
    @Override
    default ProcessPlanResult execute(List<PostAction> postActions) throws Throwable {
        //dirty
        return execute2((List<PostAction2>) (Object) postActions).toLegacy();
    }

    //legacy
    @Todo("loeschen und mittels ProcessPlanResult2 ersetzen")
    @Override
    default boolean keepRunningAfterAdoption() {
        return true;
    }

    ProcessPlanResult2 execute2(List<PostAction2> postActions) throws Throwable;

    void cleanUp();
}
