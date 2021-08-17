package de.unileipzig.irpact.core.process.modular.ca.updater;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.process.modular.ca.Stage;
import de.unileipzig.irpact.core.process.ra.RAStage;
import de.unileipzig.irpact.core.simulation.SimulationEntityBase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class EndOfYearEvaluator extends SimulationEntityBase implements ConsumerAgentDataUpdater, LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(EndOfYearEvaluator.class);

    protected ConsumerAgentEvaluationModule module;

    public EndOfYearEvaluator() {
    }

    public EndOfYearEvaluator(String name) {
        setName(name);
    }

    public void setModule(ConsumerAgentEvaluationModule module) {
        this.module = module;
    }

    public ConsumerAgentEvaluationModule getModule() {
        return module;
    }

    protected ConsumerAgentEvaluationModule getValidModule() {
        ConsumerAgentEvaluationModule module = getModule();
        if(module == null) {
            throw new NullPointerException("ConsumerAgentEvaluationModule");
        }
        return module;
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
    public boolean update(ConsumerAgentData data) throws Throwable {
        if(data.currentStage() == Stage.IMPEDED) {
            trace("reset process stage '{}' to '{}' for agent '{}'", RAStage.IMPEDED, RAStage.DECISION_MAKING, data.getAgent().getName());
            data.updateStage(Stage.DECISION_MAKING);
            getValidModule().evaluate(data);
            return true;
        } else {
            return false;
        }
    }
}
