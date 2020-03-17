package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.distribution.UnivariateDistribution;
import de.unileipzig.irpact.core.agent.AgentGroup;
import de.unileipzig.irpact.core.need.NeedDevelopmentScheme;
import de.unileipzig.irpact.core.need.NeedExpirationScheme;
import de.unileipzig.irpact.core.need.NeedSatisfyScheme;
import de.unileipzig.irpact.core.preference.Value;

import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroup extends AgentGroup<ConsumerAgent> {

    double getInformationAuthority();

    Set<ConsumerAgentGroupAttribute> getAttributes();

    Map<Value, UnivariateDistribution> getValuePreferences();

    ProductFindingScheme getFindingScheme();

    ProductAdoptionDecisionScheme getAdoptionDecisionScheme();

    NeedDevelopmentScheme getNeedDevelopmentScheme();

    NeedExpirationScheme getNeedExpirationScheme();

    NeedSatisfyScheme getNeedSatisfyScheme();
}
