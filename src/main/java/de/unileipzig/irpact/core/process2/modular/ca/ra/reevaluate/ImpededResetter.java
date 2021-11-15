package de.unileipzig.irpact.core.process2.modular.ca.ra.reevaluate;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.ra.RAStage;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAStage2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.reevaluate.AbstractReevaluator;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ImpededResetter
        extends AbstractReevaluator<ConsumerAgentData2>
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ImpededResetter.class);

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void initializeReevaluator(SimulationEnvironment environment) {
    }

    @Override
    public boolean reevaluateGlobal() {
        return false;
    }

    @Override
    public void reevaluate() {
    }

    @Override
    public boolean reevaluateIndividual() {
        return true;
    }

    @Override
    public void reevaluate(ConsumerAgentData2 input, List<PostAction2> actions) {
        if(input.getStage() == RAStage2.IMPEDED) {
            trace("[{}] reset process stage '{}' to '{}' for agent '{}'", getName(), RAStage.IMPEDED, RAStage.DECISION_MAKING, input.getAgentName());
            input.setStage(RAStage2.DECISION_MAKING);
        }
    }
}
