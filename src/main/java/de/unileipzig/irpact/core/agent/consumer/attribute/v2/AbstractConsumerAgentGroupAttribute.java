package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.commons.attribute.v3.AbstractGroupAttribute;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractConsumerAgentGroupAttribute
        extends AbstractGroupAttribute
        implements ConsumerAgentGroupAttribute {

    @Override
    public abstract AbstractConsumerAgentGroupAttribute copy();
}
