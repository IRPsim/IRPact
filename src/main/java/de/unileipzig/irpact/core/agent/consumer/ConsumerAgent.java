package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.core.GroupEntity;
import de.unileipzig.irpact.core.agent.SpatialInformationAgent;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgent extends SpatialInformationAgent, GroupEntity<ConsumerAgent> {

    @Override
    ConsumerAgentIdentifier getIdentifier();

    @Override
    ConsumerAgentGroup getGroup();

    Set<ConsumerAgentAttribute> getAttributes();

    Set<Need> getNeeds();

    Set<Product> getKnownProducts();

    Set<ProductGroup> getKnownProductGroups();
}
