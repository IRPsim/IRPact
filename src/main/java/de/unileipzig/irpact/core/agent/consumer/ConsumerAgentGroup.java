package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.core.agent.AgentGroup;
import de.unileipzig.irpact.core.process.ProcessFindingScheme;
import de.unileipzig.irpact.core.product.ProductFindingScheme;
import de.unileipzig.irpact.core.product.awareness.ProductAwarenessSupplyScheme;
import de.unileipzig.irpact.core.product.interest.ProductInterestSupplyScheme;
import de.unileipzig.irpact.core.spatial.distribution.SpatialDistribution;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroup extends AgentGroup<ConsumerAgent> {

    double getInformationAuthority();

    int getMaxNumberOfActions();

    Collection<ConsumerAgentGroupAttribute> getAttributes();

    boolean hasGroupAttribute(String name);

    default boolean hasGroupAttribute(ConsumerAgentGroupAttribute attribute) {
        return hasGroupAttribute(attribute.getName());
    }

    ConsumerAgentGroupAttribute getGroupAttribute(String name);

    void addGroupAttribute(ConsumerAgentGroupAttribute attribute);

    Collection<ProductRelatedConsumerAgentGroupAttribute> getProductRelatedGroupAttributes();

    boolean hasProductRelatedGroupAttribute(String name);

    default boolean hasProductRelatedGroupAttribute(ProductRelatedConsumerAgentGroupAttribute attribute) {
        return hasProductRelatedGroupAttribute(attribute.getName());
    }

    ProductRelatedConsumerAgentGroupAttribute getProductRelatedGroupAttribute(String name);

    void addProductRelatedGroupAttribute(ProductRelatedConsumerAgentGroupAttribute attribute);

    SpatialDistribution getSpatialDistribution();

    ProductAwarenessSupplyScheme getAwarenessSupplyScheme();

    ProductInterestSupplyScheme getInterestSupplyScheme();

    ConsumerAgent deriveAgent();

    boolean addAgent(ConsumerAgent agent);

    ProcessFindingScheme getProcessFindingScheme();

    ProductFindingScheme getProductFindingScheme();
}
