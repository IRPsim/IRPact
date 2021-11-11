package de.unileipzig.irpact.core.process2.uncert;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.DoubleRange;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class GlobalDeffuantUncertainty
        extends NameableBase
        implements Uncertainty, DeffuantUncertainty, UncertaintySupplier, LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(GlobalDeffuantUncertainty.class);

    protected Set<String> attributeNames;
    protected Map<String, DoubleRange> ranges;
    protected SimulationEnvironment environment;
    protected double extremistParameter;
    protected double extremistUncertainty;
    protected double moderateUncertainty;
    protected boolean lowerBoundInclusive;
    protected boolean upperBoundInclusive;

    public GlobalDeffuantUncertainty() {
        this(new HashSet<>(), new HashMap<>());
    }

    public GlobalDeffuantUncertainty(Set<String> attributeNames, Map<String, DoubleRange> ranges) {
        this.attributeNames = attributeNames;
        this.ranges = ranges;
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void initalize() {
        trace(
                "[{}] init: extremistParameter={}, extremistUncertainty={}, moderateUncertainty={}, lowerBoundInclusive={}, upperBoundInclusive={}",
                getName(),
                getExtremistParameter(),
                getExtremistUncertainty(),
                getModerateUncertainty(),
                isLowerBoundInclusive(),
                isUpperBoundInclusive()
        );
        update();
    }

    @Override
    public boolean isSupported(ConsumerAgent agent) {
        return agent != null;
    }

    public void addAttributeName(String name) {
        attributeNames.add(name);
    }

    @Override
    public GlobalDeffuantUncertainty createFor(ConsumerAgent agent) {
        if(agent == null) {
            throw new NullPointerException("agent is null");
        }
        return this;
    }

    @Override
    public void update() {
        ranges.clear();
        for(String attributeName: attributeNames) {
            put(attributeName);
        }
    }

    protected double getAttributeValue(ConsumerAgent agent, String attributeName) {
        return agent.getAttribute(attributeName).asValueAttribute().getDoubleValue();
    }

    protected void put(String attributeName) {
        if(isAllExtremist()) {
            trace("[{}] all extremist, skip range calculation");
            return;
        }

        if(isAllModerate()) {
            trace("[{}] all moderate, skip range calculation");
            return;
        }

        double[] sortedValues = environment.getAgents().streamConsumerAgents()
                .mapToDouble(agent -> getAttributeValue(agent, attributeName))
                .sorted()
                .toArray();

        int lowerIndex = (int) (sortedValues.length * extremistParameter / 2.0);
        int upperIndex = sortedValues.length - lowerIndex - 1;
        double lowerBound = sortedValues[lowerIndex];
        double upperBound = sortedValues[upperIndex];

        DoubleRange range = new DoubleRange();
        range.setLowerBound(lowerBound);
        range.setUpperBound(upperBound);
        range.setLowerBoundInclusive(lowerBoundInclusive);
        range.setUpperBoundInclusive(upperBoundInclusive);

        trace("[{}] set range {} for attribute {}", getName(), range, attributeName);

        ranges.put(attributeName, range);
    }

    @Override
    public void updateUncertainty(ConsumerAgentAttribute attribute, double value) {
        //uncertainty not changeable
    }

    @Override
    public void setUncertainty(ConsumerAgentAttribute attribute, double value) {
        //uncertainty not changeable
    }

    public void setExtremistParameter(double extremistParameter) {
        this.extremistParameter = Math.max(0, Math.min(1, extremistParameter));
    }

    public double getExtremistParameter() {
        return extremistParameter;
    }

    public void setExtremistUncertainty(double extremistUncertainty) {
        this.extremistUncertainty = extremistUncertainty;
    }

    public double getExtremistUncertainty() {
        return extremistUncertainty;
    }

    public void setModerateUncertainty(double moderateUncertainty) {
        this.moderateUncertainty = moderateUncertainty;
    }

    public double getModerateUncertainty() {
        return moderateUncertainty;
    }

    public void setLowerBoundInclusive(boolean lowerBoundInclusive) {
        this.lowerBoundInclusive = lowerBoundInclusive;
    }

    public boolean isLowerBoundInclusive() {
        return lowerBoundInclusive;
    }

    public void setUpperBoundInclusive(boolean upperBoundInclusive) {
        this.upperBoundInclusive = upperBoundInclusive;
    }

    public boolean isUpperBoundInclusive() {
        return upperBoundInclusive;
    }

    public boolean isAllModerate() {
        return extremistParameter == 0.0;
    }

    public boolean isAllExtremist() {
        return extremistParameter == 1.0;
    }

    @Override
    public double getUncertainty(ConsumerAgentAttribute attribute) {
        if(isAllModerate()) return moderateUncertainty;
        if(isAllExtremist()) return extremistUncertainty;

        DoubleRange range = ranges.get(attribute.getName());
        if(range == null) {
            throw new NoSuchElementException("missing range: " + attribute.getName());
        }
        return range.isInRange(attribute.asValueAttribute().getDoubleValue())
                ? moderateUncertainty
                : extremistUncertainty;
    }
}
