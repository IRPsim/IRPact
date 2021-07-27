package de.unileipzig.irpact.core.process.modular.ca.updater;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.Stage;
import de.unileipzig.irpact.core.process.ra.RAStage;
import de.unileipzig.irpact.core.simulation.SimulationEntityBase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class StartOfYearAdjuster extends SimulationEntityBase implements ConsumerAgentDataUpdater, LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(StartOfYearAdjuster.class);

    public StartOfYearAdjuster() {
    }

    public StartOfYearAdjuster(String name) {
        setName(name);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.SIMULATION_PROCESS;
    }

    @Override
    public boolean update(ConsumerAgentData data) {
        if(data.currentStage() == Stage.IMPEDED) {
            trace("reset process stage '{}' to '{}' for agent '{}'", RAStage.IMPEDED, RAStage.DECISION_MAKING, data.getAgent().getName());
            data.updateStage(Stage.DECISION_MAKING);
            return true;
        } else {
            return false;
        }
    }
}
