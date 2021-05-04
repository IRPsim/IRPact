package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentDoubleGroupAttribute
        extends AbstractConsumerAgentGroupAttribute
        implements ConsumerAgentDoubleGroupAttribute {

    protected UnivariateDoubleDistribution distribution;

    @Override
    public BasicConsumerAgentDoubleGroupAttribute copy() {
        BasicConsumerAgentDoubleGroupAttribute copy = new BasicConsumerAgentDoubleGroupAttribute();
        copy.setName(getName());
        copy.setArtificial(isArtificial());
        copy.setDistribution(getDistribution().copyDistribution());
        return copy;
    }

    @Override
    public BasicConsumerAgentDoubleAttribute derive() {
        double value = getDistribution().drawDoubleValue();
        return derive(value);
    }

    @Override
    public BasicConsumerAgentDoubleAttribute derive(double value) {
        return derive(getName(), value);
    }

    @Override
    public BasicConsumerAgentDoubleAttribute derive(Number input) {
        return derive(getName(), input);
    }

    @Override
    public BasicConsumerAgentDoubleAttribute derive(String str, Number value) {
        return derive(str, value.doubleValue());
    }

    @Override
    public BasicConsumerAgentDoubleAttribute derive(String str, double value) {
        BasicConsumerAgentDoubleAttribute attr = new BasicConsumerAgentDoubleAttribute();
        attr.setName(str);
        attr.setGroup(this);
        attr.setArtificial(isArtificial());
        attr.setDoubleValue(value);
        return attr;
    }

    public void setDistribution(UnivariateDoubleDistribution distribution) {
        this.distribution = distribution;
    }

    @Override
    public UnivariateDoubleDistribution getDistribution() {
        return distribution;
    }

    @Override
    public double drawDoubleValue() {
        return distribution.drawDoubleValue();
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
