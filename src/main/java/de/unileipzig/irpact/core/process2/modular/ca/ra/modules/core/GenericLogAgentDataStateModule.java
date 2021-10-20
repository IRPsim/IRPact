package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.modules.core.Module2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class GenericLogAgentDataStateModule<O>
        extends AbstractUniformCAMultiModule1_2<O, O, Module2<ConsumerAgentData2, O>>
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(GenericLogAgentDataStateModule.class);

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected void validateSelf() throws Throwable {
        traceModuleValidation();
    }

    @Override
    protected void initializeSelf(SimulationEnvironment environment) throws Throwable {
        traceModuleInitalization();
    }

    @Override
    public O apply(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleInfo(input);
        O result = getNonnullSubmodule().apply(input, actions);
        logAgentDataState(input);
        return result;
    }
}
