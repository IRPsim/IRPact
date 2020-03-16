package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.Group;
import de.unileipzig.irpact.core.need.Need;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface ProductGroup extends Group<Product> {

    Set<ProductGroupAttribute> getAttributes();

    Set<Need> getNeedsSatisfied();

    default boolean satisfy(Need need) {
        return getNeedsSatisfied().contains(need);
    }
}
