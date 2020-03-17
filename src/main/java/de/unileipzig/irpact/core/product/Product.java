package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.GroupEntity;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.simulation.SimulationEntity;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface Product extends SimulationEntity, GroupEntity<Product> {

    ProductGroup getGroup();

    Set<ProductAttribute> getAttributes();

    default boolean satisfy(Need need) {
        return getGroup().satisfy(need);
    }
}
