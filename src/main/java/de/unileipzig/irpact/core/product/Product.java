package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.simulation.SimulationEntity;

import java.util.Collection;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface Product extends SimulationEntity {

    ProductGroup getGroup();

    Collection<ProductAttribute> getAttributes();

    boolean hasAttribute(String name);

    ProductAttribute getAttribute(String name);

    boolean isFixed();
}
