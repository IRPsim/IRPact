package de.unileipzig.irpact.core.process.modular.ca.components.eval;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractActionModule;
import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.process.PostAction;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class DefaultActionModule extends AbstractActionModule implements ConsumerAgentEvaluationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DefaultActionModule.class);

    public DefaultActionModule() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public IRPSection getDefaultResultSection() {
        return IRPSection.SIMULATION_PROCESS;
    }

    @Override
    public int getChecksum() {
        return getPartialChecksum();
    }

    @Override
    public AdoptionResult evaluate(ConsumerAgentData data, List<PostAction> postActions) throws Throwable {
        trace("[{}] handle default actions", data.getAgent().getName());
        return doAction(data, postActions);
    }
}
