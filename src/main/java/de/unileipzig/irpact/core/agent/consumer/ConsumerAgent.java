package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.awareness.Awareness;
import de.unileipzig.irpact.core.misc.Initializable;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.agent.SpatialInformationAgent;

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
