package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.awareness.Awareness;
import de.unileipzig.irpact.commons.awareness.AwarenessDistributionMapping;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.agent.AgentGroup;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.spatial.SpatialDistribution;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroup extends AgentGroup<ConsumerAgent> {

    double getInformationAuthority();

    Set<ConsumerAgentGroupAttribute> getAttributes();

    SpatialDistribution getSpatialDistribution();

    Awareness<Product> getProductAwareness();

    AwarenessDistributionMapping<Product, UnivariateDoubleDistribution> getFixedProductAwarenessMapping();

    ConsumerAgentInitializationData derive();
}
