package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.attribute.DoubleAttributeGroupEntity;

/**
 * @author Daniel Abitz
 */
public class ConsumerAgentDoubleAttribute extends DoubleAttributeGroupEntity<ConsumerAgentGroupAttribute> implements ConsumerAgentAttribute {

    public ConsumerAgentDoubleAttribute() {
    }

    public ConsumerAgentDoubleAttribute(String name, ConsumerAgentGroupAttribute groupAttribute, double value) {
        setName(name);
        setGroup(groupAttribute);
        setDoubleValue(value);
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                ChecksumComparable.getNameChecksum(getGroup()),
                getDoubleValue()
        );
    }
}
