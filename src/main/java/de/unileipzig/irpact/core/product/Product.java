package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.product.attribute.ProductAttribute;
import de.unileipzig.irpact.core.simulation.SimulationEntity;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface Product extends SimulationEntity {

    ProductGroup getGroup();

    Collection<ProductAttribute> getAttributes();

    boolean hasAttribute(String name);

    ProductAttribute getAttribute(String name);

    boolean isFixed();

    boolean isNotFixed();
}
