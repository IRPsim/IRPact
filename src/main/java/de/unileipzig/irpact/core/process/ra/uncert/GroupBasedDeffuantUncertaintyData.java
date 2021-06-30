package de.unileipzig.irpact.core.process.ra.uncert;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.util.DoubleRange;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class GroupBasedDeffuantUncertaintyData extends NameableBase implements DeffuantUncertaintyData {

    protected Map<String, DoubleRange> ranges;
    protected ConsumerAgentGroup cag;
    protected double extremistParameter;
    protected double extremistUncertainty;
    protected double moderateUncertainty;
    protected boolean lowerBoundInclusive;
    protected boolean upperBoundInclusive;

    public GroupBasedDeffuantUncertaintyData() {
        this(new HashMap<>());
    }

    public GroupBasedDeffuantUncertaintyData(Map<String, DoubleRange> ranges) {
        this.ranges = ranges;
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
        Set<String> attributeNames = new LinkedHashSet<>(ranges.keySet());
        for(String attributeName: attributeNames) {
            put(attributeName);
        }
    }

    protected void put(String attributeName) {
        if(isDisabled()) {
            return;
        }

        double[] sortedValues = cag.streamAgents()
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

    public boolean addAttributeName(String name) {
        if(ranges.containsKey(name)) {
            return false;
        } else {
            ranges.put(name, null);
            return true;
        }
    }

    public void setConsumerAgentGroup(ConsumerAgentGroup cag) {
        this.cag = cag;
    }

    public ConsumerAgentGroup getConsumerAgentGroup() {
        return cag;
    }

    @Override
    public int getChecksum() throws UnsupportedOperationException {
        return Checksums.SMART.getChecksum(
                ranges,
                Checksums.SMART.getNamedChecksum(cag),
                extremistParameter,
                extremistUncertainty,
                moderateUncertainty,
                lowerBoundInclusive,
                upperBoundInclusive
        );
    }
}