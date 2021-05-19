package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.derivable.DependentDerivable;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentRelatedGroupAttribute<R, S>
        extends ConsumerAgentGroupAttribute, DependentDerivable<ConsumerAgentRelatedAttribute<R>, S> {
}
