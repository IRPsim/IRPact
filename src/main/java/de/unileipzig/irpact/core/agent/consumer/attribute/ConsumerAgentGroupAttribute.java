package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.derivable.DirectDerivable;
import de.unileipzig.irpact.commons.attribute.GroupAttribute;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroupAttribute extends GroupAttribute, DirectDerivable<ConsumerAgentAttribute> {

    @Override
    ConsumerAgentGroupAttribute copy();

    default boolean isDoubleGroupAttribute() {
        return false;
    }

    default boolean isStringGroupAttribute() {
        return false;
    }

    default ConsumerAgentDoubleGroupAttribute asDoubleGroupAttribute() {
        throw new UnsupportedOperationException();
    }

    default ConsumerAgentStringGroupAttribute asStringGroupAttribute() {
        throw new UnsupportedOperationException();
    }

    default boolean isProductRelatedGroupAttribute() {
        return false;
    }

    default ConsumerAgentProductRelatedGroupAttribute asProductRelatedGroupAttribute() {
        throw new UnsupportedOperationException();
    }

    default boolean isAnnualGroupAttribute() {
        return false;
    }

    default ConsumerAgentAnnualGroupAttribute asAnnualGroupAttribute() {
        throw new UnsupportedOperationException();
    }
}
