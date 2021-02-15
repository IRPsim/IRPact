package de.unileipzig.irpact.experimental;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.simulation.BasicBinaryDataManager;
import de.unileipzig.irpact.core.simulation.tasks.BasicNonSimulationTask;
import de.unileipzig.irpact.io.inout.binary.HiddenBinaryData;
import de.unileipzig.irpact.io.input.binary.VisibleBinaryData;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Daniel Abitz
 */
@Disabled
public class FunWithBinary {

    @Test
    void lala() throws Exception {
        IRPLogging.initConsole();

        BasicNonSimulationTask task1 = new BasicNonSimulationTask();
        task1.setInfo("XXX");
        task1.setTaskNumber(BasicNonSimulationTask.HELLO_WORLD);
        VisibleBinaryData hdb1 = task1.toBinary(VisibleBinaryData.class);
        System.out.println(hdb1._name);

        BasicNonSimulationTask task2 = new BasicNonSimulationTask();
        task2.setInfo("YYY");
        task2.setTaskNumber(BasicNonSimulationTask.HELLO_WORLD);
        HiddenBinaryData hdb2 = task2.toBinary(HiddenBinaryData.class);
        System.out.println(hdb2._name);

        BasicBinaryDataManager bbbm = new BasicBinaryDataManager();
        bbbm.handle(CollectionUtil.arrayListOf(hdb1.asBinary(), hdb2.asBinary()));
    }
}
