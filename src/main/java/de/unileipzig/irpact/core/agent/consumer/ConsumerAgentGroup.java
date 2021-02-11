package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.awareness.Awareness;
import de.unileipzig.irpact.commons.awareness.AwarenessDistributionMapping;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.agent.AgentGroup;
import de.unileipzig.irpact.core.process.ProcessFindingScheme;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductFindingScheme;
import de.unileipzig.irpact.core.spatial.SpatialDistribution;

import java.util.Collection;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroup extends AgentGroup<ConsumerAgent> {

    double getInformationAuthority();

    Collection<ConsumerAgentGroupAttribute> getAttributes();

    SpatialDistribution getSpatialDistribution();

    void setSpatialDistribution(SpatialDistribution distribution);

    Awareness<Product> getProductAwareness();

    AwarenessDistributionMapping<Product, UnivariateDoubleDistribution> getFixedProductAwarenessMapping();

    ConsumerAgent deriveAgent();

    boolean addAgent(ConsumerAgent agent);

    boolean addGroupAttribute(ConsumerAgentGroupAttribute cagAttr);

    boolean hasGroupAttribute(String name);

    void replace(ConsumerAgent toRemove, ConsumerAgent toAdd) throws IllegalStateException;

    ProcessFindingScheme getProcessFindingScheme();

    ProductFindingScheme getProductFindingScheme();
}
