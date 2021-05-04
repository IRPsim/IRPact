package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.attribute.AbstractAttribute;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractConsumerAgentAttribute extends AbstractAttribute implements ConsumerAgentAttribute {

    protected ConsumerAgentGroupAttribute group;

    @Override
    public abstract AbstractConsumerAgentAttribute copy();

    @Override
    public ConsumerAgentGroupAttribute getGroup() {
        return group;
    }

    public void setGroup(ConsumerAgentGroupAttribute group) {
        this.group = group;
    }
}
