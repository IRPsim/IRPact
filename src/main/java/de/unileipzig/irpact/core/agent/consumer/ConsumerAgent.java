package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.AttributeAccess;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.agent.SpatialInformationAgent;
import de.unileipzig.irpact.core.product.interest.ProductInterest;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgent extends SpatialInformationAgent {

    ConsumerAgentGroup getGroup();

    Collection<ConsumerAgentAttribute> getAttributes();

    ConsumerAgentAttribute getAttribute(String name);

    boolean hasAttribute(String name);

    void addAttribute(ConsumerAgentAttribute attribute);

    ProductInterest getProductInterest();

    Collection<AdoptedProduct> getAdoptedProducts();

    boolean hasAdopted(Product product);

    void addAdoptedProduct(AdoptedProduct adoptedProduct);

    void adopt(Need need, Product product);

    boolean link(AttributeAccess attributeAccess);

    boolean unlink(AttributeAccess attributeAccess);

    Attribute<?> findAttribute(String name);
}
