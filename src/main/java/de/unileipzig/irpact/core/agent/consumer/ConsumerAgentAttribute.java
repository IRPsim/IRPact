package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.attribute.AttributeGroupEntity;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentAttribute extends AttributeGroupEntity<ConsumerAgentGroupAttribute> {

    boolean isArtificial();
}
