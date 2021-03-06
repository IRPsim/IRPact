package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentDoubleAttribute
        extends AbstractConsumerAgentAttribute
        implements ConsumerAgentDoubleAttribute {

    protected double value;

    public BasicConsumerAgentDoubleAttribute() {
    }

    public BasicConsumerAgentDoubleAttribute(String name, double value) {
        setName(name);
        setDoubleValue(value);
    }

    @Override
    public BasicConsumerAgentDoubleAttribute copy() {
        BasicConsumerAgentDoubleAttribute copy = new BasicConsumerAgentDoubleAttribute();
        copy.setGroup(getGroup());
        copy.setName(getName());
        copy.setArtificial(isArtificial());
        copy.setDoubleValue(getDoubleValue());
        return copy;
    }

    @Override
    public double getDoubleValue() {
        return value;
    }

    @Override
    public void setDoubleValue(double value) {
        this.value = value;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                isArtificial(),
                ChecksumComparable.getNameChecksum(getGroup()),
                getDoubleValue()
        );
    }
}
