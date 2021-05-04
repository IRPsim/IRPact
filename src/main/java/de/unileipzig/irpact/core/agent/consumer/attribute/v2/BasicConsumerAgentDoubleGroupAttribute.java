package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.develop.AddToPersist;

/**
 * @author Daniel Abitz
 */
@AddToPersist
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
        BasicConsumerAgentDoubleAttribute attr = new BasicConsumerAgentDoubleAttribute();
        attr.setName(getName());
        attr.setGroup(this);
        attr.setArtificial(isArtificial());
        attr.setDoubleValue(value);
        return attr;
    }

    public void setDistribution(UnivariateDoubleDistribution distribution) {
        this.distribution = distribution;
    }

    public UnivariateDoubleDistribution getDistribution() {
        return distribution;
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
