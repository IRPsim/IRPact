package de.unileipzig.irpact.start.irpact.executors;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.start.irpact.IRPactExecutor;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public final class RunMinimalSimulation implements IRPactExecutor {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RunMinimalSimulation.class);

    public static final int ID = 1;
    public static final String ID_STR = "1";
    public static final RunMinimalSimulation INSTANCE = new RunMinimalSimulation();

    public RunMinimalSimulation() {
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void execute(IRPact irpact) throws Exception {
        LOGGER.info(IRPSection.GENERAL, "execute IRPact with minimal simulation (only system agents)");

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

        irpact.createPlatform();
        irpact.preparePlatform();
        irpact.setupTimeModel();
        irpact.createOnlyControlJadexAgents();

        if(irpact.secureWaitForCreationFailed()) {
            return;
        }

        irpact.setupPreSimulationStart();
        irpact.startSimulation();
        irpact.waitForTermination();

        //Phase 3: evaluation
        irpact.notifyEnd();
        irpact.postSimulation();
    }
}
