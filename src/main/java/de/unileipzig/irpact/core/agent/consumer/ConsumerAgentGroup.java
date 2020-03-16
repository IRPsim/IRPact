package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.core.agent.AgentGroup;
import de.unileipzig.irpact.core.need.NeedDevelopmentScheme;
import de.unileipzig.irpact.core.need.NeedExpirationScheme;
import de.unileipzig.irpact.core.need.NeedSatisfyScheme;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroup extends AgentGroup<ConsumerAgent> {

    Set<ConsumerAgentGroupAttribute> getAttributes();

    ProductFindingScheme getFindingScheme();

    ProductAdoptionDecisionScheme getAdoptionDecisionScheme();

    NeedDevelopmentScheme getNeedDevelopmentScheme();

    NeedExpirationScheme getNeedExpirationScheme();

    NeedSatisfyScheme getNeedSatisfyScheme();
}
