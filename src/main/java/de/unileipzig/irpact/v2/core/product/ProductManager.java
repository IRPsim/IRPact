package de.unileipzig.irpact.v2.core.product;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface ProductManager {

    Collection<ProductGroup> getGroups();

    ProductGroup getGroup(String name);
}
