package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.distribution.UnivariateDistribution;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentStringGroupAttribute
        extends AbstractConsumerAgentGroupAttribute
        implements ConsumerAgentStringGroupAttribute {

    protected UnivariateDistribution<String> distribution;

    public BasicConsumerAgentStringGroupAttribute() {
    }

    public BasicConsumerAgentStringGroupAttribute(String name) {
        setName(name);
    }

    @Override
    public BasicConsumerAgentStringGroupAttribute copy() {
        BasicConsumerAgentStringGroupAttribute copy = new BasicConsumerAgentStringGroupAttribute();
        copy.setName(getName());
        copy.setArtificial(isArtificial());
        copy.setDistribution(getDistribution().copyDistribution());
        return copy;
    }

    @Override
    public BasicConsumerAgentStringAttribute derive() {
        String value = getDistribution().drawValue();
        return derive(value);
    }

    public BasicConsumerAgentStringAttribute derive(String value) {
        return derive(getName(), value);
    }

    public BasicConsumerAgentStringAttribute derive(String str, String value) {
        BasicConsumerAgentStringAttribute attr = new BasicConsumerAgentStringAttribute();
        attr.setName(str);
        attr.setGroup(this);
        attr.setArtificial(isArtificial());
        attr.setStringValue(value);
        return attr;
    }

    public void setDistribution(UnivariateDistribution<String> distribution) {
        this.distribution = distribution;
    }

    @Override
    public UnivariateDistribution<String> getDistribution() {
        return distribution;
    }

    public String drawDoubleValue() {
        return distribution.drawValue();
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                isArtificial(),
                getDistribution()
        );
    }
}
