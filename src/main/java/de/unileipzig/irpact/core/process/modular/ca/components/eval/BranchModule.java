package de.unileipzig.irpact.core.process.modular.ca.components.eval;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModuleWithNGenericSubModules;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BranchModule
        extends AbstractConsumerAgentModuleWithNGenericSubModules<ConsumerAgentEvaluationModule>
        implements ConsumerAgentEvaluationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BranchModule.class);

    protected static final int NUMBER_OF_MODULES = 4;
    protected static final int INDEX_INPUT_MODULE = 0;
    protected static final int INDEX_ON_ADOPT_MODULE = 1;
    protected static final int INDEX_ON_IMPEDED_MODULE = 2;
    protected static final int INDEX_ON_IN_PROCESS_MODULE = 3;

    public BranchModule() {
        super(NUMBER_OF_MODULES);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getName(),
                getInputModule(),
                getOnAdoptModule(),
                getOnImpededModule(),
                getOnInProcessModule()
        );
    }

    public void setInputModule(ConsumerAgentEvaluationModule inputModule) {
        setSubModule(INDEX_INPUT_MODULE, inputModule);
    }
    public ConsumerAgentEvaluationModule getInputModule() {
        return getSubModuleAs(INDEX_INPUT_MODULE);
    }

    public void setOnAdoptModule(ConsumerAgentEvaluationModule inputModule) {
        setSubModule(INDEX_ON_ADOPT_MODULE, inputModule);
    }
    public ConsumerAgentEvaluationModule getOnAdoptModule() {
        return getSubModuleAs(INDEX_ON_ADOPT_MODULE);
    }

    public void setOnImpededModule(ConsumerAgentEvaluationModule inputModule) {
        setSubModule(INDEX_ON_IMPEDED_MODULE, inputModule);
    }
    public ConsumerAgentEvaluationModule getOnImpededModule() {
        return getSubModuleAs(INDEX_ON_IMPEDED_MODULE);
    }

    public void setOnInProcessModule(ConsumerAgentEvaluationModule inputModule) {
        setSubModule(INDEX_ON_IN_PROCESS_MODULE, inputModule);
    }
    public ConsumerAgentEvaluationModule getOnInProcessModule() {
        return getSubModuleAs(INDEX_ON_IN_PROCESS_MODULE);
    }

    @Override
    public AdoptionResult evaluate(ConsumerAgentData data) throws Throwable {
        AdoptionResult result = getInputModule().evaluate(data);

        switch (result) {
            case ADOPTED:
                return getOnAdoptModule().evaluate(data);

            case IMPEDED:
                return getOnImpededModule().evaluate(data);

            case IN_PROCESS:
                return getOnInProcessModule().evaluate(data);

            default:
                throw new IllegalStateException("unsupported result: " + result);
        }
    }
}
