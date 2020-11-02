package de.unileipzig.irpact.v2.core.agent.consumer;

import de.unileipzig.irpact.v2.commons.awareness.AwarenessDistributionMapping;
import de.unileipzig.irpact.v2.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.v2.core.agent.AgentGroup;
import de.unileipzig.irpact.v2.core.product.Product;
import de.unileipzig.irpact.v2.core.spatial.SpatialDistribution;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroup extends AgentGroup<ConsumerAgent> {

    double getInformationAuthority();

    Set<ConsumerAgentGroupAttribute> getAttributes();

    SpatialDistribution getSpatialDistribution();

    AwarenessDistributionMapping<Product, UnivariateDoubleDistribution> getFixedProductAwarenessMapping();

    ConsumerAgentInitializationData derive();
}
