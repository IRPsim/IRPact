package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.simulation.SimulationEntity;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface Product extends SimulationEntity {

    ProductGroup getGroup();

    Set<ProductAttribute> getAttributes();

    boolean isFixed();
}
