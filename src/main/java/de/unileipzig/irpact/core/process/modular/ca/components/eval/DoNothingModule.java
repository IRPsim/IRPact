package de.unileipzig.irpact.core.process.modular.ca.components.eval;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentCalculationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModuleWithSubModules;
import de.unileipzig.irpact.core.process.PostAction;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class DoNothingModule
        extends AbstractConsumerAgentModuleWithSubModules
        implements ConsumerAgentEvaluationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DoNothingModule.class);

    public DoNothingModule() {
        super();
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getName()
        );
    }

    @Override
    public void addSubModule(ConsumerAgentModule subModule) {
        super.addSubModule(subModule);
    }

    @Override
    public AdoptionResult evaluate(ConsumerAgentData data, List<PostAction<?>> postActions) throws Throwable {
        for(ConsumerAgentModule module: iterateModules()) {
            if(module instanceof ConsumerAgentCalculationModule) {
                LOGGER.trace(
                        IRPSection.SIMULATION_PROCESS,
                        "[{}] '{}' call calc submodule '{}'",
                        data.getAgent().getName(),
                        getName(),
                        module.getName()
                );
                ConsumerAgentCalculationModule calcModule = (ConsumerAgentCalculationModule) module;
                calcModule.calculate(data);
            }
            if(module instanceof ConsumerAgentEvaluationModule) {
                LOGGER.trace(
                        IRPSection.SIMULATION_PROCESS,
                        "[{}] '{}' call eval submodule '{}'",
                        data.getAgent().getName(),
                        getName(),
                        module.getName()
                );
                ConsumerAgentEvaluationModule evalModule = (ConsumerAgentEvaluationModule) module;
                evalModule.evaluate(data, postActions);
            }
        }

        return AdoptionResult.IN_PROCESS;
    }
}
