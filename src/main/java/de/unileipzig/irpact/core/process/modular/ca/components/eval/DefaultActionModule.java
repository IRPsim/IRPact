package de.unileipzig.irpact.core.process.modular.ca.components.eval;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractActionModule;
import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irptools.util.log.IRPLogger;

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
    public int getChecksum() {
        return getPartialChecksum();
    }

    @Override
    public AdoptionResult evaluate(ConsumerAgentData data) throws Throwable {
        return doAction(data);
    }
}