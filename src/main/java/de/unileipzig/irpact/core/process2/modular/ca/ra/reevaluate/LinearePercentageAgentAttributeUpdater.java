package de.unileipzig.irpact.core.process2.modular.ca.ra.reevaluate;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.handler.LinearePercentageAgentAttributeScaler;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.reevaluate.AbstractReevaluator;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class LinearePercentageAgentAttributeUpdater
        extends AbstractReevaluator<ConsumerAgentData2>
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(LinearePercentageAgentAttributeUpdater.class);

    protected LinearePercentageAgentAttributeScaler scaler;

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    public void setScaler(LinearePercentageAgentAttributeScaler scaler) {
        this.scaler = scaler;
    }

    public LinearePercentageAgentAttributeScaler getScaler() {
        return scaler;
    }

    @Override
    public void initializeReevaluator(SimulationEnvironment environment) throws Throwable {
        if(scaler == null) {
            throw new NullPointerException("missing scaler");
        }
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
    public void reevaluate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceReevaluatorInfo(input);
        double oldValue = scaler.getValue(input.getEnvironment(), input.getAgent());
        double growth = scaler.getGrowthFactor(input.getAgent());
        double newValue = oldValue + growth;
        scaler.setValue(input.getEnvironment(), input.getAgent(), newValue);
        trace("[{}]@[{}] update '{}' {} -> {} (growth={})", getName(), input.getAgentName(), scaler.getAttributeName(), oldValue, newValue, growth);
    }
}
