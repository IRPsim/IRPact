package de.unileipzig.irpact.experimental;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.simulation.BasicBinaryTaskManager;
import de.unileipzig.irpact.core.simulation.tasks.PredefinedPostAgentCreationTask;
import de.unileipzig.irpact.core.simulation.tasks.PredefinedPreAgentCreationTask;
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

        PredefinedPreAgentCreationTask task1 = new PredefinedPreAgentCreationTask();
        task1.setInfo("XXX");
        task1.setTask(PredefinedPreAgentCreationTask.HELLO_WORLD);
        VisibleBinaryData vdb1 = new VisibleBinaryData();
        vdb1.setID(task1.getID());
        vdb1.setBytes(task1.getBytes());
        System.out.println(vdb1.getName());
        System.out.println(Util.printJson(task1.getRoot()));

        PredefinedPostAgentCreationTask task2 = new PredefinedPostAgentCreationTask();
        task2.setInfo("YYY");
        task2.setTask(PredefinedPostAgentCreationTask.HELLO_WORLD);
        VisibleBinaryData vbd2 = new VisibleBinaryData();
        vbd2.setID(task2.getID());
        vbd2.setBytes(task2.getBytes());
        System.out.println(vbd2.getName());
        System.out.println(Util.printJson(task2.getRoot()));

        BasicBinaryTaskManager bbbm = new BasicBinaryTaskManager();
        bbbm.handle(CollectionUtil.arrayListOf(vdb1.asBinary(), vbd2.asBinary()));
        bbbm.runAllInitializationStageTasks(null);
    }
}
