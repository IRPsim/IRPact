package de.unileipzig.irpact.experimental;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.simulation.BasicBinaryTaskManager;
import de.unileipzig.irpact.core.simulation.tasks.PredefinedAppTask;
import de.unileipzig.irpact.core.simulation.tasks.PredefinedSimulationTask;
import de.unileipzig.irpact.io.param.inout.binary.HiddenBinaryData;
import de.unileipzig.irpact.io.param.input.binary.VisibleBinaryData;
import de.unileipzig.irptools.util.Util;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Daniel Abitz
 */
@Disabled
public class FunWithBinary {

    @Test
    void lala() {
        IRPLogging.initConsole();

        PredefinedAppTask task1 = new PredefinedAppTask();
        task1.setInfo("XXX");
        task1.setTask(PredefinedAppTask.HELLO_WORLD);
        VisibleBinaryData vdb1 = new VisibleBinaryData();
        vdb1.setID(task1.getID());
        vdb1.setBytes(task1.getBytes());
        System.out.println(vdb1.getName());
        System.out.println(Util.printJson(task1.getRoot()));

        PredefinedSimulationTask task2 = new PredefinedSimulationTask();
        task2.setInfo("YYY");
        task2.setTask(PredefinedSimulationTask.HELLO_WORLD);
        HiddenBinaryData hdb1 = new HiddenBinaryData();
        hdb1.setID(task2.getID());
        hdb1.setBytes(task2.getBytes());
        System.out.println(hdb1.getName());
        System.out.println(Util.printJson(task2.getRoot()));

        BasicBinaryTaskManager bbbm = new BasicBinaryTaskManager();
        bbbm.handle(CollectionUtil.arrayListOf(vdb1.asBinary(), hdb1.asBinary()));
        bbbm.runAppTasks();
        bbbm.runSimulationTasks(null);
    }
}
