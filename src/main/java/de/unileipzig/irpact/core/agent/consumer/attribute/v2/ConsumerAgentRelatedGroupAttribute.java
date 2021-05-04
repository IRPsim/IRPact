package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.commons.DependentDerivable;
import de.unileipzig.irpact.commons.DirectDerivable;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentRelatedGroupAttribute<R, S>
        extends ConsumerAgentGroupAttribute, DependentDerivable<ConsumerAgentRelatedAttribute<R>, S> {
}
