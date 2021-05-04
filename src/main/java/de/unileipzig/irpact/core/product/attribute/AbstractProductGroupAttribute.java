package de.unileipzig.irpact.core.product.attribute;

import de.unileipzig.irpact.commons.attribute.v2.AbstractGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.v2.ConsumerAgentGroupAttribute;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractProductGroupAttribute
        extends AbstractGroupAttribute
        implements ConsumerAgentGroupAttribute {

    @Override
    public abstract AbstractProductGroupAttribute copy();
}
