package de.unileipzig.irpact.core.process.modular.ca.components.eval;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractActionModule;
import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.Stage;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.process.PostAction;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class DefaultInterestModule extends AbstractActionModule implements ConsumerAgentEvaluationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DefaultInterestModule.class);

    public DefaultInterestModule() {
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
        trace("[{}] handle interest", data.getAgent().getName());

        if(isInterested(data.getAgent(), data.getProduct())) {
            doSelfActionAndAllowAttention(data.getAgent());
            data.updateStage(Stage.FEASIBILITY);
            return AdoptionResult.IN_PROCESS;
        }

        if(isAware(data.getAgent(), data.getProduct())) {
            if(data.isUnderConstruction() || data.isUnderRenovation()) {
                makeInterested(data.getAgent(), data.getProduct());
                doSelfActionAndAllowAttention(data.getAgent());
                return AdoptionResult.IN_PROCESS;
            }
        }

        return doAction(data, postActions);
    }
}
