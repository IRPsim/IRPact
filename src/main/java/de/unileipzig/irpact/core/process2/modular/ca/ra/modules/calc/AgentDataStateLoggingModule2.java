package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.data.AgentDataStateLoggingHelper;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModule1_2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class AgentDataStateLoggingModule2
        extends AbstractUniformCAMultiModule1_2<Number, Number, CalculationModule2<ConsumerAgentData2>>
        implements CalculationModule2<ConsumerAgentData2>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AgentDataStateLoggingModule2.class);

    protected AgentDataStateLoggingHelper loggingHelper;

    public void setLoggingHelper(AgentDataStateLoggingHelper loggingHelper) {
        this.loggingHelper = loggingHelper;
    }

    public AgentDataStateLoggingHelper getLoggingHelper() {
        return loggingHelper;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected void validateSelf() throws Throwable {
        if(loggingHelper == null) {
            throw new NullPointerException("missing logging helper");
        }
    }

    @Override
    protected void initializeSelf(SimulationEnvironment environment) throws Throwable {
    }

    @Override
    protected ConsumerAgentData2 castInput(ConsumerAgentData2 input) {
        return input;
    }

    @Override
    protected void initializeNewInputSelf(ConsumerAgentData2 input) throws Throwable {
    }

    @Override
    protected void setupSelf(SimulationEnvironment environment) throws Throwable {
    }

    @Override
    public double calculate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        double value = getNonnullSubmodule().calculate(input, actions);
        updateAgentDataState(input, loggingHelper, value);
        return value;
    }
}
