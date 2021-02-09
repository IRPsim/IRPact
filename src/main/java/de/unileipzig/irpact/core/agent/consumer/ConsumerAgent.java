package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.awareness.Awareness;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.agent.SpatialInformationAgent;

import java.util.Collection;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgent extends SpatialInformationAgent {

    ConsumerAgentGroup getGroup();

    Collection<ConsumerAgentAttribute> getAttributes();

    ConsumerAgentAttribute getAttribute(String name);

    boolean addAttribute(ConsumerAgentAttribute attribute);

    Awareness<Product> getProductAwareness();

    Collection<AdoptedProduct> getAdoptedProducts();

    boolean hasAdopted(Product product);

    boolean adopt(Need need, Product product);
}
