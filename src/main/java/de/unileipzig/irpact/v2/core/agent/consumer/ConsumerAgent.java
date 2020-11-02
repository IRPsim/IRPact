package de.unileipzig.irpact.v2.core.agent.consumer;

import de.unileipzig.irpact.v2.core.product.Product;
import de.unileipzig.irpact.v2.commons.awareness.Awareness;
import de.unileipzig.irpact.v2.core.agent.SpatialInformationAgent;
import de.unileipzig.irpact.v2.core.misc.Initializable;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgent extends SpatialInformationAgent, Initializable<ConsumerAgentInitializationData> {

    ConsumerAgentGroup getGroup();

    Set<ConsumerAgentAttribute> getAttributes();

    ConsumerAgentAttribute getAttribute(String name);

    Awareness<Product> getProductAwareness();

    Set<Product> getAdoptedProducts();
}
