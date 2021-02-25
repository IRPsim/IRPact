package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.core.agent.AgentGroup;
import de.unileipzig.irpact.core.process.ProcessFindingScheme;
import de.unileipzig.irpact.core.product.ProductFindingScheme;
import de.unileipzig.irpact.core.product.awareness.ProductAwarenessSupplyScheme;
import de.unileipzig.irpact.core.spatial.SpatialDistribution;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroup extends AgentGroup<ConsumerAgent> {

    double getInformationAuthority();

    Collection<ConsumerAgentGroupAttribute> getAttributes();

    boolean hasGroupAttribute(String name);

    ConsumerAgentGroupAttribute getGroupAttribute(String name);

    void addGroupAttribute(ConsumerAgentGroupAttribute attribute);

    SpatialDistribution getSpatialDistribution();

    ProductAwarenessSupplyScheme getAwarenessSupplyScheme();

    ConsumerAgent deriveAgent();

    boolean addAgent(ConsumerAgent agent);

    ProcessFindingScheme getProcessFindingScheme();

    ProductFindingScheme getProductFindingScheme();
}
