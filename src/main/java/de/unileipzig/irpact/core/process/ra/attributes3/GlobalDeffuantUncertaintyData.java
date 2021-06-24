package de.unileipzig.irpact.core.process.ra.attributes3;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.util.DoubleRange;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.develop.AddToPersist;

import java.util.*;

/**
 * @author Daniel Abitz
 */
@AddToPersist
public class GlobalDeffuantUncertaintyData extends NameableBase implements DeffuantUncertaintyData {

    protected Map<String, DoubleRange> ranges;
    protected Set<String> attributeNames;
    protected SimulationEnvironment environment;
    protected double extremistParameter;
    protected double extremistUncertainty;
    protected double moderateUncertainty;
    protected boolean lowerBoundInclusive;
    protected boolean upperBoundInclusive;

    public GlobalDeffuantUncertaintyData() {
        this(new HashMap<>(), new LinkedHashSet<>());
    }

    public GlobalDeffuantUncertaintyData(Map<String, DoubleRange> ranges, Set<String> attributeNames) {
        this.ranges = ranges;
        this.attributeNames = attributeNames;
    }

    @Override
    public double getUncertainty(ConsumerAgentAttribute attribute) {
        if(isDisabled()) {
            return moderateUncertainty;
        }

        DoubleRange range = ranges.get(attribute.getName());
        if(range == null) {
            throw new NoSuchElementException("missing range: " + attribute.getName());
        }
        return range.isInRange(attribute.asValueAttribute().getDoubleValue())
                ? moderateUncertainty
                : extremistUncertainty;
    }

    @Override
    public void initalize() {
        update();
    }

    @Override
    public void update() {
        for(String attributeName: attributeNames) {
            put(attributeName);
        }
    }

    protected void put(String attributeName) {
        if(isDisabled()) {
            return;
        }

        double[] sortedValues = environment.getAgents().streamConsumerAgents()
                .map(ca -> ca.getAttribute(attributeName))
                .mapToDouble(attr -> attr.asValueAttribute().getDoubleValue())
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

        ranges.put(attributeName, range);
        attributeNames.add(attributeName);
    }

    public boolean isDisabled() {
        return extremistParameter == -1.0;
    }

    public void setExtremistParameter(double extremistParameter) {
        this.extremistParameter = extremistParameter < 0 ? -1.0 : extremistParameter;
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

    public Map<String, DoubleRange> getRanges() {
        return ranges;
    }

    public Set<String> getAttributeNames() {
        return attributeNames;
    }

    public boolean addAttributeName(String name) {
        return attributeNames.add(name);
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public SimulationEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public int getChecksum() throws UnsupportedOperationException {
        return Checksums.SMART.getChecksum(
                ranges,
                Checksums.SMART.getNamedChecksum(environment),
                attributeNames,
                extremistParameter,
                extremistUncertainty,
                moderateUncertainty,
                lowerBoundInclusive,
                upperBoundInclusive
        );
    }
}
