package de.unileipzig.irpact.start.irpact.modes;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.start.irpact.IRPactExecutor;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public final class RunFully implements IRPactExecutor {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RunFully.class);

    public static final int ID = 0;
    public static final String ID_STR = "0";
    public static final RunFully INSTANCE = new RunFully();
    
    public RunFully() {
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void execute(IRPact irpact) throws Exception {
        LOGGER.info("execute IRPact fully");

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

        //Phase 2: simulation
        irpact.createPlatform();
        irpact.preparePlatform();
        irpact.setupTimeModel();
        irpact.createJadexAgents();
        
        if(!irpact.secureWaitForCreation()) {
            return;
        }

        //Phase 3: evaluation
        irpact.setupPreSimulationStart();
        irpact.startSimulation();
        irpact.waitForTermination();
        irpact.postSimulation();
    }
}
