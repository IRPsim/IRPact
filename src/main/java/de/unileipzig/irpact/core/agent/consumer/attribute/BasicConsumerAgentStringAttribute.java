package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.checksum.Checksums;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentStringAttribute
        extends AbstractConsumerAgentAttribute
        implements ConsumerAgentStringAttribute {

    protected String value;

    public BasicConsumerAgentStringAttribute() {
    }

    public BasicConsumerAgentStringAttribute(String name) {
        setName(name);
    }

    @Override
    public BasicConsumerAgentStringAttribute copy() {
        BasicConsumerAgentStringAttribute copy = new BasicConsumerAgentStringAttribute();
        copy.setGroup(getGroup());
        copy.setName(getName());
        copy.setArtificial(isArtificial());
        copy.setDoubleValue(getDoubleValue());
        return copy;
    }

    @Override
    public String getStringValue() {
        return value;
    }

    @Override
    public void setStringValue(String value) {
        this.value = value;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getName(),
                isArtificial(),
                getGroup().getName(),
                getDoubleValue()
        );
    }
}
