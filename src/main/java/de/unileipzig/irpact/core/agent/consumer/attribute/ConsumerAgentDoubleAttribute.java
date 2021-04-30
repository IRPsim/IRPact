package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.attribute.GroupEntityDoubleAttribute;

/**
 * @author Daniel Abitz
 */
public class ConsumerAgentDoubleAttribute extends GroupEntityDoubleAttribute<ConsumerAgentGroupAttribute> implements ConsumerAgentValueAttribute {

    public ConsumerAgentDoubleAttribute() {
    }

    public ConsumerAgentDoubleAttribute(String name, ConsumerAgentGroupAttribute groupAttribute, boolean artificial, double value) {
        setName(name);
        setGroup(groupAttribute);
        setDoubleValue(value);
        setArtificial(artificial);
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
