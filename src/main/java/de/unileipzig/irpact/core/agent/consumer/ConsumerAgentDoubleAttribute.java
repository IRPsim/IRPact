package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.attribute.DoubleAttributeGroupEntity;

/**
 * @author Daniel Abitz
 */
public class ConsumerAgentDoubleAttribute extends DoubleAttributeGroupEntity<ConsumerAgentGroupAttribute> implements ConsumerAgentAttribute {

    protected boolean artificial;

    public ConsumerAgentDoubleAttribute() {
    }

    public ConsumerAgentDoubleAttribute(String name, ConsumerAgentGroupAttribute groupAttribute, boolean artificial, double value) {
        setName(name);
        setGroup(groupAttribute);
        setDoubleValue(value);
        setArtificial(artificial);
    }

    @Override
    public boolean isArtificial() {
        return artificial;
    }

    public void setArtificial(boolean artificial) {
        this.artificial = artificial;
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
