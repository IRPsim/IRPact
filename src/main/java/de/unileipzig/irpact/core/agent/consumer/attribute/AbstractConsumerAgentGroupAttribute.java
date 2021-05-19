package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.attribute.AbstractGroupAttribute;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractConsumerAgentGroupAttribute
        extends AbstractGroupAttribute
        implements ConsumerAgentGroupAttribute {

    @Override
    public abstract AbstractConsumerAgentGroupAttribute copy();
}
