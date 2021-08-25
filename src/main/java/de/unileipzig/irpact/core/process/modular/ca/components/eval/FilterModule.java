package de.unileipzig.irpact.core.process.modular.ca.components.eval;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModuleWithNGenericSubModules;
import de.unileipzig.irpact.core.process.PostAction;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class FilterModule
        extends AbstractConsumerAgentModuleWithNGenericSubModules<ConsumerAgentEvaluationModule>
        implements ConsumerAgentEvaluationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FilterModule.class);

    protected static final int NUMBER_OF_MODULES = 5;
    protected static final int INDEX_INPUT_MODULE = 0;
    protected static final int INDEX_TASK_MODULE = 1;

    public FilterModule() {
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
                getTaskModule()
        );
    }

    public void setInputModule(ConsumerAgentEvaluationModule inputModule) {
        setSubModule(INDEX_INPUT_MODULE, inputModule);
    }
    public ConsumerAgentEvaluationModule getInputModule() {
        return getSubModuleAs(INDEX_INPUT_MODULE);
    }

    public void setTaskModule(ConsumerAgentEvaluationModule inputModule) {
        setSubModule(INDEX_TASK_MODULE, inputModule);
    }
    public ConsumerAgentEvaluationModule getTaskModule() {
        return getSubModuleAs(INDEX_TASK_MODULE);
    }

    @Override
    public AdoptionResult evaluate(ConsumerAgentData data, List<PostAction<?>> postActions) throws Throwable {
        AdoptionResult result = getInputModule().evaluate(data, postActions);

        if(result == AdoptionResult.IN_PROCESS) {
            return getTaskModule().evaluate(data, postActions);
        } else {
            return result;
        }
    }
}
