package de.unileipzig.irpact.v2.core.product;

import de.unileipzig.irpact.v2.core.simulation.SimulationEntity;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface Product extends SimulationEntity {

    ProductGroup getGroup();

    Set<ProductAttribute> getAttributes();
}
