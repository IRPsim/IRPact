package de.unileipzig.irpact.core.process2.handler;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Daniel Abitz
 */
public class LinearePercentageAgentAttributeScaler
        extends AbstractInitializationHandler
        implements InitializationHandler, LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(LinearePercentageAgentAttributeScaler.class);

    protected final Map<ConsumerAgent, Double> growthFactors = new ConcurrentHashMap<>();
    protected String attributeName;
    protected double m;
    protected double n;

    public LinearePercentageAgentAttributeScaler() {
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setM(double m) {
        this.m = m;
    }

    public double getM() {
        return m;
    }

    public void setN(double n) {
        this.n = n;
    }

    public double getN() {
        return n;
    }

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.INITIALIZATION_PARAMETER;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    protected double lin(double input) {
        return m * input + n;
    }

    protected static int getFirstYear(SimulationEnvironment environment) {
        return environment.getTimeModel().getFirstSimulationYear();
    }

    protected static int getLastYear(SimulationEnvironment environment) {
        return environment.getTimeModel().getLastSimulationYear();
    }

    protected double getScaleFactor(SimulationEnvironment environment) {
        return lin(getFirstYear(environment)) / lin(getLastYear(environment) + 1);
    }

    protected double getGrowth() {
        return m;
    }

    protected void storeGrowthFactor(ConsumerAgent agent, double value) {
        growthFactors.put(agent, value);
    }

    public double getGrowthFactor(ConsumerAgent agent) {
        Double value = growthFactors.get(agent);
        if(value == null) {
            throw new NoSuchElementException(StringUtil.format("missing growth factor for agent '{}'", agent));
        }
        return value;
    }

    public double getValue(SimulationEnvironment environment, ConsumerAgent agent) {
        return environment.getAttributeHelper().getDouble(agent, null, getAttributeName(), true);
    }

    public void setValue(SimulationEnvironment environment, ConsumerAgent agent, double value) {
        environment.getAttributeHelper().setDouble(agent, null, getAttributeName(), value, true);
    }

    @Override
    public void initalize(SimulationEnvironment environment) throws Throwable {
        LOGGER.debug("run {} '{}' for attribute '{}'", getClass().getSimpleName(), getName(), getAttributeName());
        LOGGER.trace("function: f(x) = '{}' * x + '{}'", getM(), getN());

        double scaleFactor = getScaleFactor(environment);
        LOGGER.trace("scaleFactor={}", scaleFactor);

        double growth = getGrowth();
        LOGGER.trace("growth={}", growth);

        MutableDouble min = MutableDouble.empty();
        MutableDouble max = MutableDouble.empty();

        LOGGER.trace("search min/max...");
        for(ConsumerAgent ca: environment.getAgents().iterableConsumerAgents()) {
            double value = getValue(environment, ca);
            min.setMin(value);
            max.setMax(value);
        }
        LOGGER.trace("... min={}, max={}", min.get(), max.get());

        LOGGER.trace("update attributes...");
        for(ConsumerAgent ca: environment.getAgents().iterableConsumerAgents()) {
            double initialValue = getValue(environment, ca);
            double rangedValue = (initialValue - min.get()) / (max.get() - min.get());
            double scaledValue = rangedValue * scaleFactor;
            setValue(environment, ca, scaledValue);
            LOGGER.trace("update '{}' (initial={} -> inRange={} -> scaled={}): {}", ca.getName(), initialValue, rangedValue, scaledValue, getValue(environment, ca));

            double growthFactor = scaledValue * growth;
            storeGrowthFactor(ca, growthFactor);
            LOGGER.trace("growth factor for '{}': {}", ca.getName(), growthFactor);
        }
        LOGGER.trace("... update finished");
    }
}
