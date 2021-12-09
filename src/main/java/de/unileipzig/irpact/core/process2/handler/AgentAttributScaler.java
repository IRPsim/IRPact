package de.unileipzig.irpact.core.process2.handler;

import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class AgentAttributScaler
        extends AbstractInitializationHandler
        implements InitializationHandler, LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AgentAttributScaler.class);

    protected String attributeName;

    public AgentAttributScaler() {
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.INITIALIZATION_PARAMETER;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    protected double getValue(SimulationEnvironment environment, ConsumerAgent agent) {
        return environment.getAttributeHelper().getDouble(agent, null, getAttributeName(), true);
    }

    protected void setValue(SimulationEnvironment environment, ConsumerAgent agent, double value) {
        environment.getAttributeHelper().setDouble(agent, null, getAttributeName(), value, true);
    }

    @Override
    public void initalize(SimulationEnvironment environment) throws Throwable {
        LOGGER.debug("run {} '{}' for attribute '{}'", getClass().getSimpleName(), getName(), getAttributeName());

        MutableDouble min = MutableDouble.empty();
        MutableDouble max = MutableDouble.empty();

        LOGGER.trace("search min/max...");
        for(ConsumerAgent ca: environment.getAgents().iterableConsumerAgents()) {
            double value = getValue(environment, ca);
            min.setMin(value);
            max.setMax(value);
        }
        LOGGER.trace("... min={}, max={}", min.get(), max.get());

        if(min.isEquals(max)) {
            LOGGER.trace("min==max, skip scaling");
        } else {
            LOGGER.trace("update attributes...");
            for(ConsumerAgent ca: environment.getAgents().iterableConsumerAgents()) {
                double value = getValue(environment, ca);
                double newValue = (value - min.get()) / (max.get() - min.get());
                setValue(environment, ca, newValue);
                //spam
                LOGGER.trace("update '{}': {} -> {}", ca.getName(), value, getValue(environment, ca));
            }
            LOGGER.trace("... update finished");
        }
    }
}
