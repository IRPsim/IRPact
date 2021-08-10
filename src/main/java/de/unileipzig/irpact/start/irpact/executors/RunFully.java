package de.unileipzig.irpact.start.irpact.executors;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
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
    public void execute(IRPact irpact) throws Throwable {
        if(irpact == null) {
            throw new NullPointerException("IRPact instance is null");
        }

        try {
            LOGGER.info(IRPSection.GENERAL, "execute IRPact fully");

            irpact.notifyStart();

            //Phase 1: initialization
            irpact.initialize();
            System.gc();

            irpact.preAgentCreation();
            irpact.runPreAgentCreationTasks();
            irpact.preAgentCreationValidation();

            irpact.createAgents();

            irpact.postAgentCreation();
            irpact.runPostAgentCreationTasks();
            irpact.postAgentCreationValidation();

            irpact.runPrePlatformCreationTasks();

            irpact.printInitialNetwork();
            if(irpact.checkNoSimulationFlag()) {
                return;
            }
            System.gc();

            irpact.createPlatform();
            irpact.preparePlatform();
            irpact.setupTimeModel();
            irpact.createJadexAgents();

            if(irpact.secureWaitForCreationFailed()) {
                return;
            }

            irpact.setupPreSimulationStart();
            irpact.startSimulation();
            irpact.waitForTermination();

            //Phase 3: evaluation
            irpact.notifyEnd();
            irpact.postSimulation();

        } catch (Throwable cause) {
            irpact.createStackTraceImageIfDesired(cause);
            irpact.postSimulationWithDummyOutputAndErrorMessageIfDesired(cause);
            throw cause;
        }
    }
}
