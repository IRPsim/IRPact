package de.unileipzig.irpact.v2.core.product;

import de.unileipzig.irpact.v2.core.simulation.SimulationEntity;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface ProductGroup extends SimulationEntity {

    Set<ProductGroupAttribute> getAttributes();

    Product derive();
}
