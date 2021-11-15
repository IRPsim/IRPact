package de.unileipzig.irpact.core.process2.uncert;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.DoubleRange;
import de.unileipzig.irpact.commons.util.Quantile;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;
import java.util.function.DoubleConsumer;
import java.util.function.ToDoubleFunction;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractGlobalModerateExtremistUncertainty
        extends NameableBase
        implements UncertaintySupplier, LoggingHelper {

    protected static final ToDoubleFunction<Double> FUNC = Double::doubleValue;

    protected SimulationEnvironment environment;
    protected Set<String> attributeNames;
    protected Map<String, DoubleRange> ranges;
    protected double extremistParameter;
    protected double extremistParameterHalf;
    protected double extremistUncertainty;
    protected double moderateUncertainty;
    protected boolean lowerBoundInclusive;
    protected boolean upperBoundInclusive;

    protected boolean initalized = false;

    public AbstractGlobalModerateExtremistUncertainty() {
        this(new HashSet<>(), new HashMap<>());
    }

    public AbstractGlobalModerateExtremistUncertainty(Set<String> attributeNames, Map<String, DoubleRange> ranges) {
        this.attributeNames = attributeNames;
        this.ranges = ranges;
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public abstract IRPLogger getDefaultLogger();

    protected void checkInitalized() {
        if(!initalized) {
            throw new IllegalStateException("not initalized: " + getName());
        }
    }

    @Override
    public void initalize() {
        if(initalized) {
            return;
        }

        trace(
                "[{}] initalize: extremistParameter={}, extremistUncertainty={}, moderateUncertainty={}, lowerBoundInclusive={}, upperBoundInclusive={}",
                getName(),
                getExtremistParameter(),
                getExtremistUncertainty(),
                getModerateUncertainty(),
                isLowerBoundInclusive(),
                isUpperBoundInclusive()
        );
        initalized = true;
        putAll(attributeNames);
    }

    @Override
    public boolean isSupported(ConsumerAgent agent) {
        return agent != null;
    }

    public void addAttributeName(String name) {
        attributeNames.add(name);
    }

    @Override
    public void update() {
        trace("[{}] update ranges [@ {}]", getName(), tryGetNow());
        ranges.clear();
        putAll(attributeNames);
    }

    protected double getAttributeValue(ConsumerAgent agent, String attributeName) {
        return agent.getAttribute(attributeName).asValueAttribute().getDoubleValue();
    }

    protected void putAll(Collection<? extends String> attributeNames) {
        for(String attributeName: attributeNames) {
            put(attributeName);
        }
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

        List<Double> sortedValues = new ArrayList<>();
        collectValues(attributeName, sortedValues::add);
        sortedValues.sort(Double::compareTo);

        updateRange(attributeName, sortedValues);
    }

    protected void collectValues(String attributeName, DoubleConsumer consumer) {
        environment.getAgents().streamConsumerAgents()
                .mapToDouble(agent -> getAttributeValue(agent, attributeName))
                .forEach(consumer);
    }

    protected Timestamp tryGetNow() {
        try {
            return environment.getTimeModel().now();
        } catch (Throwable t) {
            return null;
        }
    }

    protected void updateRange(String attributeName, List<Double> sortedValues) {
        double min = sortedValues.get(0);
        double max = sortedValues.get(sortedValues.size() - 1);
        double lowerBound = Quantile.calculate(sortedValues, FUNC, extremistParameterHalf);
        double upperBound = Quantile.calculate(sortedValues, FUNC, 1.0 - extremistParameterHalf);

        DoubleRange range = new DoubleRange();
        range.setLowerBound(lowerBound);
        range.setUpperBound(upperBound);
        range.setLowerBoundInclusive(lowerBoundInclusive);
        range.setUpperBoundInclusive(upperBoundInclusive);

        trace(
                "[{}] set range for attribute '{}' (min={}, lowerBound={}, upperBound={}, max={}): {} [@ {}]",
                getName(),
                attributeName,
                min,
                lowerBound,
                upperBound,
                max,
                range,
                tryGetNow()
        );

        ranges.put(attributeName, range);
    }

    public void setExtremistParameter(double extremistParameter) {
        this.extremistParameter = Math.max(0, Math.min(1, extremistParameter));
        this.extremistParameterHalf = extremistParameter * 0.5;
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
}
