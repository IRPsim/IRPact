package de.unileipzig.irpact.core.process.modular.ca.components.eval;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractActionModule;
import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.Stage;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class DefaultFeasibilityModule extends AbstractActionModule implements ConsumerAgentEvaluationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DefaultFeasibilityModule.class);

    public DefaultFeasibilityModule() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getChecksum() {
        return getPartialChecksum();
    }

    @Override
    public AdoptionResult evaluate(ConsumerAgentData data) throws Throwable {
        boolean isShare = isShareOf1Or2FamilyHouse(data.getAgent());
        boolean isOwner = isHouseOwner(data.getAgent());

        if(isShare && isOwner) {
            doSelfActionAndAllowAttention(data.getAgent());
            data.updateStage(Stage.DECISION_MAKING);
            return AdoptionResult.IN_PROCESS;
        }

        return doAction(data);
    }
}
