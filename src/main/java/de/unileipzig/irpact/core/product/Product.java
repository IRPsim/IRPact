package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.GroupEntity;
import de.unileipzig.irpact.core.need.Need;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface Product extends GroupEntity<Product> {

    ProductGroup getGroup();

    String getName();

    Set<ProductAttribute> getAttributes();

    default boolean satisfy(Need need) {
        return getGroup().satisfy(need);
    }
}
