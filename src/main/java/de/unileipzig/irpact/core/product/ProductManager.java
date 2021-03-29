package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.misc.InitalizablePart;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface ProductManager extends InitalizablePart {

    Collection<ProductGroup> getGroups();

    default int getNumberOfProductGroups() {
        return getGroups().size();
    }

    ProductGroup getGroup(String name);

    void makeKnownInSimulation(Product product);
}
