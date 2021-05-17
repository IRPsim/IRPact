package de.unileipzig.irpact.start.irpact.modes;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.start.irpact.IRPactExecutor;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public final class RunWithoutSimulation implements IRPactExecutor {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RunFully.class);

    public static final int ID = 1;
    public static final String ID_STR = "1";
    public static final RunWithoutSimulation INSTANCE = new RunWithoutSimulation();

    public RunWithoutSimulation() {
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void execute(IRPact irpact) throws Exception {
        LOGGER.info("execute IRPact without simulation, only initialization and evaluation");

        //Phase 1: initialization
        irpact.initialize();

        irpact.preAgentCreation();
        irpact.runPreAgentCreationTasks();
        irpact.preAgentCreationValidation();

        irpact.createAgents();

        irpact.postAgentCreation();
        irpact.runPostAgentCreationTasks();
        irpact.postAgentCreationValidation();

        irpact.runPrePlatformCreationTasks();

        if(irpact.checkNoSimulationFlag()) {
            return;
        }

        //Phase 3: evaluation
        irpact.setupPreSimulationStart();
        irpact.startSimulation();
        irpact.waitForTermination();
        irpact.postSimulation();
    }
}
